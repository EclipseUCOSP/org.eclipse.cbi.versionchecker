package org.eclipse.cbi.versionchecker.ui.actions;

public class VCResponseData {

	private String component;
	private String state;
	private String version;
	private Repoinfo repoinfo;
	public String getComponent() {
		return this.component;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public Repoinfo getRepoinfo() {
		return this.repoinfo;
	}
	
	public static class Repoinfo {
		private String repo;
		private String commit;
		private String branch;
		
		public String getRepo() {
			return this.repo;
		}
		
		public String getCommit() {
			return this.commit;
		}
		
		public String getBranch() {
			return this.branch;
		}
	}
}
