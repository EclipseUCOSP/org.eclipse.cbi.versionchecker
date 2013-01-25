package org.eclipse.cbi.versiontracker.mojo;

import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.cbi.versiontracker.db.main.VersionManifest;

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
