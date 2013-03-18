package org.eclipse.cbi.versiontracker.mojo.test;

import junit.framework.TestCase;

import org.eclipse.cbi.versiontracker.db.main.VersionManifest;

/**
 * Tests the query generation for the version manifest.
 * @author Derek Ludwig
 */
public class VersionManifestTest extends TestCase {

	public void testEmpty() {
		VersionManifest m = new VersionManifest();
		assertEquals(m.createAddQuery(), "add");
	}

	public void testTychoVersion() {
		VersionManifest m = new VersionManifest();
		m.setP2Version("1.0.0.20121109");
		assertEquals(m.createAddQuery(), "add -p2v 1.0.0.20121109");
	}

	public void testMavenVersion() {
		VersionManifest m = new VersionManifest();
		m.setMavenVersion("1.0.0-SNAPSHOT");
		assertEquals(m.createAddQuery(), "add -mvnv 1.0.0-SNAPSHOT");
	}

	public void testCommit() {
		VersionManifest m = new VersionManifest();
		m.setGitCommit("1234abc");
		assertEquals(m.createAddQuery(), "add -cmt 1234abc");
	}

	public void testBranch() {
		VersionManifest m = new VersionManifest();
		m.setGitBranch("master");
		assertEquals(m.createAddQuery(), "add -br master");
	}

	public void testRepository() {
		VersionManifest m = new VersionManifest();
		m.setGitRepo("git@github.com:EclipseUCOSP/mp");
		assertEquals(m.createAddQuery(), "add -repo git@github.com:EclipseUCOSP/mp");
	}

}
