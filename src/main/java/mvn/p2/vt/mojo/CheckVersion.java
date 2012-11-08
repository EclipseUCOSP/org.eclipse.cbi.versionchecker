package mvn.p2.vt.mojo;

import java.io.IOException;
import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectId;

/**
 * Calls the Version Checker, prompting the user to enter a query to run.
 * The given query is then passed as arguments to the main method in MvnP2Util,
 * which executes the query (valid syntax specified in the 'db' README).
 * @goal callVC
 */
public class CheckVersion extends AbstractMojo
{
	/**
	 * The root of the Git repository.
	 *
	 * @parameter default-value="${basedir}/.git"
	 */
	private File repositoryRoot;

	/**
	 * The name of the upstream remote repository.
	 *
	 * @parameter default-value="origin"
	 */
	private String upstreamRemote;

	/**
	 * The Maven version.
	 *
	 * @parameter expression="${project.version}"
	 * @readonly
	 */
	private String mvnVersion;

	public void execute() throws MojoExecutionException
	{
		String query = "add";
		query += " -mvnv " + mvnVersion;

		// Find the Git revision &c.
		// Note: I do not call readEnvironment on the builder, since undocumented
		// environment variables are the bane of repeatable builds.
		try {
			Repository repo = new FileRepositoryBuilder()
				.setGitDir(repositoryRoot)
				.findGitDir()
				.build();

			String branchName = repo.getBranch();
			if (branchName == null) {
				getLog().warn("Not on a branch");
			} else {
				query += " -br " + branchName;
			}

			ObjectId head = repo.resolve("HEAD");
			if (head == null) {
				getLog().warn("Could not resolve HEAD");
			} else {
				query += " -cmt " + head.name();
			}

			Config config = repo.getConfig();
			String url = config.getString("remote", upstreamRemote, "url");
			if (url == null) {
				getLog().warn("Could not find remote" + upstreamRemote);
			} else {
				query += " -repo " + url;
			}

		} catch (IOException ioe) {
			getLog().error("Unable to read repository");
			ioe.printStackTrace();
			return;
		} catch (IllegalArgumentException iae) {
			getLog().error("Not a Git repository");
			iae.printStackTrace();
			return;
		}

		String[] query_arr = query.split(" ");
		getLog().info("Executing the following query: " + query);
		try {
			// call the MvnP2Util with the given commands. This just starts the main method, set up your array of strings accordingly.
			mavenp2versionmatch.main.MvnP2Util.main(query_arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
