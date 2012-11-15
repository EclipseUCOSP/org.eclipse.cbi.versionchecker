package mavenp2versionmatch.db;

public enum MavenP2Col {
	GIT_REPO("git_repo", "-repo"),
	GIT_COMMIT("git_commit", "-cmt"),
	GIT_BRANCH("git_branch", "-br"),
	PROJECT("project", "-p"),
	GIT_TAG("git_tag", "-gtag"),
	P2_VERSION("p2_version", "-p2v"),
	MAVEN_VERSION("maven_version", "-mvnv")
	;
	private String col_name;
	private String c_arg;

	private MavenP2Col(String col_name, String c_arg) {
		this.col_name = col_name;
		this.c_arg = c_arg;
	}

	public String getColName() {
		return col_name;
	}

	private String getArgName() {
		return c_arg;
	}

	public static MavenP2Col findByStr(String str){
		for (MavenP2Col e : MavenP2Col.values()) {
			if (e.getArgName().equals(str)) {
				return e;
			}
		}
		return null;
	}


}
