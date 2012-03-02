package au.edu.anu.viewer.xml.dc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="dc", namespace=DCConstants.OAI_DC)
public class DublinCore {
	List<JAXBElement<String>> items;
	
	public DublinCore(){
		items = new ArrayList<JAXBElement<String>>();
	}
	
	@XmlAnyElement
	public List<JAXBElement<String>> getItems(){
		return items;
	}
	
	public void setItems(List<JAXBElement<String>> items){
		this.items = items;
	}
}
