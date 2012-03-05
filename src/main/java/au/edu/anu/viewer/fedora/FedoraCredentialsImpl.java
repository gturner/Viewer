package au.edu.anu.viewer.fedora;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.edu.anu.viewer.util.ViewerProperties;

import com.yourmediashelf.fedora.client.FedoraCredentials;

/**
 * A class that creates fedora credentials with information from saved properties
 */
public class FedoraCredentialsImpl {
	final Logger log = LoggerFactory.getLogger("FedoraCredentials");
	FedoraCredentials fedoraCredentials;

	public FedoraCredentialsImpl(){
		fedoraCredentials = null;
		
		try{
			URL baseUrl = new URL(ViewerProperties.getProperty("fcbaseuri"));
			String username = ViewerProperties.getProperty("fcuser");
			String password = ViewerProperties.getProperty("fcpassword");
			fedoraCredentials = new FedoraCredentials(baseUrl, username, password);
		}catch(IOException e){
			log.error("Error reading properties file: " + e.toString());
		}
	}
	
	/**
	 * Get the credentials
	 * @return The saved credentials
	 */
	public FedoraCredentials getFedoraCredentials(){
		return fedoraCredentials;
	}
	
	/**
	 * Set the credentials
	 * @param fedoraCredentials The credentials used when connecting to other systems
	 */
	public void setFedoraCredentials(FedoraCredentials fedoraCredentials){
		this.fedoraCredentials = fedoraCredentials;
	}
}
