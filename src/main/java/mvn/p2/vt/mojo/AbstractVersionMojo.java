package mvn.p2.vt.mojo;

import java.io.IOException;
import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectId;

/**
 * A common base class for version checker mojos.
 */
public class AbstractVersionMojo extends AbstractMojo
{

	/**
	 * The name of the upstream remote repository.
	 *
	 * @parameter default-value="origin"
	 */
	private String upstreamRemote;
	

	/**
	 * Creates a version manifest.
	 */
	protected VersionManifest createManifest() throws MojoFailureException {
		// Find the Git revision &c.
		// Note: I do not call readEnvironment on the builder, since undocumented
		// environment variables are the bane of repeatable builds.
		VersionManifest manifest = new VersionManifest();
		try {
					
			Repository repo = new FileRepositoryBuilder()
				.findGitDir()
				.build();

			manifest.setBranch(repo.getBranch());

			ObjectId head = repo.resolve("HEAD");
			if (head != null) manifest.setCommit(head.name());

			Config config = repo.getConfig();
			
			String url = config.getString("remote", upstreamRemote, "url");
			manifest.setRepository(url);
		} catch (IOException ioe) {
			throw new MojoFailureException("Unable to read repository", ioe);
		} catch (IllegalArgumentException iae) {
			throw new MojoFailureException("Not a Git repository", iae);
		}

		return manifest;
	}

	public void execute() throws MojoExecutionException, MojoFailureException
	{
		VersionManifest manifest = createManifest();
		String query = manifest.createAddQuery();

		String[] query_arr = query.split(" ");
		getLog().info("Executing the following query: " + query);
		try {
			// call the MvnP2Util with the given commands. This just starts the main method, set up your array of strings accordingly.
			mavenp2versionmatch.main.MvnP2Util.main(query_arr);
		} catch (Exception e) {
			throw new MojoFailureException("Failure in MavenP2Util.", e);
		}
	}

}
