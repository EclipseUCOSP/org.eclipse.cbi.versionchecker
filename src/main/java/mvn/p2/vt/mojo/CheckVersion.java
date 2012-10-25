package mvn.p2.vt.mojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Calls the Version Checker, prompting the user to enter a query to run.
 * The given query is then passed as arguments to the main method in MvnP2Util,
 * which executes the query (valid syntax specified in the 'db' README).
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
    		// prompt the user to enter a query
    		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
    		getLog().info("Please enter your query:");
    		try {
				query = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	String[] query_arr = query.split(" ");
        getLog().info("Executing the following  query: " + query);
		try {
			// call the MvnP2Util with the given commands. This just starts the main method, set up your array of strings accordingly.
			mavenp2versionmatch.main.MvnP2Util.main(query_arr); 
		} catch (Exception e) {
			e.printStackTrace();
		}		
    }


}
