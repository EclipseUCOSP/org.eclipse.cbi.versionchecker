package org.eclipse.cbi.versiontracker.mojo;

import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.cbi.versiontracker.db.main.VersionManifest;

/**
 * Calls the Version Checker with a p2/Tycho generated version.
 * @goal add-tycho-version
 */
public class TychoVersionMojo extends AbstractVersionMojo
{
	/**
	 * The p2 unqualified version.
	 *
	 * @parameter default-value="${unqualifiedVersion}"
	 */
	private String p2UnqualifiedVersion;
	
	/**
	 * The p2 qualified version
	 * 
	 * @parameter default-value="${buildQualifier}"
	 */
	private String buildQualifier;
	
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
		String p2Version = "";
		
		//TODO: once we figure out why this is happening we can throw an error and fail the build instead
		if (p2UnqualifiedVersion == null || p2UnqualifiedVersion.equals("${unqualifiedVersion}")) {
			getLog().error("Cannot find p2Version. Inserting invalid value to avoid build failure.");
		} else {
			if (buildQualifier == null || buildQualifier.equals("${buildQualifier}")) {
				p2Version = p2UnqualifiedVersion;
			} else {
				p2Version = p2UnqualifiedVersion + "." + buildQualifier;
			}
		}
		manifest.setP2Version(p2Version);
		manifest.setMavenVersion(mvnVersion);
		
		if (mvnVersion == null) {
			getLog().warn("Maven version could not be detected");
		}
		
		return manifest;
	}
}
