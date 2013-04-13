package org.eclipse.cbi.versionchecker.db.db;

import org.eclipse.cbi.versionchecker.db.exception.DBIException;
import org.eclipse.cbi.versionchecker.db.main.VersionManifest;

import java.util.List;
import java.util.Map;

public interface DBI {
	public void open() throws DBIException;
	public void close() throws DBIException;
	public void addRecord(VersionManifest vm) throws DBIException;
	public void updateRecord(VersionManifest match, VersionManifest update) throws DBIException;
	public void updateRecordFromMap(Map<String,String> match, Map<String,String> update) throws DBIException; // TODO: make this unnecessary by reworking doUpdate in MvnP2Util to work without maps
	public List<VersionManifest> find(VersionManifest vm) throws DBIException;
	public List<VersionManifest> findFromMap(Map<String, String> map) throws DBIException; // TODO: make this unnecessary by reworking doUpdate in MvnP2Util to work without maps
	public List<VersionManifest> findAll() throws DBIException;
}

