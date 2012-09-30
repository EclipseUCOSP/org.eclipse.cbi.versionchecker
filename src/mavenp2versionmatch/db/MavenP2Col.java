package mavenp2versionmatch.db;

public enum MavenP2Col {
	GIT_REPO
	{
		public String toString() {
			return "repo";
		}
	},
	GIT_COMMIT
	{
		public String toString() {
			return "cmmit";
		}
	},	 
	GIT_TAG
		{
		public String toString() {
			return "gtag";
		}
	},
	P2_VERSION
	{
		public String toString() {
			return "p2_v";
		}
	},
	MAVEN_VERSION
	{
		public String toString() {
			return "mvn_v";
		}
	},
	;
	
	public static MavenP2Col findByStr(String str){
	    for(MavenP2Col e : MavenP2Col.values()){
	        if( e.equals(str)){
	            return e;
	        }
	    }
	    return null;
	}


}
