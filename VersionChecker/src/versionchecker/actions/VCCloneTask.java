package versionchecker.actions;

import java.awt.Dimension;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.ui.IWorkbenchWindow;

public class VCCloneTask {
	private String id;
	private String version;
	private String gitRepo;
	private String gitBranch;
	private Boolean lastestFlag = false;
	private IWorkbenchWindow window;
	
	public VCCloneTask(String id, String version,IWorkbenchWindow window){
		this.window = window;
		this.id = id;
		if (version == null){
			this.lastestFlag = true;
		}else{
			this.version = version;
		}
		try {
			this.makeDBCall();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			MessageDialog.openInformation(
					window.getShell(),
					"VersionChecker",
					"SQLExecption, Please check the database connection");
			return;
		}
		createAndShowGit();
	}
	
	private void makeDBCall() throws SQLException{
		VCDBCall call = new VCDBCall(VCDBCall.DBI_MYSQL);
		if (this.lastestFlag){
			this.gitRepo = call.getLastestRepo(id);
		}else{
			this.gitRepo = (String)call.getCurrentRepo(id, version)[0];
			this.gitBranch = (String)call.getCurrentRepo(id, version)[1];
		}
	}
	
	private void createAndShowGit() {
		Dimension d = new Dimension(400,400);
		
		JFrame f = new JFrame();
		f.setSize(d);
		f.setResizable(false);
		
		VCGitWindow vcgw = new VCGitWindow(f);
		vcgw.show();
		
		if (vcgw.status()){
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Select Destination Folder");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int ret = jfc.showOpenDialog(f);
			
			if (ret == JFileChooser.APPROVE_OPTION){
				File loc = jfc.getSelectedFile();
				FileRepositoryBuilder builder = new FileRepositoryBuilder();
				Repository repository;
				
				try {
					repository = builder.setGitDir(loc).readEnvironment().findGitDir().build();
					Git git = new Git(repository);              
					CloneCommand clone = git.cloneRepository();
					clone.setBare(false);
					if (this.lastestFlag){
						clone.setCloneAllBranches(true);
					}else{
						clone.setBranch(this.gitBranch);
					}
					clone.setDirectory(loc).setURI(this.gitRepo);
					UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(vcgw.getLogin(), vcgw.getPass());
					clone.setCredentialsProvider(user);
					clone.call();
					
					MessageDialog.openInformation(
							window.getShell(),
							"VersionChecker",
							"Clone successfully");
	
				} catch (Exception e) {
					MessageDialog.openInformation(
							window.getShell(),
							"VersionChecker",
							e.getMessage());
				}
	
			}
		}
	}


}
