package au.edu.anu.scanu.fedora.datastream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.request.AddDatastream;
import com.yourmediashelf.fedora.client.request.ListDatastreams;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.ListDatastreamsResponse;
import com.yourmediashelf.fedora.generated.access.DatastreamType;

/**
 * 
 */

/**
 * Usage: FileUpload.process(new File("C:\\Rahul\\FileUpload\\FTP\\Links.txt.properties"));
 * This will pull the datastream info from the properties file and upload/reference the file Links.txt to it.
 *  
 * @author Rahul Khanna
 * 
 */
public class FileUpload
{
	private static final Logger log = Logger.getLogger("FileUpload");

	// Supported keys in property files.
	private static final String DATASTREAM_PROPERTY_PID = "Pid";
	private static final String DATASTREAM_PROPERTY_ID = "Id";
	private static final String DATASTREAM_PROPERTY_STATE = "State";
	private static final String DATASTREAM_PROPERTY_LABEL = "Label";
	private static final String DATASTREAM_PROPERTY_VERSIONABLE = "Versionable";
	private static final String DATASTREAM_PROPERTY_CHECKSUMTYPE = "ChecksumType";
	private static final String DATASTREAM_PROPERTY_CHECKSUM = "Checksum";
	private static final String DATASTREAM_PROPERTY_MIMETYPE = "MimeType";
	private static final String DATASTREAM_PROPERTY_CONTROLGROUP = "ControlGroup";

	public static void process(URI dsPropFileURI)
	{
		process(new File(dsPropFileURI));
	}

	public static void process(File dsPropFile)
	{
		FedoraCredentials fedoraCredentials;
		FedoraClient fedoraClient;
		Properties fedoraServerProps = new Properties();

		// Get the connection strings for the Fedora Server. If the properties file containing the string doesn't exist, load defaults.
		try
		{
			fedoraServerProps.loadFromXML(new FileInputStream("FedoraServer.properties"));
		}
		catch (IOException e)
		{
			log.warning("FedoraServer.properties not found. Loading defaults.");
			fedoraServerProps.setProperty("Server", "http://localhost:8081/fedora");
			fedoraServerProps.setProperty("Username", "fedoraAdmin");
			fedoraServerProps.setProperty("Password", "admin");
		}

		try
		{
			// Open connection to repository using credentials in .properties file.
			fedoraCredentials = new FedoraCredentials(fedoraServerProps.getProperty("Server"), fedoraServerProps.getProperty("Username"),
					fedoraServerProps.getProperty("Password"));
			fedoraClient = new FedoraClient(fedoraCredentials);

			// Read .properties file.
			Properties fileProps = new Properties();
			// fileProps.load(new FileInputStream("C:\\Rahul\\FileUpload\\FTP\\Links.txt.properties"));
			fileProps.load(new FileInputStream(dsPropFile));

			// Ensure that the minimum required properties are specified - PID and ID.
			if (fileProps.getProperty(DATASTREAM_PROPERTY_PID) == null || fileProps.getProperty(DATASTREAM_PROPERTY_ID) == null)
				throw new NullPointerException("Invalid properties file. Mandatory properties not specified.");

			// Check if a datastream with the same ID for the Pid doesn't already exist.
			ListDatastreams listDsCmd = new ListDatastreams(fileProps.getProperty(DATASTREAM_PROPERTY_PID));
			ListDatastreamsResponse listDsResp = listDsCmd.execute(fedoraClient);
			if (listDsResp.getStatus() != HttpsURLConnection.HTTP_OK)
				throw new FedoraClientException("Unable to obtain list of existing datastreams for Pid '" + fileProps.getProperty(DATASTREAM_PROPERTY_PID)
						+ "'.");

			List<DatastreamType> listDs = listDsResp.getDatastreams();
			ListIterator<DatastreamType> i = listDs.listIterator();

			while (i.hasNext())
			{
				DatastreamType curDsType = i.next();
				if (curDsType.getDsid().equals(fileProps.getProperty(DATASTREAM_PROPERTY_ID)))
				{
					log.info("Datastream " + curDsType.getDsid() + " already exists for Pid " + fileProps.getProperty(DATASTREAM_PROPERTY_PID) + ".");
					break;
				}
			}

			// Create datastream object.
			AddDatastream datastream = new AddDatastream(fileProps.getProperty(DATASTREAM_PROPERTY_PID), fileProps.getProperty(DATASTREAM_PROPERTY_ID));

			if (fileProps.getProperty(DATASTREAM_PROPERTY_STATE) != null)
				datastream = datastream.dsState(fileProps.getProperty(DATASTREAM_PROPERTY_STATE));

			if (fileProps.getProperty(DATASTREAM_PROPERTY_LABEL) != null)
				datastream = datastream.dsLabel(fileProps.getProperty(DATASTREAM_PROPERTY_LABEL));

			if (fileProps.getProperty(DATASTREAM_PROPERTY_VERSIONABLE) != null)
				datastream = datastream.versionable(Boolean.parseBoolean(fileProps.getProperty(DATASTREAM_PROPERTY_VERSIONABLE)));

			if (fileProps.getProperty(DATASTREAM_PROPERTY_CHECKSUMTYPE) != null)
				datastream = datastream.checksumType(fileProps.getProperty(DATASTREAM_PROPERTY_CHECKSUMTYPE));

			if (fileProps.getProperty(DATASTREAM_PROPERTY_CHECKSUM) != null)
				datastream = datastream.checksum(fileProps.getProperty(DATASTREAM_PROPERTY_CHECKSUM));

			if (fileProps.getProperty(DATASTREAM_PROPERTY_MIMETYPE) != null)
			{
				datastream = datastream.mimeType(fileProps.getProperty(DATASTREAM_PROPERTY_MIMETYPE));
			}
			else
			{
				// TODO: Add feature (low priority) to auto detect mime type based on file extension (fast) or file contents (slow).
			}

			if (fileProps.getProperty(DATASTREAM_PROPERTY_CONTROLGROUP) != null)
			{
				datastream = datastream.controlGroup(fileProps.getProperty(DATASTREAM_PROPERTY_CONTROLGROUP));

				if (fileProps.getProperty(DATASTREAM_PROPERTY_CONTROLGROUP).equals("M") || fileProps.getProperty(DATASTREAM_PROPERTY_CONTROLGROUP).equals("E"))
				{
					datastream = datastream.content(new File("C:\\Rahul\\FileUpload\\FTP\\Links.txt"));
					// datastream = datastream.dsLocation("http://localhost:9081/Test.txt");
				}
				else
				{
					// TODO: Add code to manage X and R controlGroups.
				}
			}

			// Add the created dataset into Fedora.
			AddDatastreamResponse addResponse = datastream.execute(fedoraClient);
			log.info("Add Datastream Response Status: " + addResponse.getStatus());
		}
		catch (FedoraClientException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
