package plugin.harvester;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class harvester implements Processor {
  
	public void process(Exchange e) {
		System.out.println("Received exchange: "+ e.getIn()); 
        // TODO: Camel stuff - set some headers too?
		
		// TODO: Integration with web-harvest 2.0.
		
		
    }
	
}
