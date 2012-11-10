package mvn.p2.vt.mojo;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Calls the Version Checker with a Maven version.
 * @goal add-maven-version
 */
public class MavenVersionMojo extends AbstractVersionMojo
{
	/**
	 * The Maven version.
	 *
	 * @parameter expression="${project.version}"
	 * @readonly
	 */
	private String mvnVersion;

	@Override
	protected VersionManifest createManifest() throws MojoFailureException {
		VersionManifest manifest = super.createManifest();
		manifest.setMavenVersion(mvnVersion);
		return manifest;
	}
}
