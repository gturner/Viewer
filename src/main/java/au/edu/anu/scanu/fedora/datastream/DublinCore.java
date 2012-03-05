package au.edu.anu.scanu.fedora.datastream;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 */

/**
 * @author Rahul Khanna
 * 
 */
public final class DublinCore
{
	// Private constants
	public static final String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";

	// Public constants for use in methods of this class as arguments.
	public static final String ELEMENT_TITLE = "title";
	public static final String ELEMENT_CREATOR = "creator";
	public static final String ELEMENT_SUBJECT = "subject";
	public static final String ELEMENT_DESCRIPTION = "description";
	public static final String ELEMENT_PUBLISHER = "publisher";
	public static final String ELEMENT_CONTRIBUTOR = "contributor";
	public static final String ELEMENT_DATE = "date";
	public static final String ELEMENT_TYPE = "type";
	public static final String ELEMENT_FORMAT = "format";
	public static final String ELEMENT_IDENTIFIER = "identifier";
	public static final String ELEMENT_SOURCE = "source";
	public static final String ELEMENT_LANGUAGE = "language";
	public static final String ELEMENT_RELATION = "relation";
	public static final String ELEMENT_COVERAGE = "coverage";
	public static final String ELEMENT_RIGHTS = "rights";
	public static final String[] DC_ELEMENTS =
	{ ELEMENT_TITLE, ELEMENT_CREATOR, ELEMENT_SUBJECT, ELEMENT_DESCRIPTION, ELEMENT_PUBLISHER, ELEMENT_CONTRIBUTOR, ELEMENT_DATE, ELEMENT_TYPE, ELEMENT_FORMAT,
			ELEMENT_IDENTIFIER, ELEMENT_SOURCE, ELEMENT_LANGUAGE, ELEMENT_RELATION, ELEMENT_COVERAGE, ELEMENT_RIGHTS };

	private Document dcDoc;

	public DublinCore() throws ParserConfigurationException
	{
		// TODO: This constructor isn't working.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		dcDoc = factory.newDocumentBuilder().newDocument();
		Element dcElement = dcDoc.createElementNS("http://www.openarchives.org/OAI/2.0/oai_dc/", "dc");
	}

	public DublinCore(InputStream inStream) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		dcDoc = factory.newDocumentBuilder().parse(inStream);
	}

	public DublinCore(Document inDoc)
	{
		dcDoc = inDoc;
	}

	public DublinCore(String inString) throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		dcDoc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(inString)));
	}

	/**
	 * Gets the value of the Dublin Core element.
	 * 
	 * @param elementName
	 *            The Dublin Core element to get. The following are valid:
	 *            <table>
	 *            <tr>
	 *            <td>ELEMENT_TITLE</td>
	 *            <td>ELEMENT_CREATOR</td>
	 *            <td>ELEMENT_SUBJECT</td>
	 *            <td>ELEMENT_DESCRIPTION</td>
	 *            <td>ELEMENT_PUBLISHER</td>
	 *            <td>ELEMENT_CONTRIBUTOR</td>
	 *            <td>ELEMENT_DATE</td>
	 *            <td>ELEMENT_TYPE</td>
	 *            <td>ELEMENT_FORMAT</td>
	 *            <td>ELEMENT_IDENTIFIER</td>
	 *            <td>ELEMENT_SOURCE</td>
	 *            <td>ELEMENT_LANGUAGE</td>
	 *            <td>ELEMENT_RELATION</td>
	 *            <td>ELEMENT_COVERAGE</td>
	 *            <td>ELEMENT_RIGHTS</td>
	 *            </tr>
	 *            </table>
	 * 
	 * @return Returns the value of the Dublin Core element or null if the element doesn't exist in the DC doc.
	 */
	public String getElement(String elementName)
	{
		NodeList elements = dcDoc.getElementsByTagNameNS(NAMESPACE_DC, elementName);
		return elements.getLength() == 0 ? null : elements.item(0).getTextContent();
	}

	public void setElement(String elementName, String newTextContent)
	{
		NodeList elements = dcDoc.getElementsByTagNameNS(NAMESPACE_DC, elementName);

		// If no element by that name doesn't exist, create it, add text content, and add it to the document. Else, update the text content.
		if (elements.getLength() == 0)
		{
			Element createdElement = dcDoc.createElementNS(NAMESPACE_DC, elementName);
			createdElement.setPrefix("dc");
			createdElement.setTextContent(newTextContent);
			dcDoc.getFirstChild().appendChild(createdElement);
		}
		else
		{
			elements.item(0).setTextContent(newTextContent);
		}
	}

	/**
	 * Returns the DC XML stream as String.
	 */
	public String toString()
	{
		StringWriter xml = new StringWriter();

		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(dcDoc);

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.transform(source, new StreamResult(xml));
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xml.toString();
	}
}
