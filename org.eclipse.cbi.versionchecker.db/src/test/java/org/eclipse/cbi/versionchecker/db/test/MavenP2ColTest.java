package org.eclipse.cbi.versionchecker.db.test;

import static org.junit.Assert.*;

import org.eclipse.cbi.versionchecker.db.db.MavenP2Col;
import org.junit.Test;

public class MavenP2ColTest {

	@Test
	public void testFindByStrKnown() {
		// test argument names
		ArgColPair[] args = {
			new ArgColPair("-repo", "git_repo"),
			new ArgColPair("-cmt", "git_commit"),
			new ArgColPair("-br", "git_branch"),
			new ArgColPair("-p", "project"),
			new ArgColPair("-gtag", "git_tag"),
			new ArgColPair("-p2v", "p2_version"),
			new ArgColPair("-mvnv", "maven_version")};

		for(ArgColPair acp : args){
			MavenP2Col col =  MavenP2Col.findByStr(acp.arg);
			assertEquals("All arguments do not map to the correct column", col.getColName(), acp.col);
		}

	}
	@Test
	public void testFindByStrUnknown() {
		// test for failure when passing in unknown argument names.
		MavenP2Col col = MavenP2Col.findByStr("-notarg");
		assertEquals(null, col);
	}


	private class ArgColPair{
		public String arg, col;
		ArgColPair(String arg, String col){
			this.arg = arg;
			this.col = col;
		}
	}


}
