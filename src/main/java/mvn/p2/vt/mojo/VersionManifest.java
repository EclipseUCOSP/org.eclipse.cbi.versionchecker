package mvn.p2.vt.mojo;

/**
 * A representation of a single version checker record.
 */
public class VersionManifest
{

	private String mvnVersion;
	private String p2Version;
	private String commit;
	private String branch;
	private String repository;

	/**
	 * Creates a query which adds this manifest to the database.
	 */
	public String createAddQuery() {
		String q = "add";
		if (mvnVersion != null) q += " -mvnv " + mvnVersion;
		if (p2Version != null) q += " -p2v " + p2Version;
		if (commit != null) q += " -cmt " + commit;
		if (branch != null) q += " -br " + branch;
		if (repository != null) q += " -repo " + repository;
		return q;
	}

	public void setMavenVersion(String mvnv) {
		mvnVersion = mvnv;
	}
	public void setP2Version(String p2v) {
		p2Version = p2v;
	}
	public void setCommit(String cmt) {
		commit = cmt;
	}
	public void setBranch(String br) {
		branch = br;
	}
	public void setRepository(String repo) {
		repository = repo;
	}
}
