package mavenp2versionmatch.main.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;

import mavenp2versionmatch.db.MavenP2Col;
import mavenp2versionmatch.main.MvnP2Util;

public class MvnP2UtilTest {
	static MvnP2Util util;
	static Method getOptions;
	static Class<?> goParams[];
	static Method isValid;
	static Class<?> ivParams[];


	/* Some other ideas for test cases (todo: implement later)
	 * what if you pass something like -project -git_commit asdf
	 * */
	@BeforeClass
	public static void setup() throws SecurityException, NoSuchMethodException {
		util = new MvnP2Util();
		
		goParams = new Class[1];
		goParams[0] = String[].class;
		getOptions = MvnP2Util.class.getDeclaredMethod("getOptions", goParams);
		getOptions.setAccessible(true);
		
		ivParams = new Class[1];
		ivParams[0] = Map.class;
		isValid = MvnP2Util.class.getDeclaredMethod("isValidAdd", ivParams);
		isValid.setAccessible(true);
		
	}
	
	@Test
	public void testValidAddMaven() throws Exception {
		// test with a valid dummy string for a maven add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY").split(" ");
		int numargs = (args.length - 1) / 2;
		
		Object goargcontainer[] = new Object[1];
		goargcontainer[0] = args;
		
		Object result = getOptions.invoke(util, goargcontainer);
		Map<?, ?> m = null;
		
		if (result instanceof Map) {
			m = (Map<?, ?>)result;
		}
		else {
			fail("getOptions returned an unexpected type");
		}
		
		Object ivargcontainer[] = new Object[1];
		ivargcontainer[0] = m;
		
		assertEquals("Should return true, couldn't translate a string for a valid maven add", 
				true, isValid.invoke(util, ivargcontainer));

		assertEquals(numargs, m.size());
	}
	
	@Test
	public void testValidAddP2() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// test with a valid dummy string for a p2 add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY").split(" ");
		int numargs = (args.length - 1) / 2;
		
		
		Object goargcontainer[] = new Object[1];
		goargcontainer[0] = args;
		
		Object result = getOptions.invoke(util, goargcontainer);
		Map<?, ?> m = null;
		
		if (result instanceof Map) {
			m = (Map<?, ?>)result;
		}
		else {
			fail("getOptions returned an unexpected type");
		}
		
		Object ivargcontainer[] = new Object[1];
		ivargcontainer[0] = m;
		
		assertEquals("Should return true, couldn't translate a string for a valid p2 add", 
				true, isValid.invoke(util, ivargcontainer));
		assertEquals(numargs, m.size());
	}
	
	@Test
	public void testValidAddMissingArgs() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// test with an invalid dummy string, missing version info
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch").split(" ");
		int numargs = (args.length - 1) / 2;
		
		Object goargcontainer[] = new Object[1];
		goargcontainer[0] = args;
		
		Object result = getOptions.invoke(util, goargcontainer);
		Map<?, ?> m = null;
		
		if (result instanceof Map) {
			m = (Map<?, ?>)result;
		}
		else {
			fail("getOptions returned an unexpected type");
		}
		
		Object ivargcontainer[] = new Object[1];
		ivargcontainer[0] = m;
		
		assertEquals("Should return false, test arguments missing p2 and maven version info.", 
				false, isValid.invoke(util, ivargcontainer));
		assertEquals(numargs, m.size());
	}

	@Test
	public void testValidAddInvalidArgument() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// test with a made up argument
		// expected: string will be accepted, but the bad argument will not be included in our Map.
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -invalidarg abcd").split(" ");

		int numargs = (args.length / 2) -1; // exclude bad argument
		Object goargcontainer[] = new Object[1];
		goargcontainer[0] = args;
		
		Object result = getOptions.invoke(util, goargcontainer);
		Map<?, ?> m = null;
		
		if (result instanceof Map) {
			m = (Map<?, ?>)result;
		}
		else {
			fail("getOptions returned an unexpected type");
		}
		
		//assertEquals("Should return false, test arguments contain made up argument -invalidarg being accepted", false, util.isValidAdd(m));
		assertEquals(numargs, m.size());
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
	
	@Test
	public void testValidAddSameArgumentTwice() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// Specify the same argument twice
		// Expected: receive the last specified argument.
		// Maybe we can warn the user that the same argument was passed twice.

		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY -mvnv 0.0-DOUBLE").split(" ");
		Object goargcontainer[] = new Object[1];
		goargcontainer[0] = args;
		
		Object result = getOptions.invoke(util, goargcontainer);
		Map<?, ?> m = null;
		
		if (result instanceof Map) {
			m = (Map<?, ?>)result;
		}
		else {
			fail("getOptions returned an unexpected type");
		}

		//Check if 0.0-DOUBLE was assigned
		assertFalse(m.get(MavenP2Col.MAVEN_VERSION.getColName()).equals("0.0-DUMMY"));
		assertEquals(m.get(MavenP2Col.MAVEN_VERSION.getColName()), "0.0-DOUBLE");
	}
}
