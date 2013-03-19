package org.eclipse.cbi.versionchecker.ui.actions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import com.google.gson.Gson;

public class VCPostRequest {
	public HashMap<String, String> getLatestRepo(String name) throws IOException {
		HashMap<String, String> hashmap = new HashMap<String, String>();
		
		Properties prop = new Properties();
		prop.load(this.getClass().getResourceAsStream("/config.properties"));

		URL url = new URL(prop.getProperty("webserviceurl"));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		// TODO: need to pass component name to the web service
		String urlParameters = "";
		
		// Implicitly set the request method to POST
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		// Create I/O streams
		DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		DataInputStream inStream = new DataInputStream(connection.getInputStream());
		
		// Send request
		outStream.writeBytes(urlParameters);
		outStream.flush();
		outStream.close();
		
		// Get response
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder builder = new StringBuilder();
		int cp;
		while ((cp = reader.read()) != -1) {
			builder.append((char) cp);
		}
		inStream.close();
		
		String jsonText = builder.toString();
				
		VCResponseData artifact = new Gson().fromJson(jsonText, VCResponseData.class);
		hashmap.put("repo", artifact.getCurrent().getRepo());
		hashmap.put("branch", artifact.getCurrent().getBranch());
		hashmap.put("commit", artifact.getCurrent().getCommit());
		
		return hashmap;
	}
	
	// TODO: this is just a copy of getLatestRepo. Need to be changed when the web service is done.
	public HashMap<String, String> getCurrentRepo(String name, String version) throws IOException {
		HashMap<String, String> hashmap = new HashMap<String, String>();
		
		Properties prop = new Properties();
		prop.load(this.getClass().getResourceAsStream("/config.properties"));

		URL url = new URL(prop.getProperty("webserviceurl"));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// TODO: need to pass component name and version to the web service
		String urlParameters = "";
		
		// Implicitly set the request method to POST
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		// Create I/O streams
		DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		DataInputStream inStream = new DataInputStream(connection.getInputStream());
		
		// Send request
		outStream.writeBytes(urlParameters);
		outStream.flush();
		outStream.close();
		
		// Get response
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder builder = new StringBuilder();
		int cp;
		while ((cp = reader.read()) != -1) {
			builder.append((char) cp);
		}
		inStream.close();
		
		String jsonText = builder.toString();
				
		VCResponseData artifact = new Gson().fromJson(jsonText, VCResponseData.class);
		hashmap.put("repo", artifact.getCurrent().getRepo());
		hashmap.put("branch", artifact.getCurrent().getBranch());
		hashmap.put("commit", artifact.getCurrent().getCommit());
		
		return hashmap;
	}
}
