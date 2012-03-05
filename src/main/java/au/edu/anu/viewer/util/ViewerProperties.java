package au.edu.anu.viewer.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A class that provides access to the properties file 'viewer.properties'
 */
public class ViewerProperties {
	private static final Properties config;
	
	static {
		Properties fallback = new Properties();
		fallback.put("key", "default");
		config = new Properties(fallback);
		try{
			InputStream stream = ViewerProperties.class.getResourceAsStream("/viewer.properties");
			try{
				config.load(stream);
			}
			finally {
				stream.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String propertyName){
		return config.getProperty(propertyName);
	}
}
