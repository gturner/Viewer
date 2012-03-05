package au.edu.anu.viewer.xml.dc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DCConstants {
	public static final String DC = "http://purl.org/dc/elements/1.1/";
	public static final String OAI_DC = "http://www.openarchives.org/OAI/2.0/oai_dc/";
	public static final String XSI = "http://www.w3.org/2001/XMLSchema-instance";
	
	public static final Map<String, String> FIELD_TO_ELEMENTS;
	static{
		Map<String, String> aMap = new HashMap<String, String>();
		
		aMap.put("name", "title");
		aMap.put("kind", "type");
		aMap.put("briefDesc", "description");
		aMap.put("fullDesc", "description");
		
		FIELD_TO_ELEMENTS = Collections.unmodifiableMap(aMap);
	}
}
