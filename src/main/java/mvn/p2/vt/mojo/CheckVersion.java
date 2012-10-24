package mvn.p2.vt.mojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import mavenp2versionmatch.main.MvnP2Util; // import the MvnP2Util (Version Checker). Maven includes it in our classpath so this should be all gravy

/**
 * Call the Version Checker (with a default "add" operation for now)
 * @goal callVC
 */
public class CheckVersion extends AbstractMojo
{
	/**
     * The query to send the version checker.
     *
     * @parameter expression="${callVC.query}""
     */
    private String query;
	
    public void execute() throws MojoExecutionException
    {
    	if (query == null || query.isEmpty()) {
    		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
    		getLog().info("Please enter your query:");
    		try {
				query = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
        System.out.println("query: "+ query);
        getLog().info("Executing the following query: " + query);
        // Run a version checker in a separate process
        Process proc;
		try {
			// execute the process with preset arguments (for now)
			String[] params = {"TEST COMMAND PLEASE IGNORE"}; // set up commands to pass to the MvnP2Util. you'll probably want to do this some differenty way.
			mavenp2versionmatch.main.MvnP2Util.main(params); // call the MvnP2Util with given commands. This just starts the main method, set up your array of strings accordingly.

			/* Shouldn't need these anymore. I'm not totally sure so I've just commented them out -Mike
			proc = Runtime.getRuntime().exec("java -jar MvnP2Util-0.1-SNAPSHOT.jar " + query);
	        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	        BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	        // wait for the process to terminate
	        proc.waitFor();
	        // print the output of the process
	        getLog().info("Version Checker output:");
	        while (in.ready()){
	        	getLog().info(in.readLine());
	        }
	        while (err.ready()){
	        	getLog().info(err.readLine());
	        }
	        */
		} catch (Exception e) {
			e.printStackTrace();
		}		
    }


}
