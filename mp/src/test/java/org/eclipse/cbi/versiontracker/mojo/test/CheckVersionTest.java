package org.eclipse.cbi.versiontracker.mojo.test;

import java.io.File;
import org.eclipse.cbi.versiontracker.mojo.MavenVersionMojo;
import org.eclipse.cbi.versiontracker.mojo.TychoVersionMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

/**
 * @author Caroline
 *
 */
public class CheckVersionTest extends AbstractMojoTestCase {
	/** {@inheritDoc} */
	protected void setUp()
		throws Exception
	{
		super.setUp();
	}

	/** {@inheritDoc} */
	protected void tearDown()
		throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test method for {@link org.eclipse.cbi.versiontracker.mojo.MavenVersionMojo#execute()}.
	 * @throws Exception 
	 */
	@Test
	public final void testMavenVersionMojo() throws Exception {
		File pom = getTestFile( "src/test/resources/unit/maven-project/pom.xml" );
		assertNotNull( pom );
		assertTrue( pom.exists() );

		MavenVersionMojo vc = (MavenVersionMojo) lookupMojo( "add-maven-version", pom );
		assertNotNull( vc );
		vc.execute();
	}

	/**
	 * Test method for {@link org.eclipse.cbi.versiontracker.mojo.TychoVersionMojo#execute()}.
	 * @throws Exception 
	 */
	@Test
	public final void testTychoVersionMojo() throws Exception {
		File pom = getTestFile( "src/test/resources/unit/tycho-project/pom.xml" );
		assertNotNull( pom );
		assertTrue( pom.exists() );

		TychoVersionMojo vc = (TychoVersionMojo) lookupMojo( "add-tycho-version", pom );
		assertNotNull( vc );
		vc.execute();
	}
}
