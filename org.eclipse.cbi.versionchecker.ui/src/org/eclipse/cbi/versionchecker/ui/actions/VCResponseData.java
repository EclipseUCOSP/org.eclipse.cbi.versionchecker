package org.eclipse.cbi.versionchecker.ui.actions;

public class VCResponseData {

	private String component;
	private Current current;
	public String getComponent() {
		return this.component;
	}
	
	public Current getCurrent() {
		return this.current;
	}
	
	public static class Current {
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
