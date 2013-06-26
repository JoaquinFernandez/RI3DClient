package com.jfernandez.ri3d.client;

import java.util.Properties;

/**
 * Configuration class, it opens the configuration file and has a public method to
 * retrieve the property needed
 * 
 * @author Joaquin Fernandez
 *
 */
public class Config {

	/** Property object, that holds the config file */
	Properties configFile;

	/**
	 * Constructor that opens the configuration file so it can be used later
	 */
	public Config(String filename) {

		configFile = new java.util.Properties();
		try {
			configFile.load(this.getClass().getClassLoader().
					getResourceAsStream(filename));

		} catch(Exception eta){
			eta.printStackTrace();
		}
	}

	/**
	 * Getter method that retrieves the property bounded to the key
	 * @param key of the property wanted
	 * @return the property wanted
	 */
	public String getProperty(String key) {
		String value = this.configFile.getProperty(key);
		return value;
	}
}
