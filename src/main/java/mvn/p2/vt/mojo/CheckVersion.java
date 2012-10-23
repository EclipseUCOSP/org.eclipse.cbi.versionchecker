package mvn.p2.vt.mojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

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
			proc = Runtime.getRuntime().exec("java -jar VersionChecker.jar " + query);
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
		} catch (Exception e) {
			e.printStackTrace();
		}		
    }


}
