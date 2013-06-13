/**
 * Class to select just some from properties, by using a criteria
 * 
 * Made, since I repeatably required this wrapper with this functionality in numberous projects.
 * 
 *  @version 1.1.1
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
 */

/**
 * This is a class which eases searching in properties. 
 */

import java.util.Enumeration;
import java.util.Properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class t3kpropselect extends Properties {

	final static int BEGIN = 0;
	final static int END = 1;
	final static int BEGINEND = 2;
	final static int CONTAINS = 3;
	final static int REGEX = 4;

	boolean unique = true;
	
	/**
	 * Set the uniquness of returned parts
	 * @param unique
	 */
	public void setAllUniqueKeyParts (boolean unique) {
		this.unique = unique;
	}
	
	/**
	 * Get the uniquness of the returned parts
	 * 
	 * @return true or false, depending if unique or not
	 */
	public boolean getAllUniqueKeyParts() {
		return unique;
	}
	
	/**
	 * Get all properties keys and values which begin with the key
	 * @param key the key
	 * @return all the properties matching the criteria
	 */
	public Properties getAllKeyValuesByBegin(String key) {
		return getAllPropertiesByKey(key,BEGIN);	
	}
	
	/**
	 * Get all properties keys and values which end with the key 
	 * @param key the key
	 * @return all the properties matching the criteria
	 */
	public Properties getAllKeyValuesByEnd(String key) {
		return getAllPropertiesByKey(key,END);
	}

	/**
	 * Get all properties keys and values which begin and end with the key
	 * @param key the key
	 * @return all the properties matching the criteria
	 */
	public Properties getAllKeyValuesByBeginAndEnd(String beginkey, String endkey) {
		return getAllPropertiesByKey(beginkey,endkey,BEGINEND);
	}
	
	/**
	 * Get all properties keys and values which contain the key
	 * @param key the key
	 * @return all the properties matching the criteria
	 */
	public Properties getAllKeyValuesContaining(String key) {
		return getAllPropertiesByKey(key,CONTAINS);
	}
	
	/**
	 * Get all properties keys and values which match the regular expression key
	 * This is the most powerfull searching method.
	 * 	For a description of regular expressions in java please see one (or more) here:
	 *  	- http://docs.oracle.com/javase/tutorial/essential/regex/
	 *  	- http://wiki.lundogbendsen.dk/download/attachments/10223967/JavaRegExpCheatSheet.pdf
	 * 
	 * @param regexkey the key
	 * @return all the properties matching the criteria
	 */
	public Properties getAllKeyValuesWithRegex(String regexkey) {
		return getAllPropertiesByKey(regexkey,REGEX);
	}

	/**
	 * Compound function with one parameter
	 * 
	 * @param key the key
	 * @param way the kind to get the results
	 * @return results matching the criteria
	 */
	public Properties getAllPropertiesByKey(String key, int way) {
		return getAllPropertiesByKey(key, null, way);
	}
	
	/**
	 * Compound function with two parametes
	 * 
	 * @param key1 the first key
	 * @param key2 the second key (or null)
	 * @param way the kind to get the results
	 * @return results matching the criteria
	 */
	public Properties getAllPropertiesByKey(String key1, String key2, int way) {
		
		if (key1 == null) return null;
		
		Properties allKeys = new Properties();
		Enumeration e = propertyNames();

		    while (e.hasMoreElements()) {
		      String ckey = (String) e.nextElement();
		      String value = getProperty(ckey);
		      
		      if (key2 == null) {
			      if (way == BEGIN) {
			    	  if (ckey.startsWith(key1) == true)
			    		  allKeys.setProperty(ckey, value);
			      }
			      else if (way == END) {
			    	  if (ckey.endsWith(key1) == true)
			    		  allKeys.setProperty(ckey, value);
			      }
			      else if (way == CONTAINS) {
			    	  if (ckey.contains(key1) == true)
			    		  allKeys.setProperty(ckey, value);
			      }
			      else if (way == REGEX) {
			    	  if (ckey.matches(key1) == true)
			    		  allKeys.setProperty(ckey, value);
			      } else {
			    	  return null;
			      }		    	  
		      } else {
		    	  if (way == BEGINEND) {
		    		  if ((ckey.startsWith(key1) == true) && (ckey.endsWith(key2)) )
				    	  allKeys.setProperty(ckey, value);
			      } else {
			    	return null;
			      }
		      }
		      
		    }
		    
		return allKeys;
	}
	
	/**
	 * Get the part of a key which is unspecified. Usually to be used for index geting.
	 * 
	 * @param begin the beginning part of the key
	 * @param end the ending part of the key
	 * @return the moddle part of the key
	 */
	String[] getAllKeyParts(String begin, String end)  {
		
		if (begin == null) return new String[0];
		if (end == null) return new String[0];
		
		Properties allKeys = getAllKeyValuesByBeginAndEnd(begin,end);
		String[] keys = new String[0];

		if (unique == false) { keys = new String[allKeys.size()]; };
		Properties unique = new Properties();
		
		Enumeration e = allKeys.propertyNames();
		int i = 0;
		while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      if ((begin != null) && (end != null)) {		    	  
		    	  if (this.unique == true) {
		    		  unique.setProperty(key.substring(begin.length(), key.length()-end.length()), "1");
		    	  } else {
		    		  keys[i] = key.substring(begin.length(), key.length()-end.length());  
		    	  }
		      }
		      i++;
		}
		
		if (this.unique == true) {
			 keys = new String[unique.size()];
			 e = unique.propertyNames();
			 int j = 0;
			 while (e.hasMoreElements()) {
			      String key = (String) e.nextElement();
			      keys[j] = key;
			      j++;
			 }
		}
		
		return keys;
	}
	
	/**
	 * @param args
	 * 
	 * Test and debug function. Not to be used normally.
	 */
	public static void main(String[] args) {
		t3kpropselect ps = new t3kpropselect();
		ps.setProperty("test1", "value1");
		ps.setProperty("thfd1", "value1");
		ps.setProperty("nest1", "value1");
		System.out.println("test size:" + ps.getAllKeyValuesByEnd("st1") .size());
		System.out.println("middle part:" + ps.getAllKeyParts("t","1")[0]);
		System.out.println("left part:" + ps.getAllKeyParts("","1")[0]);
		System.out.println("right part:" + ps.getAllKeyParts("t","")[0]);		
	}

}
