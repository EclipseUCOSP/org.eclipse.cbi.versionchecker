package mavenp2versionmatch.db;

import mavenp2versionmatch.main.VersionManifest;

import java.sql.*;
import java.util.List;
import java.util.Map;

public interface DBI {
	public void open() throws SQLException;
	public void close() throws SQLException;
	public void addRecord(Map<String, String> colMap) throws SQLException;
	public void updateRecord(Map<String, String> matchMap, Map<String, String> updateMap) throws SQLException;
	public List<VersionManifest> find(Map<String, String> map) throws SQLException;
	public List<VersionManifest> findAll() throws SQLException;
}

