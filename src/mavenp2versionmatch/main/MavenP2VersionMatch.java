package mavenp2versionmatch.main;

import java.util.HashMap;
import java.util.Map;

import mavenp2versionmatch.db.MavenP2Col;

public class MavenP2VersionMatch {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		
		for (int i = 0; i< args.length; i++) {
			String key = args[i];
			//substring for dash in commandline
			//TODO: is there a better way?
			String val = args[++i].substring(1);

			if (MavenP2Col.findByStr(key) == null) {
				System.err.println("Invalid argument");
				System.exit(-1);
			}
			else {
				map.put(key, val);
			}
		}
		
		
	}

}
