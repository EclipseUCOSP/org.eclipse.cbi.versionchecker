package mavenp2versionmatch.db;

import mavenp2versionmatch.main.VersionManifest;

import java.sql.*;
import java.util.List;
import java.util.Map;
import mavenp2versionmatch.exception.DBIException;

public interface DBI {
	public void open() throws DBIException;
	public void close() throws DBIException;
	public void addRecord(VersionManifest vm) throws DBIException;
	public void updateRecord(VersionManifest match, VersionManifest update) throws DBIException;
	public List<VersionManifest> find(VersionManifest vm) throws DBIException;
	public List<VersionManifest> findAll() throws DBIException;
}

