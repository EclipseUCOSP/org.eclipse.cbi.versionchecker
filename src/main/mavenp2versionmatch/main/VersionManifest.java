package mavenp2versionmatch.main;

import mavenp2versionmatch.db.MavenP2Col;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VersionManifest {
	// Fields
	private String gitRepo;
	private String gitCommit;
	private String gitBranch;
	private String project;
	private String gitTag;

	private String p2Version;
	private String mavenVersion;

	// Methods
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

	//Getters and Setters
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

	public static List<VersionManifest> fromResultSet(ResultSet rs) throws SQLException {

		List<VersionManifest> mpvList = new ArrayList<VersionManifest>();
		VersionManifest mpv;

		while (rs.next()) {
			mpv = new VersionManifest();
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

	/**
	 * Create a map from this manifest.
	 *
	 */
	public Map<String,String> createMap() {
		Map<String, String> map = new HashMap<String, String>();

		String commit = this.getGitCommit();
		String repo = this.getGitRepo();
		String branch = this.getGitBranch();
		String project = this.getProject();
		String p2Version = this.getP2Version();
		String mavenVersion = this.getMavenVersion();

		if (commit != null) map.put(MavenP2Col.GIT_COMMIT.getColName(), commit);
		if (branch != null) map.put(MavenP2Col.GIT_BRANCH.getColName(), branch);
		if (repo != null) map.put(MavenP2Col.GIT_REPO.getColName(), repo);
		if (project != null) map.put(MavenP2Col.PROJECT.getColName(), project);
		if (p2Version != null) map.put(MavenP2Col.P2_VERSION.getColName(), p2Version);
		if (mavenVersion != null) map.put(MavenP2Col.MAVEN_VERSION.getColName(), mavenVersion);

		return map;
	}

}
