package versionchecker.actions;

import java.awt.Dimension;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.lib.Ref;
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
	private Boolean lastestFlag = false;

	public VCCloneTask(String id, String version) {
		this.id = id;
		if (version == null) {
			this.lastestFlag = true;
		} else {
			this.version = version;
		}
		try {
			this.makeDBCall();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		// System.out.println(this.gitRepo);
		// System.out.println(this.gitBranch);
		createAndShowGit();
	}

	/**
	 * Query the database for the repo and branch.
	 */
	private void makeDBCall() throws SQLException {
		VCDBCall call = new VCDBCall(VCDBCall.DBI_MYSQL);
		if (this.lastestFlag) {
			this.gitRepo = call.getLastestRepo(id);
		} else {
			this.gitRepo = (String) call.getCurrentRepo(id, version)[0];
			this.gitBranch = (String) call.getCurrentRepo(id, version)[1];
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
					if (this.lastestFlag) {
						// clone.setDirectory(loc).setURI(this.gitRepo).setBranchesToClone(Arrays.asList("refs/heads/master"));
						clone.setDirectory(loc).setURI(this.gitRepo)
								.setCloneAllBranches(true);
					} else {
						// System.out.println("refs/remotes/origin/" +
						// this.gitBranch);
						// clone.setDirectory(loc).setURI(this.gitRepo).setBranchesToClone(Arrays.asList("refs\\remotes\\origin\\"
						// + this.gitBranch));
						// TODO: Fix clone certain branch option
						clone.setDirectory(loc).setURI(this.gitRepo)
								.setCloneAllBranches(true);
					}
					UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(
							vcgw.getLogin(), vcgw.getPass());
					clone.setCredentialsProvider(user);
					clone.call();

					for (Ref b : git.branchList().setListMode(ListMode.ALL)
							.call()) {
						System.out.println("(cloneAllBranches): cloned branch "
								+ b.getName());
					}
					System.out.println("here");

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Clone failed",
							"VersionChecker", 1);
					return;
				}

				JOptionPane.showMessageDialog(null, "Clone successfully",
						"VersionChecker", 1);

			}
		}
	}

}
