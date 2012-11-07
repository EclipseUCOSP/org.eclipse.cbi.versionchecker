package mavenp2versionmatch.main;
import java.util.Map;
import junit.framework.TestCase;

public class MvnP2UtilTest extends TestCase{
	private MvnP2Util util;

	public MvnP2UtilTest(String name){
		super(name);
		util = new MvnP2Util();
	}

	
	public void testMavenAddValid() throws Exception {
		// test with a valid dummy string for a maven add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY").split(" ");
		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		assertEquals("Should return true, couldn't translate a string for a valid maven add", true, util.isValidAdd(m));

		assertEquals(numargs, m.size());
	}
	public void testP2AddValid() throws Exception {
		// test with a valid dummy string for a p2 add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY").split(" ");
		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should return true, couldn't translate a string for a valid p2 add", true, util.isValidAdd(m));
		assertEquals(numargs, m.size());
	}
	public void testValidAddMissingArgs() throws Exception {
		// test with an invalid dummy string, missing version info
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch").split(" ");
		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should return false, test arguments missing p2 and maven version info.", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
	}

	public void testValidAddInvalidArgument() throws Exception{
		// test with a made up argument
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -invalidarg abcd").split(" ");

		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should return false, test arguments contain made up argument -invalidarg being accepted", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
	}

	public void testValidAddMissingValue() throws Exception{
		// test with an argument that is missing a value.
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -project").split(" ");

		int numargs = (args.length - 1) / 2;
		Map<String, String>m = MvnP2Util.getOptions(args);
		
		assertEquals("Should be invalid, is missing a value on -project argument", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
		
	}

}
