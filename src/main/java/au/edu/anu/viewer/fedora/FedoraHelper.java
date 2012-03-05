package au.edu.anu.viewer.fedora;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.request.AddDatastream;
import com.yourmediashelf.fedora.client.request.AddRelationship;
import com.yourmediashelf.fedora.client.request.FindObjects;
import com.yourmediashelf.fedora.client.request.GetDatastreamDissemination;
import com.yourmediashelf.fedora.client.request.GetObjectXML;
import com.yourmediashelf.fedora.client.request.Ingest;
import com.yourmediashelf.fedora.client.request.ModifyDatastream;
import com.yourmediashelf.fedora.client.request.PurgeDatastream;
import com.yourmediashelf.fedora.client.response.AddDatastreamResponse;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.FindObjectsResponse;
import com.yourmediashelf.fedora.client.response.IngestResponse;
import com.yourmediashelf.fedora.client.response.ModifyDatastreamResponse;
import com.yourmediashelf.fedora.client.response.PurgeDatastreamResponse;

public class FedoraHelper {
	final Logger log = LoggerFactory.getLogger(FedoraHelper.class);
	FedoraClient fc;
	
	public FedoraHelper(){
		fc = new FedoraClient(new FedoraCredentialsImpl().getFedoraCredentials());
	}
	
	public void saveNewObject(String templateID, String sourceXMLDoc, List<FedoraReference> references, String dc){
		log.info("In submit document");
		
		String pid = "";
		//TODO fix webserver path
		String template = "http://localhost:8380/fedora/objects/" + templateID + "/datastreams/XML_SOURCE/content";
		//String template = "fedora:info/" + templateID;
		try{
			//create a new object in fedora
			IngestResponse ingestResponse = new Ingest().namespace("test").execute(fc);
		
			pid = ingestResponse.getPid();
			if(!pid.equals("")){
				//Add the xml source
				AddDatastreamResponse sourceResponse = new AddDatastream(pid, "XML_SOURCE").controlGroup("X").dsLabel("XML Document").content(sourceXMLDoc).mimeType(MediaType.TEXT_XML).execute(fc);
				
				//Add the template to the objects
				AddDatastreamResponse templateResponse = new AddDatastream(pid, "XML_TEMPLATE").controlGroup("M").dsLabel("XML Template").dsLocation(template).mimeType("text/xml").execute(fc);
				
				addRelationships(pid, references);
				addDublinCore(pid, dc);
			}
		}catch(FedoraClientException e){
			log.error(e.toString());
		}
	}
	
	private void addRelationships(String pid, List<FedoraReference> references)
			throws FedoraClientException{
		log.info("PID: " + pid);
		references.add(new FedoraReference("info:fedora/fedora-system:def/model#hasModel", "info:fedora/def:DCDataContentModel", false));
		references.add(new FedoraReference("http://www.openarchives.org/OAI/2.0/itemID", "oai:" + pid, false));
		
		PurgeDatastreamResponse purgeDataResponse = new PurgeDatastream(pid,"RELS-EXT").execute(fc);
		
		for(int i = 0; i < references.size(); i++){
			FedoraReference reference = references.get(i);
			FedoraResponse relResponse = new AddRelationship(pid).predicate(reference.getPredicate_()).object(reference.getObject_()).isLiteral(reference.isLiteral_()).execute(fc);
		}
	}
	
	private void addDublinCore(String pid, String dc) throws FedoraClientException {
		ModifyDatastreamResponse sourceResponse = new ModifyDatastream(pid, "DC").content(dc).execute(fc);
	}

	public void saveExistingObject(String pid, String sourceXMLDoc, List<FedoraReference> references, String dc){
		try{
			ModifyDatastreamResponse sourceResponse = new ModifyDatastream(pid, "XML_SOURCE").dsLabel("XML Document").content(sourceXMLDoc).execute(fc);
			
			addRelationships(pid, references);
			
			addDublinCore(pid, dc);
		}catch(FedoraClientException e){
			log.error(e.toString());
		}
	}
	
	public Document getXMLDatastreamObject(String pid, String dsId){
		Document doc = null;
		try{
			FedoraResponse sourceResponse = new GetDatastreamDissemination(pid, dsId).execute(fc);
			doc = sourceResponse.getEntity(Document.class);
		}catch(FedoraClientException e){
			log.error(e.toString());
		}
		
		return doc;
	}
	
	public InputStream getDatastreamAsStream(String pid, String dsId){
		InputStream is = null;
		try{
			FedoraResponse sourceResponse = new GetDatastreamDissemination(pid, dsId).execute(fc);
			is = sourceResponse.getEntityInputStream();
		}catch(FedoraClientException e){
			log.error(e.toString());
		}
		return is;
	}
}
