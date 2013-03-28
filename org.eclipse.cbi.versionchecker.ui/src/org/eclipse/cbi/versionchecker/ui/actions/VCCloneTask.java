package org.eclipse.cbi.versionchecker.ui.actions;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

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
	private Boolean latestFlag = false;
	private VCResponseData artifact = null;
	final private String title = "Version Checker";

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
					title, 1);
			return;
		}
		if (artifact == null || artifact.getState().equals("unavailable")) {
			JOptionPane.showMessageDialog(null,
					"No record for this component in database",
					title, 1);
			return;
		} else if (artifact.getState().equals("alternative")) {
			int n = JOptionPane.showConfirmDialog(null, 
					id + ": " + System.getProperty("line.separator") + 
						"The requested version " + version +" is not available. " + System.getProperty("line.separator") + 
						"Do you want to clone an alternative version " + artifact.getVersion() + "?", 
					title, JOptionPane.YES_NO_OPTION);
			if (n == 1) // no!
				return;
		}

		createAndShowGit();
	}
	
	private void sendPostRequest() throws IOException {
		VCPostRequest post = new VCPostRequest();
		if (this.latestFlag) {
			artifact = post.getLatestRepo(id);
		} else {
			artifact = post.getCurrentRepo(id, version);
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
				
				String branch = artifact.getRepoinfo().getBranch();
				
				git.checkout().setCreateBranch(true)
					.setName(branch)
					.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
					.setStartPoint("origin/" + branch).call();
				
				if (!this.latestFlag) {
					git.reset().setMode(ResetType.HARD).setRef(artifact.getRepoinfo().getCommit()).call();
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
		
		clone.setDirectory(loc).setURI(artifact.getRepoinfo().getRepo()).setCloneAllBranches(true);
		
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
