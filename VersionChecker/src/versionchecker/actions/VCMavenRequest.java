package versionchecker.actions;

import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * VCMavenRequest class is a package for each maven action.
 * There are two types of maven actions, one for fetch, one for query.
 */
public class VCMavenRequest {
	public static int VCMavenQuery = 0;
	public static int VCMavenFetch = 1;
	
	private int requestType;
	private String id;
	private String version;
	private String response;
	
	public VCMavenRequest(int type, String id, String version){
		this.requestType = type;
		this.id = id;
		this.version = version;
	}
	
	public void send(){
		//TODO add maven command
		this.response = VCMavenExecution.RunCommand("");
		
	}
	
	
}
