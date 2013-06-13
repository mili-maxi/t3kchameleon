/**
 * 
 * Route and processor
 * To be used together with t3kchameleon
 * 
 */

public class t3kchrap {

	String from = "";
	String to = "";
	String process = "";
	
	/**
	 * Constructor
	 * 
	 * @param from route from part
	 * @param processClass the dynamically loaded java processor class
	 * @param to route to part
	 */
	t3kchrap(String from, String processClass, String to) {
		this.from = from;
		this.to = to;
		this.process = processClass;
	}

	/**
	 * Get the from part
	 * 
	 * @return the URI from
	 */
	String getFrom() {
		return from;
	}
	
	/**
	 * Get the to part
	 * 
	 * @return the URI to
	 */
	String getTo() {
		return to;
	}
	
	/**
	 * Get the plugin processor class
	 * 
	 * @return the Java classname
	 */
	String getProcessor() {
		return process;
	}
	
}
