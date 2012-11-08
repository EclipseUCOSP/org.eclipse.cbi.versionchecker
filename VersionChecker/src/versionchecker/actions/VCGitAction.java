package versionchecker.actions;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class VCGitAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public VCGitAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		
		VCXmlParser xp = new VCXmlParser();
		final Object[] data = xp.parse();
	       javax.swing.SwingUtilities.invokeLater(new Runnable() {
	           public void run() {
	               VCArtifactTable.createAndShowGUI(data);
	           }
	       });
		
		
//		Dimension d = new Dimension(400,400);
//		
//		JFrame f = new JFrame();
//		f.setSize(d);
//		f.setResizable(false);
//		
//		VCGitWindow vcgw = new VCGitWindow(f);
//		vcgw.show();
//		
//		if (vcgw.status()){
//			JFileChooser jfc = new JFileChooser();
//			jfc.setDialogTitle("Select Destination Folder");
//			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//			int ret = jfc.showOpenDialog(f);
//			
//			if (ret == JFileChooser.APPROVE_OPTION){
//				File loc = jfc.getSelectedFile();
//				FileRepositoryBuilder builder = new FileRepositoryBuilder();
//				Repository repository;
//				
//				try {
//					repository = builder.setGitDir(loc).readEnvironment().findGitDir().build();
//					Git git = new Git(repository);              
//					CloneCommand clone = git.cloneRepository();
//					clone.setBare(false);
//					clone.setCloneAllBranches(true);
//					clone.setDirectory(loc).setURI(vcgw.getURL());
//					UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider(vcgw.getLogin(), vcgw.getPass());
//					clone.setCredentialsProvider(user);
//					clone.call();
//					
//					MessageDialog.openInformation(
//							window.getShell(),
//							"VersionChecker",
//							"Clone successfully");
//	
//				} catch (Exception e) {
//					MessageDialog.openInformation(
//							window.getShell(),
//							"VersionChecker",
//							e.getMessage());
//				}
//	
//			}
//		}


	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}