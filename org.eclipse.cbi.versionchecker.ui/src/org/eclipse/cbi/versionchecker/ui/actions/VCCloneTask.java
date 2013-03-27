package org.eclipse.cbi.versionchecker.ui.actions;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * VCCloneTask represents the lifecycle of a git clone action. Includes the database
 * query for gitRepo and gitBranch, retrieval of username and password using a UI
 * dialog, and git execution and error reporting.
 */
public class VCCloneTask {
	private String id;
	private String version;
	private String gitRepo;
	private String gitBranch;
	private String gitCommit;
	private Boolean latestFlag = false;

	public VCCloneTask(String id, String version) {
		this.id = id;
		if (version == null) {
			this.latestFlag = true;
		} else {
			this.version = version;
		}
		try {
			this.sendPostRequest();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"VersionChecker", 1);
			return;
		}
		if (this.gitRepo == "") {
			JOptionPane.showMessageDialog(null,
					"No record for this component in database",
					"VersionChecker", 1);
			return;
		}

		createAndShowGit();
	}
	
	private void sendPostRequest() throws IOException {
		VCPostRequest post = new VCPostRequest();
		HashMap<String, String> hashmap;
		if (this.latestFlag) {
			hashmap = post.getLatestRepo(id);
			this.gitRepo = hashmap.get("repo");
			this.gitBranch = hashmap.get("branch");
		} else {
			hashmap = post.getCurrentRepo(id, version);
			this.gitRepo = hashmap.get("repo");
			this.gitBranch = hashmap.get("branch");
			this.gitCommit = hashmap.get("commit");
		}
			
	}

	/**
	 * Main function for the git clone action.
	 */
	private void createAndShowGit() {
		Dimension d = new Dimension(400, 400);

		JFrame f = new JFrame();
		f.setSize(d);
		f.setResizable(false);

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Select Destination Folder");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ret = jfc.showOpenDialog(f);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File loc = jfc.getSelectedFile();
			try {
				executeCloneCommand(f, loc);
				
				FileRepository localRepo = new FileRepository(loc + "/.git");
				Git git = new Git(localRepo);
				
				git.checkout().setCreateBranch(true)
					.setName(gitBranch)
					.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
					.setStartPoint("origin/" + gitBranch).call();
				
				if (!this.latestFlag) {
					git.reset().setMode(ResetType.HARD).setRef(gitCommit).call();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Clone failed: " + e.getMessage(), "VersionChecker", 1);
				return;
			}
			
			JOptionPane.showMessageDialog(null, "Clone successfully","VersionChecker", 1);

		}
	}

	private void executeCloneCommand(JFrame parentFrame, File loc) throws InvalidRemoteException, TransportException, GitAPIException {
		CloneCommand clone = Git.cloneRepository();
		clone.setBare(false);
		
		clone.setDirectory(loc).setURI(this.gitRepo).setCloneAllBranches(true);
		
		try {
			clone.call();
		} catch (TransportException e) {
			VCGitWindow vcgw = new VCGitWindow(parentFrame);
			vcgw.show();

			if (vcgw.status()) {
				UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
						vcgw.getLogin(), vcgw.getPass());
				clone.setCredentialsProvider(user);
				clone.call();
			}
		}
	}

}
