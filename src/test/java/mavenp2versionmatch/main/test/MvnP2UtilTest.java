package mavenp2versionmatch.main.test;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.net.URL;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import mavenp2versionmatch.main.InvalidManifestException;
import mavenp2versionmatch.main.MvnP2Util;
import mavenp2versionmatch.main.VersionManifest;
import mavenp2versionmatch.db.MavenP2Col;
import mavenp2versionmatch.db.SQLiteDBI;
import mavenp2versionmatch.db.MavenP2Version;

public class MvnP2UtilTest extends TestCase{

	Method createManifestMethod;
	Method validateMethod;
	Method validateAddMethod;

	public MvnP2UtilTest(String name){
		super(name);
		Class<?> klass = MvnP2Util.class;
		try {
			createManifestMethod = klass.getDeclaredMethod("createManifest", String[].class);
			validateMethod = klass.getDeclaredMethod("validate", VersionManifest.class);
			validateAddMethod = klass.getDeclaredMethod("validateAdd", VersionManifest.class);

			createManifestMethod.setAccessible(true);
			validateMethod.setAccessible(true);
			validateAddMethod.setAccessible(true);
		} catch (NoSuchMethodException e) {
			fail("Could not find the expected methods");
		}
	}

	/** TODO Implement more test cases.
	 * e.g. what if you pass something like -project -git_commit asdf
	 * e.g. what if you pass just -cmt
	 */
	
	@Test
	public void testValidAddMaven() throws InvalidManifestException, IllegalAccessException, InvocationTargetException {
		// test with a valid dummy string for a maven add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY").split(" ");
		VersionManifest m = (VersionManifest)createManifestMethod.invoke(null, new Object[] { args });

		// Should not throw
		validateAddMethod.invoke(null, m);
	}
	@Test
	public void testValidAddP2() throws InvalidManifestException, IllegalAccessException, InvocationTargetException {
		// test with a valid dummy string for a p2 add
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY").split(" ");
		VersionManifest m = (VersionManifest)createManifestMethod.invoke(null, new Object[] { args });

		// Should not throw
		validateAddMethod.invoke(null, m);
	}

	@Test(expected=InvalidManifestException.class)
	public void testValidAddMissingArgs() throws Exception {
		// test with an invalid dummy string, missing version info
		InvalidManifestException ime = null;
		try {
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch").split(" ");
		VersionManifest m = (VersionManifest)createManifestMethod.invoke(null, new Object[] { args });

		validateAddMethod.invoke(null, m);
		} catch (InvocationTargetException e) {
			ime = (InvalidManifestException)e.getCause();
		}

		assertNotNull(ime);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testValidAddInvalidArgument() throws Exception {
		// test with a made up argument
		// expected: throws an IllegalArgumentException
		IllegalArgumentException iae = null;
		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -p2v 0.0-DUMMY -invalidarg abcd").split(" ");
		try {
			VersionManifest m = (VersionManifest)createManifestMethod.invoke(null, new Object[] { args });
		} catch (InvocationTargetException e) {
			iae = (IllegalArgumentException)e.getCause();
		}

		assertNotNull(iae);
	}

	@Test
	public void testValidAddSameArgumentTwice() throws IllegalAccessException, InvocationTargetException {
		// Specify the same argument twice
		// Expected: receive the last specified argument.
		// Maybe we can warn the user that the same argument was passed twice.

		String[] args = new String("add -repo dummyrepo -cmt dummycommit -br dummybranch -mvnv 0.0-DUMMY -mvnv 0.0-DOUBLE").split(" ");
		VersionManifest m = (VersionManifest)createManifestMethod.invoke(null, new Object[] { args });

		//Check if 0.0-DOUBLE was assigned
		assertThat(m.getMavenVersion(), not(equalTo("0.0-DUMMY")));
		assertEquals(m.getMavenVersion(), "0.0-DOUBLE");
	}
}
