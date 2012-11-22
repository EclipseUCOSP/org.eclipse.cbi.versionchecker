package versionchecker.actions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import mavenp2versionmatch.db.DBI;
import mavenp2versionmatch.db.MySQLDBI;
import mavenp2versionmatch.db.SQLiteDBI;
import mavenp2versionmatch.main.VersionManifest;

/**
 * VCDBCall represent a life cycle of a VCDB query.
 * It works with the DB project and use the VersionChecker
 * API.
 */
public class VCDBCall {
	public final static int DBI_MYSQL = 0;
	public final static int DBI_SQLITE = 0;
	
	private DBI db;
	
	public VCDBCall(int dbType){
		if (dbType == VCDBCall.DBI_MYSQL){
			this.db = new MySQLDBI();
		}else if (dbType == VCDBCall.DBI_SQLITE) {
			try {
				this.db = new SQLiteDBI();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public String[] getCurrentRepo(String name, String mVersion) throws SQLException{
		this.db.open();
		Map<String, String> query = new HashMap<String,String>();
		query.put("project", name);
		query.put("p2_version", mVersion);
		java.util.List<VersionManifest> result = db.find(query);
		this.db.close();
		
		String retRepo = "";
		String retBranch = "";
		if (result.size()>0){
			retRepo = result.get(0).getGitRepo();
			retBranch = result.get(0).getGitBranch();
		}
		String[] ret = {retRepo, retBranch};
		return ret;
	}
	
	public String getLastestRepo(String name) throws SQLException{
		this.db.open();
		Map<String, String> query = new HashMap<String,String>();
		query.put("project", name);
		java.util.List<VersionManifest> result = db.find(query);
		this.db.close();
		String ret = "";
		if (result.size()>0){
			ret = result.get(0).getGitRepo();
		}
		return ret;
	}

}
