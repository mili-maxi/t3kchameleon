/**
 * 
 *  @version 0.1 alpha (proof-of-concept)
 *	@author Alen Milincevic
 *
 *	@section LICENSE
 *
 *	Copyright 2013 Alen Milincevic
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *
 *	Other included components may be under other licenses by their respective owners.
 *
 */

/**
 *  Main class for automation
 *  - using Apache Camel as underlying engine (http://camel.apache.org/)
 *  - using a simple plugin processor, based on Apache Camel engine (Processor)
 *  - planning: plugin to integrate web-harvest V2.0 (http://web-harvest.sourceforge.net/)
 *  - planning: servlet and applet support, better integration support etc.
 *  
 * t3k chameleon, could be rad as Trick-chameleon, as it an change, depending on parameters.
 * 
 */

import java.io.IOException;
import java.util.Date;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class t3kchameleon {

	static t3kchconfig conf = null;
	static String[] clargs = new String[0];
	
	/**
	 * The main starting class
	 * 
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		
		Date now = new Date();
		
		Logger logger = LoggerFactory.getLogger(t3kchameleon.class);
		
		clargs = args;
		CamelContext context = new DefaultCamelContext();

		try {
			context.addRoutes (new RouteBuilder() {

				Logger rblog = LoggerFactory.getLogger(RouteBuilder.class);
				
				Processor getChameleonDynamicProcessorClass(String classname) {
					   System.out.println("dynamic class:"+classname); // DEBUG					   
					   Processor p = null;
					   try {
				            Class exampleClass = Class.forName(classname);
				            Object ob = exampleClass.newInstance();
				            p = (Processor)ob;
				            rblog.debug("Inited processor class:"+classname);
				        } catch (ClassNotFoundException e) {
				            e.printStackTrace();
				            return null;
				        }
				        catch (InstantiationException e) {
				            e.printStackTrace();
				            return null;
				        } catch (IllegalAccessException e) {
				            e.printStackTrace();
				            return null;
				        }
					   return p;
				}
				
				public void configure() {

					String streamInitName = "C:\\Users\\Milijun\\git\\t3kchameleon\\t3kchameleon\\src\\config.properties";
					if (System.getProperty("t3kchameleon.configfile") != null) {
						streamInitName = System.getProperty("t3kchameleon.configfile");
					}
					if (clargs.length > 0) {
						streamInitName = clargs[0];
					}
					if (conf == null) {
						conf = new t3kchconfig();
						boolean inited = conf.init(streamInitName);
						if (inited == false) {
							System.out.println("Error loading config:"+streamInitName);
							rblog.error("Aborting. Error loading config:"+streamInitName);
							System.exit(1);
						}
					}
					
					int routecount = conf.getAllRouteAndProcessorIDs().length;
					
					for (int i=0;i<routecount;i++) {
						String routename = conf.getAllRouteAndProcessorIDs()[i];
						t3kchrap rp = conf.getRouteAndProcessor(routename);
						
						rblog.debug(" Route name: "+routename);
						
						if ((rp.getFrom() != null) && (rp.getProcessor() != null) && (rp.getTo() != null)) {														
							System.out.println("variant1 route chosen.");// DEBUG
							rblog.debug("parameters: from="+rp.getFrom()+";to="+rp.getTo()+";processor="+rp.getProcessor());
							Processor p = getChameleonDynamicProcessorClass(rp.getProcessor());							
							from(rp.getFrom()).process(p).to(rp.getTo());
						}
						else if  ((rp.getFrom() != null) && (rp.getTo() != null)) {
							System.out.println("variant2 route chosen");// DEBUG
							rblog.debug("parameters: from="+rp.getFrom()+";to="+rp.getTo());
							from(rp.getFrom()).to(rp.getTo());
						}
						else if ((rp.getFrom() != null) && (rp.getProcessor() != null)) {							
							System.out.println("variant3 route chosen");// DEBUG
							rblog.debug("parameters: from="+rp.getFrom()+";processor="+rp.getProcessor());
							Processor p = getChameleonDynamicProcessorClass(rp.getProcessor());							
							from(rp.getFrom()).process(p);
						}
						else {
							System.out.println("invalid params for { "+i+"} : "+routename);
							System.out.println("Check config. Will not continue.");
							System.exit(1);
						}
												
					}

				}
			});
		} catch (Exception e) {
			System.out.println("Context Exception: "+ e.getStackTrace());
			logger.error(e.getStackTrace().toString());
			System.exit(1);
		}
		
		try {
		//context.start();
		Date started = new Date();
		Long starttime = new Long(started.getTime()-now.getTime());
		System.out.println("Started in "+ starttime +" msec. Sleeping...");
		logger.info("Started t3kchameleon in" + starttime + " msec.");
		Thread.sleep(Integer.parseInt(conf.getAllProperties().getProperty("t3kchameleon.timeout")));
		context.stop();
		Date stopped = new Date();
		Long stoptime = new Long(stopped.getTime()-started.getTime());
		System.out.println("Finished in " + stoptime + " msec.");
		logger.info("Stoped t3kchameleon after " + stoptime + " msec.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getStackTrace().toString());
		}
	
	}

}
