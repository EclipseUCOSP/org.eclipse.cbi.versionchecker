package mavenp2versionmatch.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MavenP2Version {
	private String gitRepo;
	private String gitCommit;
	private String gitBranch;
	private String project;
	private String gitTag;
	
	private String p2Version;
	private String mavenVersion;
	
	//constructor
	public MavenP2Version () {
		
	}
	
	//static converter from ResultSet
	
	public static List<MavenP2Version> convertFromResultSet(ResultSet rs) throws SQLException {
		
		List<MavenP2Version> mpvList = new ArrayList<MavenP2Version>();
		MavenP2Version mpv;

		while (rs.next()) {
			mpv = new MavenP2Version();
			mpv.setGitRepo(rs.getString(MavenP2Col.GIT_REPO.toString()));
			mpv.setGitCommit(rs.getString(MavenP2Col.GIT_COMMIT.toString()));
			mpv.setGitBranch(rs.getString(MavenP2Col.GIT_BRANCH.toString()));
			mpv.setProject(rs.getString(MavenP2Col.PROJECT.toString()));
			mpv.setGitTag(rs.getString(MavenP2Col.GIT_TAG.toString()));
			mpv.setP2Version(rs.getString(MavenP2Col.P2_VERSION.toString()));
			mpv.setMavenVersion(rs.getString(MavenP2Col.MAVEN_VERSION.toString()));
			
			mpvList.add(mpv);
		}
		
		return mpvList;
	}
	
	public String toString() {
		String str;
		str = "Repo: \t\t"+this.gitRepo +
				"\nCommit: \t" + this.gitCommit +
				"\nBranch: \t" + this.gitBranch +
				"\nTag: \t\t" + this.gitTag +
				"\nProject: \t" + this.project +
				"\nP2 Version:\t"+ this.p2Version +
				"\nMaven Version:\t" + this.mavenVersion;
		return str;
	}
	//-------------------------------------------
	//Getters and Setters
	//-------------------------------------------
	public String getGitRepo() {
		return gitRepo;
	}

	public void setGitRepo(String gitRepo) {
		this.gitRepo = gitRepo;
	}

	public String getGitCommit() {
		return gitCommit;
	}

	public void setGitCommit(String gitCommit) {
		this.gitCommit = gitCommit;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getGitTag() {
		return gitTag;
	}

	public void setGitTag(String gitTag) {
		this.gitTag = gitTag;
	}

	public String getP2Version() {
		return p2Version;
	}

	public void setP2Version(String p2Version) {
		this.p2Version = p2Version;
	}

	public String getMavenVersion() {
		return mavenVersion;
	}

	public void setMavenVersion(String mavenVersion) {
		this.mavenVersion = mavenVersion;
	}

	public String getGitBranch() {
		return gitBranch;
	}

	public void setGitBranch(String gitBranch) {
		this.gitBranch = gitBranch;
	}
	
	
}
