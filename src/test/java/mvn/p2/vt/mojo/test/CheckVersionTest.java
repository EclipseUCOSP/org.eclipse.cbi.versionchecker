package mvn.p2.vt.mojo.test;
import java.io.File;
import mvn.p2.vt.mojo.CheckVersion;
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
	 * Test method for {@link mvn.p2.vt.mojo.CheckVersion#execute()}.
	 * @throws Exception 
	 */
	@Test
	public final void testMojo() throws Exception {
		File pom = getTestFile( "src/test/resources/unit/project-to-test/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        CheckVersion vc = (CheckVersion) lookupMojo( "callVC", pom );
        assertNotNull( vc );
        vc.execute();
	}

}
