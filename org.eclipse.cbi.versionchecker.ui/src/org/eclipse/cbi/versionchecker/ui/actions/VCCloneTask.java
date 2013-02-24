package org.eclipse.cbi.versionchecker.ui.actions;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * VCCloneTask represent a life cycle of a git clone action. Including database
 * query for gitRepo and gitBranch, getting user name and password from a UI
 * dialog, actual git operation and error report.
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
		if (this.latestFlag) {
			this.gitRepo = post.getLatestRepo(id);
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
		VCGitWindow vcgw = new VCGitWindow(f);
		vcgw.show();

		if (vcgw.status()) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Select Destination Folder");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int ret = jfc.showOpenDialog(f);

			if (ret == JFileChooser.APPROVE_OPTION) {
				File loc = jfc.getSelectedFile();
				FileRepositoryBuilder builder = new FileRepositoryBuilder();
				Repository repository;

				try {
					repository = builder.setGitDir(loc).readEnvironment()
							.findGitDir().build();
					Git git = new Git(repository);
					CloneCommand clone = git.cloneRepository();
					clone.setBare(false);
					
					// TODO: Fix clone certain branch option
					clone.setDirectory(loc).setURI(this.gitRepo).setCloneAllBranches(true);

					UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
							vcgw.getLogin(), vcgw.getPass());
					clone.setCredentialsProvider(user);
					clone.call();
					
					// TODO: Revert to git commit
					if (!this.latestFlag) {
					}
					

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Clone failed","VersionChecker", 1);
					return;
				}
				
				JOptionPane.showMessageDialog(null, "Clone successfully","VersionChecker", 1);

			}
		}
	}

}