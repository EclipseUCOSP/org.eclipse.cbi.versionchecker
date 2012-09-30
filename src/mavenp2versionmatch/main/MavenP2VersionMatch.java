package mavenp2versionmatch.main;

import java.util.HashMap;
import java.util.Map;


import mavenp2versionmatch.db.MavenP2Col;

public class MavenP2VersionMatch {
	
	/*
	 * Input must contain git repo and commit and one of p2 version
	 * or maven version
	 * @param map database column names and values
	 */
	private static boolean isValidInput(Map<String, String> map) {
		return map.containsKey(MavenP2Col.GIT_REPO.toString()) &&
				map.containsKey(MavenP2Col.GIT_COMMIT.toString()) &&
				(map.containsKey(MavenP2Col.MAVEN_VERSION.toString()) ||
						map.containsKey(MavenP2Col.P2_VERSION.toString()));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		
		for (int i = 0; i< args.length; i++) {
			String key = args[i].substring(1);
			//substring for dash in commandline
			//TODO: is there a better way?
			String val = args[++i];

			if (MavenP2Col.findByStr(key) == null) {
				System.err.println("Invalid argument. No entry for " + key);
				System.exit(-1);
			}
			else {
				map.put(key, val);
			}
		}
			
			if (!isValidInput(map)) {
				System.err.println("Invalid input. Must include git repo, " +
						"commit and one of maven version and p2 version.");
				System.exit(-1);
			}
			else {
			//make sqlitedbi object	
			}
		
		
	}

}
