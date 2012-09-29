package mavenp2versionmatch.db;

import java.io.File;
import java.sql.*;

public class SQLiteDBI {
	private Connection conn;
	private String dbName;
	private String tableName;
	
	public SQLiteDBI (String dbName, String tableName) throws SQLException {
		this.dbName = dbName;
		this.tableName = tableName;
		
		//make sure driver is loaded
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new SQLException("SQLite Driver wasn't loaded");
		}
		//make sure DB exists
		if( ! new File(dbName).exists()) {
			throw new SQLException("Database does not exist");
		}
		
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		
	}
	
	public void closeDB() throws SQLException {
		if( !conn.isClosed()) {
			conn.close();
		}
	}
	
	public void addRecord(String commit, String p2, String maven, String tag) throws SQLException{
		if(!conn.isClosed())
			throw new SQLException("Connection is closed cannot add Record");
		
		//TODO: redo query to eliminate sql inection
		String query = "INSERT INTO "+ this.tableName + "VALUES(" + commit + ", " +tag + ", " + p2 + ", " + maven + ")";
		
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate(query);
	}
}
