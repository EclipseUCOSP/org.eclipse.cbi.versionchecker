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

public class MvnP2UtilSqliteTest extends TestCase{
	private MvnP2Util util;

	public MvnP2UtilSqliteTest(String name){
		super(name);
		util = new MvnP2Util();
	}

	/* note: use different filenames for each test's database to avoid any unexpected interactions between different unit tests. */

	public void testSqliteDoAddAndFind() throws SQLException{
		MvnP2Util util = new MvnP2Util();
		URL url = getClass().getClassLoader().getResource("empty.db");
		util.dbi = new SQLiteDBI(url.getPath());

		Map<String, String>mAdd = new HashMap();
		mAdd.put("git_repo", "GITREPO");
		mAdd.put("git_commit", "GITCOMMIT");
		mAdd.put("git_branch", "GITBRANCH");
		mAdd.put("maven_version", "0.0-DUMMY");

		util.doAdd(mAdd);

		// connection is closed after doadd.
		util.dbi = new SQLiteDBI(url.getPath()); // reopen connection
		util.dbi.openDB();
		Map<String, String>mFind = new HashMap();
		mFind.put("maven_version", "0.0-DUMMY");

		List<MavenP2Version> mpvList = util.dbi.find(mFind);

		util.dbi.closeDB(); // done all the queries we will need, close database
		assertEquals("added one entry to empty database, should contain one item. instead, contains " + mpvList.size(), mpvList.size(), 1); // there should only be one entry in the database
	
	}
}
