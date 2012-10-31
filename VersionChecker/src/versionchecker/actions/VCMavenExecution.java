package versionchecker.actions;

import java.io.*;

import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * VCMavenExecution class is for maven command line execution.
 */
public class VCMavenExecution {
	
	// Run command will run the given Maven command and return the output.
	public static String RunCommand(String c) {
		Process p = null;
		String s = null;
		try {
			p = Runtime.getRuntime().exec(c);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
