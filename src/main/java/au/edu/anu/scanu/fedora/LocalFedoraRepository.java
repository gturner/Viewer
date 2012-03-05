/**
 * 
 */
package au.edu.anu.scanu.fedora;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import au.edu.anu.scanu.fedora.datastream.DublinCore;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.request.GetDatastreamDissemination;
import com.yourmediashelf.fedora.client.request.ModifyDatastream;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;

/**
 * @author Rahul Khanna
 * 
 */
public class LocalFedoraRepository
{
	private static final Logger log = Logger.getLogger(LocalFedoraRepository.class.getSimpleName());

	private static final Properties fedoraServerProps;
	private static FedoraCredentials fedoraCredentials = null;
	private static FedoraClient fedoraClient = null;

	static
	{
		fedoraServerProps = new Properties();

		// Read server info from .properties file.
		try
		{
			fedoraServerProps.loadFromXML(Thread.currentThread().getContextClassLoader().getResourceAsStream("FedoraServer.properties"));
		}
		catch (IOException e)
		{
			log.warning("FedoraServer.properties not found. Loading defaults.");
			fedoraServerProps.setProperty("Server", "http://localhost:8081/fedora");
			fedoraServerProps.setProperty("Username", "fedoraAdmin");
			fedoraServerProps.setProperty("Password", "admin");
		}

		// Open connection to repository using credentials in .properties file.
		try
		{
			fedoraCredentials = new FedoraCredentials(fedoraServerProps.getProperty("Server"), fedoraServerProps.getProperty("Username"),
					fedoraServerProps.getProperty("Password"));
			fedoraClient = new FedoraClient(fedoraCredentials);
		}
		catch (MalformedURLException e)
		{
			log.severe("Either the URL is incorrect, or Fedora Server isn't running.");
			e.printStackTrace();
		}
	}

	public static FedoraClient getClient() throws NullPointerException
	{
		if (fedoraClient == null)
			throw new NullPointerException();

		return fedoraClient;
	}

	/**
	 * Returns the Dublin Core datastream contents as a DublinCore Object.
	 * 
	 * @param pid
	 *            Persistent Identifier of the Fedora object whose DC datastream contents are being requested.
	 * @return Dublin Core datastream contents as DublinCore object.
	 * @throws FedoraClientException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static DublinCore getDc(String pid) throws FedoraClientException, SAXException, IOException, ParserConfigurationException
	{
		GetDatastreamDissemination getDsDissmnCmd = new GetDatastreamDissemination(pid, "DC");
		getDsDissmnCmd.download(true);
		FedoraResponse getDsDissmnResp = (FedoraResponse) getDsDissmnCmd.execute(LocalFedoraRepository.getClient());
		DublinCore dc = new DublinCore(getDsDissmnResp.getEntityInputStream());
		return dc;
	}

	/**
	 * Extracts a Dublin Core XML document from a another stream by transforming using XSL.
	 * 
	 * @param pid
	 *            Persistent Identifier of the fedora object.
	 * @param dsId
	 *            Datastream identifier of the source datastream.
	 * @return The extracted Dublin Core as Dublin Core object.
	 */
	public static DublinCore transformToDc(String pid, String dsId)
	{
		// Get the datastream from which DC stream will be created, then transform it.
		// StringWriter extractedDc = new StringWriter();
		Document extractedDcDoc = null;

		try
		{
			// Execute a datastream dissemination request.
			GetDatastreamDissemination getDsDissmnCmd = new GetDatastreamDissemination(pid, dsId);
			getDsDissmnCmd = getDsDissmnCmd.download(true);
			FedoraResponse getDsDissmnResp = (FedoraResponse) getDsDissmnCmd.execute(LocalFedoraRepository.getClient());

			// Transform the response into DC XML.
			TransformerFactory tFactory = TransformerFactory.newInstance();
			// TODO: Source needs to be selected based on the datastream's root element's namespace (?).
			Source xsl = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("RegistryObjectToDC.xsl"));
			Templates template = tFactory.newTemplates(xsl);
			Transformer transformer = template.newTransformer();
			// transformer.transform(new StreamSource(getDsDissmnResp.getEntityInputStream()), new StreamResult(extractedDc));
			extractedDcDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			transformer.transform(new StreamSource(getDsDissmnResp.getEntityInputStream()), new DOMResult(extractedDcDoc));

		}
		catch (NullPointerException | FedoraClientException | TransformerException | ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create a DublinCore object from the DC stream, make sure it has an identifier, return it.
		DublinCore dc = new DublinCore(extractedDcDoc);

		if (dc.getElement(DublinCore.ELEMENT_IDENTIFIER) == null)
			dc.setElement(DublinCore.ELEMENT_IDENTIFIER, pid);

		return dc;
	}

	public static void saveDc(String pid, DublinCore dc)
	{
		ModifyDatastream modifyDs = new ModifyDatastream(pid, "DC");
		modifyDs = modifyDs.content(dc.toString());
		ModifyDatastreamResponse modDsResp;

		try
		{
			modDsResp = modifyDs.execute(fedoraClient);
			log.info("Modify datastream HTTP Status: " + modDsResp.getStatus());
		}
		catch (FedoraClientException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
