package mvn.p2.vt.mojo;

import java.io.IOException;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.submodule.SubmoduleWalk;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.ConfigInvalidException;
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
	 * The projects in the reactor.
	 *
	 * @parameter default-value="${basedir}"
	 */
	private File currDir;

	/**
	 * Creates a version manifest.
	 */
	//TODO: refactor this plugin to run once per build instead of on every module?
	protected VersionManifest createManifest() throws MojoFailureException {
		// Find the Git revision &c.
		// Note: I do not call readEnvironment on the builder, since undocumented
		// environment variables are the bane of repeatable builds.
		VersionManifest manifest = null;
		try {				
			Repository repo = new FileRepositoryBuilder()
				.findGitDir()
				.build();
			
			manifest = storeSubmodule(repo);
			if (manifest == null) {
				manifest = buildManifest(repo);
			}
			
		} catch (IOException ioe) {
			throw new MojoFailureException("Unable to read repository", ioe);
		} catch (IllegalArgumentException iae) {
			throw new MojoFailureException("Not a Git repository", iae);
		} catch (ConfigInvalidException cie) {
			throw new MojoFailureException("Invalid module path", cie);
		}
		
		return manifest;
	}

	private VersionManifest buildManifest(Repository repo)
			throws IOException, AmbiguousObjectException {
		System.out.println(repo);
		
		VersionManifest manifest = new VersionManifest();
		
		manifest.setBranch(repo.getBranch());

		ObjectId head = repo.resolve("HEAD");
		if (head != null) manifest.setCommit(head.name());

		Config config = repo.getConfig();
		
		String url = config.getString("remote", upstreamRemote, "url");
		manifest.setRepository(url);
		
		return manifest;
	}
	
	protected VersionManifest storeSubmodule(Repository repo) throws IOException, ConfigInvalidException {
		VersionManifest manifest = null; 
		
		SubmoduleWalk gen = SubmoduleWalk.forIndex(repo);
		
		while (gen.next()) {
			//TODO: better way to get module path?
			String modulePath = repo.getWorkTree().toString() + "/" + gen.getModulesPath();
			
			//mojo running in git submodule?
			if (modulePath.equals(currDir.toString())) {
				manifest = new VersionManifest();

				//TODO gen.getModules path will work for git 1.7.7 and older
				//TODO better way to get modules path?
				Repository srepo = new FileRepositoryBuilder()
				.setGitDir(new File(repo.getWorkTree() + "/.git/modules/" + gen.getDirectory().getName()))
				.build();

				return buildManifest(srepo);
			}
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
