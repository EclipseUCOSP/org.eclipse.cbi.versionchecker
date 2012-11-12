package mavenp2versionmatch.db.test;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import org.junit.*;
import static org.junit.Assert.*;

import mavenp2versionmatch.db.DBI;
import mavenp2versionmatch.db.SQLiteDBI;
import mavenp2versionmatch.db.MavenP2Version;
import mavenp2versionmatch.main.MvnP2Util;

public class MvnP2UtilSqliteTest {
	static MvnP2Util util;
	static Method doAdd;
	static Class<?> params[];
	static Field dbif;
	static DBI dbi;
	
	@BeforeClass
	//sets up the class to use reflection for private methods
	public static void setUp() throws Exception {
		util = new MvnP2Util();
		URL url = MvnP2UtilSqliteTest.class.getClassLoader().getResource("empty.db");
		dbi = new SQLiteDBI(url.getPath());
		dbi.openDB();
		
		params = new Class[1];
		params[0] = Map.class;
		doAdd = MvnP2Util.class.getDeclaredMethod("doAdd", params);
		doAdd.setAccessible(true);
		
		dbif = MvnP2Util.class.getDeclaredField("dbi");
		dbif.setAccessible(true);
		dbif.set(util, dbi);
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		dbi.closeDB(); // close database
	}
	/* note: use different filenames for each test's database to avoid any unexpected interactions between different unit tests. */

	@Test
	public void testSqliteDoAddAndFind() throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{

		Map<String, String>mAdd = new HashMap<String, String>();
		mAdd.put("git_repo", "GITREPO");
		mAdd.put("git_commit", "GITCOMMIT");
		mAdd.put("git_branch", "GITBRANCH");
		mAdd.put("maven_version", "0.0-DUMMY");
		
		Object args[] = new Object[1];
		args[0] = mAdd;
		
		Integer oldsize = dbi.findAll().size();

		doAdd.invoke(util, args);

		Map<String, String>mFind = new HashMap<String, String>();
		mFind.put("maven_version", "0.0-DUMMY");

		List<MavenP2Version> mpvList = dbi.find(mFind);

		assertEquals("added one entry to database, should contain " + oldsize + 1 + "instead, contains " + mpvList.size(), mpvList.size(), oldsize + 1);
		MavenP2Version mpv = mpvList.get(0);
		assertEquals(mpv.getGitRepo(), "GITREPO");
		assertEquals(mpv.getGitCommit(), "GITCOMMIT");
		assertEquals(mpv.getGitBranch(), "GITBRANCH");
		assertEquals(mpv.getMavenVersion(), "0.0-DUMMY");

	
	}
}
