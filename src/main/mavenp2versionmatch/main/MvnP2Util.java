package mavenp2versionmatch.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;


import mavenp2versionmatch.db.MavenP2Col;
import mavenp2versionmatch.db.MavenP2Version;
import mavenp2versionmatch.db.MySQLDBI;
//import mavenp2versionmatch.db.SQLiteDBI;

public class MvnP2Util {
	//protected SQLiteDBI dbi;
	protected static MySQLDBI dbi;
	
	/*
	 * Input must contain git repo and commit and one of p2 version
	 * or maven version
	 * @param map database column names and values
	 */
	protected static boolean isValidAdd(Map<String, String> map) {
		return map.containsKey(MavenP2Col.GIT_REPO.getColName()) &&
				map.containsKey(MavenP2Col.GIT_COMMIT.getColName()) &&
				map.containsKey(MavenP2Col.GIT_BRANCH.getColName()) &&
				(map.containsKey(MavenP2Col.MAVEN_VERSION.getColName()) ||
						map.containsKey(MavenP2Col.P2_VERSION.getColName()));
	}
	
	/*
	 * get command line options starting at index 1
	 * @param args command line input to main
	 * @return map of db column name and input value
	 */
	protected static Map<String, String> getOptions(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		
		for (int i = 1; i< args.length; i++) {
			String key = args[i];
			String val = args[++i];
			MavenP2Col e  = MavenP2Col.findByStr(key);
			
			if (e == null) {
				System.err.println("Invalid argument. No entry for " + key);
				/* mjl: removed a call to System.exit(-1) here. want to handle gracefully, accept any remaining arguments.*/
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
	 * Attempts to insert a record, checking if any matching entries already exist
	 * in the database first.
	 * @param map of db column name and input value
	 */
	protected static void doAdd(Map<String, String> map) {
		if (!isValidAdd(map)) {
			System.err.println("Invalid input. Must include git repo, branch, " +
					"commit and one of maven version and p2 version.");
			System.exit(-1);
		}
		else {
			// check if a matching entry already exists, updating instead of adding if match found
			if (!doUpdate(map)){
				// if no match found, add the new record
				try {
					if (MvnP2Util.dbi == null) {
						dbi = new MySQLDBI();
					}
					dbi.openDB();
					dbi.addRecord(map);
					dbi.closeDB();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}
	
	/*
	 * searches a record
	 * @param map of db column name and input value
	 */
	protected static void doFind(Map<String, String> map) {
		try {
			if (dbi == null) {
				dbi = new MySQLDBI();
			}
			dbi.openDB();
			List<MavenP2Version> mpvList = dbi.find(map);
			
			for(MavenP2Version v: mpvList) {
				System.out.println(v);
			}
			dbi.closeDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Attempts to update a record using the map given, searching for matches in
	 * for the hardcoded list of columns (as seen in filterMap). If any matches found, 
	 * prompts the user to confirm an update, updating if confirmed and canceling 
	 * the database call otherwise.
	 * @param map of db column name and input value
	 * @return true if a matching record was found to update, false otherwise
	 */
	protected static boolean doUpdate(Map<String, String> map) {
		if (!isValidAdd(map)) {
			System.err.println("Invalid input. Must include git repo, branch, " +
					"commit and one of maven version and p2 version.");
			System.exit(-1);
		}
		else{
			try {
				if (dbi == null) {
					dbi = new MySQLDBI();
				}
				dbi.openDB();
				Map<String, String> mvnMap = filterMap(map, MavenP2Col.MAVEN_VERSION);
				Map<String, String> p2Map = filterMap(map, MavenP2Col.P2_VERSION);
				List<MavenP2Version> mvnMatch = dbi.find(mvnMap);
				List<MavenP2Version> p2Match = dbi.find(p2Map);
				if (mvnMatch.size() > 0 || p2Match.size() > 0){
					System.out.println("Matching record found in the database.");
					// matching record found - ask the user if they would like to update 
					if(JOptionPane.showConfirmDialog(null, "Matching record found in the database.\nUpdate the existing record?") == JOptionPane.OK_OPTION){
						if (mvnMatch.size() > 0){
							for (String key : mvnMap.keySet()) map.remove(key);
							dbi.updateRecord(mvnMap, map);
						}
						else{
							for (String key : p2Map.keySet()) map.remove(key);
							dbi.updateRecord(p2Map, map);
						}
					}
					else{
						System.out.println("Nothing updated in database - user cancellation.");
					}
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Filter a given Map of database values to only include required values
	 * (for use in determining whether a given record already exists).
	 * The required values are: git_repo, git_commit, git_branch, and one of
	 * p2_version and maven_version.
	 * @param map Set of command name-value pairs
	 * @param filter Which of p2_version and maven_version to search for matches
	 */
	private static Map<String, String> filterMap(Map<String, String> map, MavenP2Col filter){
		Map<String, String> result = new HashMap<String, String>();
		for (String key : map.keySet()){
			if (key == MavenP2Col.GIT_REPO.getColName() ||
					key == MavenP2Col.GIT_COMMIT.getColName() || 
					key == MavenP2Col.GIT_BRANCH.getColName() ||
					key == filter.getColName()){
				result.put(key, map.get(key));
			}
		}
		return result;
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
		//doAdd(map);
		
		switch (command) {
		case ADD:
			doAdd(map);
			break;
		case FIND:
			doFind(map);
			break;
		case UPDATE:
			boolean success = doUpdate(map);
			if (!success) System.out.println("No matching record found in the database - update cancelled.");
			break;
		default:
			System.err.println("Unexpected command. Should never get here.");
			System.exit(-1);
		}
	}

}
