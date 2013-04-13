package org.eclipse.cbi.versionchecker.ui.actions;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
	final private static String TITLE = "Version Checker";

	private String id;
	private String version;
	private Boolean latestFlag = false;
	private List<VCResponseData> artifacts = null;
	private VCResponseData selectedArtifact = null;

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
			JOptionPane.showMessageDialog(null, e.getMessage(), TITLE, 1);
			return;
		}
		
		String lineSeparator = System.getProperty("line.separator");
		if (artifacts == null || artifacts.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No record for this component in database", TITLE, 1);
			return;
		} else if (this.latestFlag) {
			String message = String.format("Do you want to clone the latest version %s?", 
											artifacts.get(0).getVersion());
			int n = JOptionPane.showConfirmDialog(null, message, TITLE, JOptionPane.YES_NO_OPTION);
			if (n == 1) {
				// no!
				return;
			}
			
			selectedArtifact = artifacts.get(0);
		} else {
			String[] choices = new String[artifacts.size()];
			for (int i = 0; i < artifacts.size(); i++)
				choices[i] = artifacts.get(i).getVersion();
			
			String message = String.format("%s: %sThe requested version %s is not available. %sDo you want to clone an alternative version?", 
											id, lineSeparator, version, lineSeparator);
			
			String input = (String) JOptionPane.showInputDialog(null, message, TITLE, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			if (input == null) {
				// cancel!
				return;
			}
			for (int i = 0; i < artifacts.size(); i++)
				if (choices[i] == input)
					selectedArtifact = artifacts.get(i);
		}

		createAndShowGit();
	}
	
	private void sendPostRequest() throws IOException {
		VCPostRequest post = new VCPostRequest();
		if (latestFlag) {
			artifacts = post.getLatestRepo(id);
		} else {
			artifacts = post.getCurrentRepo(id, version);
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
		jfc.setDialogTitle("Select an Empty Folder as Destination");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ret = jfc.showOpenDialog(f);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File loc = jfc.getSelectedFile();
			try {
				executeCloneCommand(f, loc);
				
				FileRepository localRepo = new FileRepository(loc + "/.git");				
				Git git = new Git(localRepo);
				
				String branch = selectedArtifact.getRepoinfo()
										.getBranch();
				
				if (!branch.equals("master")) {
					git.checkout()
						.setCreateBranch(true)
						.setName(branch)
						.setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
						.setStartPoint("origin/" + branch).call();
				}
				
				git.reset()
					.setMode(ResetType.HARD)
					.setRef(selectedArtifact.getRepoinfo().getCommit())
					.call();
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
		
		clone.setDirectory(loc)
				.setURI(selectedArtifact.getRepoinfo().getRepo())
				.setCloneAllBranches(true);
		
		try {
			clone.call();
		} catch (TransportException e) {
			VCGitWindow vcgw = new VCGitWindow(parentFrame);
			vcgw.show();

			if (vcgw.status()) {
				UsernamePasswordCredentialsProvider user = 
					new UsernamePasswordCredentialsProvider(vcgw.getLogin(), vcgw.getPass());

				clone.setCredentialsProvider(user)
						.call();
			}
		}
	}

}
