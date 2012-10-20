package mvn.p2.vt.mojo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Call the Version Checker (with a default "add" operation for now)
 * @goal callVC
 */
public class CheckVersion extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
    	int rand = (int)(100*Math.random());
        getLog().info("Adding rows to VC with random value: " + rand);
        // Run a version checker in a separate process
        Process proc;
		try {
			// execute the process with preset arguments (for now)
			proc = Runtime.getRuntime().exec("java -jar VersionChecker.jar add -repo test1:" + rand + " -p2v test2:" + rand + " -cmt test3:" + rand);
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
