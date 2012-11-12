package mavenp2versionmatch.main;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.net.URL;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import mavenp2versionmatch.db.MavenP2Col;
import mavenp2versionmatch.db.SQLiteDBI;
import mavenp2versionmatch.db.MavenP2Version;

public class MvnP2UtilTest extends TestCase{
	private MvnP2Util util;

	public MvnP2UtilTest(String name){
		super(name);
		util = new MvnP2Util();
	}

	/** TODO Implement more test cases.
	 * e.g. what if you pass something like -project -git_commit asdf
	 * e.g. what if you pass just -cmt
	 */
	
	public void testValidAddMaven() throws InvalidManifestException {
		// test with a valid dummy string for a maven add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY").split(" ");
		VersionManifest m = MvnP2Util.createManifest(args);

		// Should not throw
		MvnP2Util.validateAdd(m);
	}
	public void testValidAddP2() throws InvalidManifestException {
		// test with a valid dummy string for a p2 add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY").split(" ");
		VersionManifest m = MvnP2Util.createManifest(args);

		// Should not throw
		MvnP2Util.validateAdd(m);
	}

	@Test(expected=InvalidManifestException.class)
	public void testValidAddMissingArgs(){
		// test with an invalid dummy string, missing version info
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch").split(" ");
		VersionManifest m = util.createManifest(args);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testValidAddInvalidArgument(){
		// test with a made up argument
		// expected: throws an IllegalArgumentException
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -invalidarg abcd").split(" ");
	}

	/* rethink this
	public void testValidAddMissingValue(){
		// test with an argument that is missing a value.
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -project").split(" ");

		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should be invalid, is missing a value on -project argument", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
		
	}
	*/
	public void testValidAddSameArgumentTwice(){
		// Specify the same argument twice
		// Expected: receive the last specified argument.
		// Maybe we can warn the user that the same argument was passed twice.

		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY -mvnv 0.0-DOUBLE").split(" ");
		VersionManifest m = MvnP2Util.createManifest(args);

		//Check if 0.0-DOUBLE was assigned
		assertThat(m.getMavenVersion(), not(equalTo("0.0-DUMMY")));
		assertEquals(m.getMavenVersion(), "0.0-DOUBLE");
	}
}
