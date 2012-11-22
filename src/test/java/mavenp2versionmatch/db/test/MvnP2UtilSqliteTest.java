package mavenp2versionmatch.db.test;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.net.URL;
import java.net.URLDecoder;
import java.lang.reflect.Constructor;
import junit.framework.TestCase;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import mavenp2versionmatch.main.VersionManifest;
import mavenp2versionmatch.main.InvalidManifestException;
import mavenp2versionmatch.main.MvnP2Util;
import mavenp2versionmatch.db.MavenP2Col;
import mavenp2versionmatch.db.SQLiteDBI;
import mavenp2versionmatch.db.DBI;
import mavenp2versionmatch.exception.DBIException;
import mavenp2versionmatch.db.MavenP2Version;

public class MvnP2UtilSqliteTest extends TestCase{
	public MvnP2UtilSqliteTest(String name){
		super(name);
	}

	/* note: use different filenames for each test's database to avoid any unexpected interactions between different unit tests. */

	public void testSqliteDoAddAndFind() throws DBIException, InvalidManifestException {
		URL url = getClass().getClassLoader().getResource("empty.db");
		MvnP2Util util = null;
		try {
			Constructor<MvnP2Util> cons = MvnP2Util.class.getDeclaredConstructor(DBI.class);
			cons.setAccessible(true);
			// convert to URI instead of using getPath(), to handle spaces in file path
			util = cons.newInstance(new SQLiteDBI(url.toURI().getPath()));
		} catch (Exception e) {
			fail("Unable to construct a MvnP2Util with a SQLiteDBI");
		}
		VersionManifest mft = new VersionManifest();
		mft.setGitRepo("GITREPO");
		mft.setGitCommit("102909bacbf86c8e9024fcee4378dbc8223b6a1e");
		mft.setGitBranch("GITBRANCH");
		mft.setMavenVersion("0.0-DUMMY");

		util.add(mft);

		VersionManifest query = new VersionManifest();
		query.setMavenVersion("0.0-DUMMY");
		List<VersionManifest> mpvList = util.find(query);

		assertEquals("added one entry to empty database, should contain one item. instead, contains " + mpvList.size(), mpvList.size(), 1);
		VersionManifest mpv = mpvList.get(0);
		assertEquals(mpv.getGitRepo(), "GITREPO");
		assertEquals(mpv.getGitCommit(), "102909bacbf86c8e9024fcee4378dbc8223b6a1e");
		assertEquals(mpv.getGitBranch(), "GITBRANCH");
		assertEquals(mpv.getMavenVersion(), "0.0-DUMMY");
	}
}
