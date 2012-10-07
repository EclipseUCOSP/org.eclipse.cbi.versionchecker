package mavenp2versionmatch.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


import mavenp2versionmatch.db.MavenP2Col;
import mavenp2versionmatch.db.SQLiteDBI;

public class MavenP2VersionMatch {
	
	/*
	 * Input must contain git repo and commit and one of p2 version
	 * or maven version
	 * @param map database column names and values
	 */
	private static boolean isValidInput(Map<String, String> map) {
		return map.containsKey(MavenP2Col.GIT_REPO.getColName()) &&
				map.containsKey(MavenP2Col.GIT_COMMIT.getColName()) &&
				(map.containsKey(MavenP2Col.MAVEN_VERSION.getColName()) ||
						map.containsKey(MavenP2Col.P2_VERSION.getColName()));
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
			MavenP2Col e  = MavenP2Col.findByStr(key);
			
			if (e == null) {
				System.err.println("Invalid argument. No entry for " + key);
				System.exit(-1);
			}
			else {
				map.put(e.getColName(), val);
			}
		}
			
			if (!isValidInput(map)) {
				System.err.println("Invalid input. Must include git repo, " +
						"commit and one of maven version and p2 version.");
				System.exit(-1);
			}
			else {
			try {
				SQLiteDBI dbi = new SQLiteDBI();
				//TODO: For debugging. Add to log or usr msg 
				for (String key : map.keySet()) {
					System.out.print(key + ": ");
					System.out.println(map.get(key));
				}
				dbi.addRecord(map);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}
		
		
	}

}
