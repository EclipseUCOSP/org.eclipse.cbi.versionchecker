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
	private static boolean isValidAdd(Map<String, String> map) {
		return map.containsKey(MavenP2Col.GIT_REPO.getColName()) &&
				map.containsKey(MavenP2Col.GIT_COMMIT.getColName()) &&
				(map.containsKey(MavenP2Col.MAVEN_VERSION.getColName()) ||
						map.containsKey(MavenP2Col.P2_VERSION.getColName()));
	}
	
	/*
	 * get command line options starting at index 1
	 * @param args command line input to main
	 * @return map of db column name and input value
	 */
	private static Map<String, String> getOptions(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		
		for (int i = 1; i< args.length; i++) {
			String key = args[i];
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
		//TODO: For debugging. Add to log or usr msg 
		for (String key : map.keySet()) {
			System.out.print(key + ": ");
			System.out.println(map.get(key));
		}
		return map;
	}
	
	/*
	 * attempts to insert a record
	 * @param map of db column name and input value
	 */
	private static void doAdd(Map<String, String> map) {
			
			if (!isValidAdd(map)) {
				System.err.println("Invalid input. Must include git repo, " +
						"commit and one of maven version and p2 version.");
				System.exit(-1);
			}
			else {
			try {
				SQLiteDBI dbi = new SQLiteDBI();
				dbi.addRecord(map);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}
	}
	
	/*
	 * searches a record
	 * @param map of db column name and input value
	 */
	private static void doFind(Map<String, String> map) {
			//TODO: not built in dbi yet
	}
	
	/*
	 * updates a record
	 * @param map of db column name and input value
	 */
	private static void doUpdate(Map<String, String> map) {
			//TODO: not built in dbi yet
	}
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Arguments not found. Must specify one of: " +
					"add, find, update.");
			System.exit(-1);
		}
		
		Command command = Command.findByStr(args[0]);
		if (command == null) {
			System.err.println("Command not found: " + args[0]);
			System.exit(-1);
		}
		Map<String, String> map = getOptions(args);
		doAdd(map);
		
		switch (command) {
		case ADD:
			doAdd(map);
			break;
		case FIND:
			doFind(map);
			break;
		case UPDATE:
			doUpdate(map);
			break;
		default:
			System.err.println("Unexpected command. Should never get here.");
			System.exit(-1);
		}
	}

}
