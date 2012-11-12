package mavenp2versionmatch.db;

import java.sql.*;
import java.util.List;
import java.util.Map;
public interface DBI {
	public void openDB() throws SQLException;
	public void closeDB() throws SQLException;
	public void addRecord(Map<String, String> colMap) throws SQLException;
	public void updateRecord(Map<String, String> matchMap, Map<String, String> updateMap) throws SQLException;
	public List<MavenP2Version> find(Map<String, String> map) throws SQLException;
	public List<MavenP2Version> findAll() throws SQLException;
}

