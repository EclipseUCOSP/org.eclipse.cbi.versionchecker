package org.eclipse.cbi.versionchecker.ui.actions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VCPostRequest {
	public List<VCResponseData> getLatestRepo(String name) throws IOException {
		String urlParameters =  "[{\"component\": \"" + name + "\"}]";
		return sendPostRequest(urlParameters);
	}
	
	public List<VCResponseData> getCurrentRepo(String name, String version) throws IOException {
		String urlParameters =  "[{\"component\": \"" + name + "\", \"version\": \"" + version + "\"}]";;
		return sendPostRequest(urlParameters);
	}
	
	private List<VCResponseData> sendPostRequest(String urlParameters) throws IOException {
		Properties prop = new Properties();
		prop.load(this.getClass().getResourceAsStream("/config.properties"));

		URL url = new URL(prop.getProperty("webserviceurl"));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		// Implicitly set the request method to POST
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		// Create I/O streams
		DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		// Send request
		outStream.writeBytes(urlParameters);
		outStream.flush();
		outStream.close();
		
		DataInputStream inStream = new DataInputStream(connection.getInputStream());
		// Get response
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		StringBuilder builder = new StringBuilder();
		int cp;
		while ((cp = reader.read()) != -1) {
			builder.append((char) cp);
		}
		inStream.close();
		
		String jsonText = builder.toString();
		
		Type artifactListType = new TypeToken<List<VCResponseData>>(){}.getType();
		List<VCResponseData> artifacts = new Gson().fromJson(jsonText, artifactListType);
		
		return artifacts;
	}
}
