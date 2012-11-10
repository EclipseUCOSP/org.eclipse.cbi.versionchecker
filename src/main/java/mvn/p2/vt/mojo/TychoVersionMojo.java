package mvn.p2.vt.mojo;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Calls the Version Checker with a p2/Tycho generated version.
 * @goal add-tycho-version
 */
public class TychoVersionMojo extends AbstractVersionMojo
{
	/**
	 * The p2 version.
	 *
	 * @parameter expression="${unqualifiedVersion}.${buildQualifier}"
	 * @readonly
	 */
	private String p2Version;

	@Override
	protected VersionManifest createManifest() throws MojoFailureException {
		VersionManifest manifest = super.createManifest();
		manifest.setP2Version(p2Version);
		return manifest;
	}
}
