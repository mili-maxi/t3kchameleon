package plugin.dummy;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class dummy implements Processor {
  
	public void process(Exchange e) {
		System.out.println("Received exchange: "+ e.getIn()); 
        // TODO: Camel stuff - set some headers too?
		
    }
	
}