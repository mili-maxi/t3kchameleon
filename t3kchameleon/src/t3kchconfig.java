import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

/**
 *  Configuration class to be used with t3kchameleon
 *  
 * @author Alen Milincevic
 *
 */

public class t3kchconfig {

	t3kpropselect routes = new t3kpropselect();
	
	/**
	 * Get properties from stream.
	 * Can be a classic properties or Java XML properties format. 
	 * 
	 * @param filename the name of the file stream
	 * @return success or failure
	 */
	public boolean init(String filename) {
		 try {
		        routes.loadFromXML(new FileInputStream(filename));
		 } catch (IOException e) {
				try {
			        routes.load(new FileInputStream(filename));
			 } catch (IOException e2) {
				 e2.printStackTrace();
				 return false;
			 }		
		 }
		return true;
	}
	
	/**
	 * Retrieve the route (and optionally processor) from configuration
	 * 
	 * @param name the name of the route
	 * @return the reference to the route
	 */
	public t3kchrap getRouteAndProcessor(String name) {
		t3kchrap rp = new t3kchrap(
				routes.getProperty("t3kchameleon.route."+name+".from"),
				routes.getProperty("t3kchameleon.route."+name+".processor"),
				routes.getProperty("t3kchameleon.route."+name+".to")
				);
		return rp;
	}
	
	/**
	 * Get all the IDs for routes
	 * 
	 * @return an array of IDs
	 */
	public String[] getAllRouteAndProcessorIDs() {
		return routes.getAllKeyParts("t3kchameleon.route.", ".from");
	}
	
	/**
	 * Get all properties from configuration
	 * 
	 * @return reference to properties
	 */
	public Properties getAllProperties() {
		return routes;
	}

}
