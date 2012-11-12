package mavenp2versionmatch.main;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.net.URL;
import junit.framework.TestCase;
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

	/* Some other ideas for test cases (todo: implement later)
	 * what if you pass something like -project -git_commit asdf
	 * */
	
	public void testValidAddMaven(){
		// test with a valid dummy string for a maven add
		/*
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY").split(" ");
		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		assertEquals("Should return true, couldn't translate a string for a valid maven add", true, util.isValidAdd(m));

		assertEquals(numargs, m.size());
		*/
	}
	public void testValidAddP2(){
		/*
		// test with a valid dummy string for a p2 add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY").split(" ");
		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should return true, couldn't translate a string for a valid p2 add", true, util.isValidAdd(m));
		assertEquals(numargs, m.size());
		*/
	}
  /*
	public void testValidAddMissingArgs(){
		// test with an invalid dummy string, missing version info
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch").split(" ");
		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should return false, test arguments missing p2 and maven version info.", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
	}

	public void testValidAddInvalidArgument(){
		// test with a made up argument
		// expected: string will be accepted, but the bad argument will not be included in our Map.
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -invalidarg abcd").split(" ");

		int numargs = (args.length / 2) -1; // exclude bad argument
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		//assertEquals("Should return false, test arguments contain made up argument -invalidarg being accepted", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
	}
  */

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
  /*
	public void testValidAddSameArgumentTwice(){
		// Specify the same argument twice
		// Expected: receive the last specified argument.
		// Maybe we can warn the user that the same argument was passed twice.

		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY -mvnv 0.0-DOUBLE").split(" ");
		Map<String, String>m = MvnP2Util.getOptions(args);

		//Check if 0.0-DOUBLE was assigned
		assertThat(m.get(MavenP2Col.MAVEN_VERSION.getColName()), not(equalTo("0.0-DUMMY")));
		assertEquals(m.get(MavenP2Col.MAVEN_VERSION.getColName()), "0.0-DOUBLE");
	}
  */
}
