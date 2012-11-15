package mavenp2versionmatch.db;

import mavenp2versionmatch.main.VersionManifest;

import java.sql.*;
import java.util.List;
import java.util.Map;
import mavenp2versionmatch.exception.DBIException;

public interface DBI {
	public void open() throws DBIException;
	public void close() throws DBIException;
	public void addRecord(Map<String, String> colMap) throws DBIException;
	public void updateRecord(Map<String, String> matchMap, Map<String, String> updateMap) throws DBIException;
	public List<VersionManifest> find(Map<String, String> map) throws DBIException;
	public List<VersionManifest> findAll() throws DBIException;
}

