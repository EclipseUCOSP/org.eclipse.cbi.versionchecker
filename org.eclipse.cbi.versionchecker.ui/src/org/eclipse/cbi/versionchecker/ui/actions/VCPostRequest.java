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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VCPostRequest {
	public String getLatestRepo(String name) throws IOException {
		URL url = new URL("http://ec2-107-21-119-69.compute-1.amazonaws.com/cakephp/");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
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
		
		Type artifactListType = new TypeToken<List<VCResponseData>>(){}.getType();
		
		List<VCResponseData> artifacts = new Gson().fromJson(jsonText, artifactListType);
		if (!artifacts.isEmpty())
			return artifacts.get(0).getCurrent().getRepo();
		else
			return "";
	}
}
