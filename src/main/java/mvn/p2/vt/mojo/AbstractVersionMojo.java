package mvn.p2.vt.mojo;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
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
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
	MavenProject project;

	/**
	 * Creates a version manifest.
	 */
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
		
		if (project != null) {
			manifest.setProject(project.getName());
		}
		else {
			getLog().warn("Project name could not be detected");
		}
		
		return manifest;
	}

	/**
	 * 
	 * @param repo
	 * @return VersionManifest
	 * @throws IOException
	 * @throws AmbiguousObjectException
	 * Builds a VersionMainfest from a configured Repository 
	 */
	private VersionManifest buildManifest(Repository repo)
			throws IOException, AmbiguousObjectException {
		
		VersionManifest manifest = new VersionManifest();
		
		manifest.setBranch(repo.getBranch());

		ObjectId head = repo.resolve("HEAD");
		if (head != null) manifest.setCommit(head.name());

		Config config = repo.getConfig();
		
		String url = config.getString("remote", upstreamRemote, "url");
		manifest.setRepository(url);
		
		return manifest;
	}
	
	/**
	 * @param repo
	 * @return VersionManifest
	 * @throws IOException
	 * @throws ConfigInvalidException
	 * @throws MojoFailureException
	 * Checks if a submodule is being built and creates a repository if this is the case
	 */
	protected VersionManifest storeSubmodule(Repository repo) throws IOException, ConfigInvalidException, MojoFailureException {
		VersionManifest manifest = null; 
		
		SubmoduleWalk gen = SubmoduleWalk.forIndex(repo);
		
		while (gen.next()) {
			//TODO: better way to get module path?
			String modulePath = repo.getWorkTree().toString() + "/" + gen.getModulesPath();
			
			//mojo running in git submodule?
			if (modulePath.equals(currDir.toString())) {
				manifest = new VersionManifest();
				
				FilenameFilter gitFilter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						if (name.equals(".git")) {
							return true;
						} else {
							return false;
						}
					}
				};
				File currDir = new File(modulePath);
				File[] files = currDir.listFiles(gitFilter);
				//there should be exactly one
				if (files.length != 1) {
					throw new MojoFailureException("Unexpected .git file location");
				}
				Repository srepo  = null;
				
				//for git 1.7.8 and greater
				if (files[0].isFile()) {
				srepo = new FileRepositoryBuilder()
				//TODO better way to get modules path?
				.setGitDir(new File(repo.getWorkTree() + "/.git/modules/" + gen.getDirectory().getName()))
				.build();
				}
				//for git 1.7.7 and below
				else if (files[0].isDirectory()) {
					srepo = new FileRepositoryBuilder()
					.setGitDir(files[0])
					.build();
				}
				else {
					throw new MojoFailureException(".git is neither a file nor a directory");
				}

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
