package plugin.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class comproc implements Processor {
  
	public void process(Exchange e) {
		System.out.println("Received exchange: "+ e.getIn()); 
        // TODO: Camel stuff - set some headers too?
		
		// TODO: execute command, when parsing parameters
		// executeCommand(commandParams);
		
    }
	
	String executeCommand(String commandParams) {
		
		String stres = null;
		String res = "";

        try {
            
            Process proc = Runtime.getRuntime().exec(commandParams);
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(proc.getErrorStream()));

            // output from the command
            while ((stres = stdInput.readLine()) != null) {
                // System.out.println(stres); // debug
                res = res + '\n' + stres;
            }
            
            // standard error from the command
            while ((stres = stdError.readLine()) != null) {
                // System.out.println(stres); // debug
            }

        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
		
		return stres;
	}
	
}