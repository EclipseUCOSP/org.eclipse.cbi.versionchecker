package versionchecker.actions;

import java.awt.List;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import mavenp2versionmatch.db.*;
import mavenp2versionmatch.main.VersionManifest;

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
	
	public String getCurrentRepo(String name, String mVersion) throws SQLException{
		this.db.open();
		Map<String, String> query = new HashMap<String,String>();
		query.put("project", name);
		query.put("maven_version", mVersion);
		java.util.List<VersionManifest> result = db.find(query);
		this.db.close();
		String ret = "";
		if (result.size()>0){
			ret = result.get(0).getGitRepo();
		}
		return ret;
	}

}
