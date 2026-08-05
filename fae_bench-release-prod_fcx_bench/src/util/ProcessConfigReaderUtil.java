//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file PropertiesReaderUtil.java
*    @author Marcos Oliveira
*    @date 12 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Marcos Oliveira
 *
 */
public class ProcessConfigReaderUtil {
	
	/**
	 * Reads the socketConfig.xml to acquire the Ip and Port for connection
	 */
	public static HashMap<String, String> readConfigXMLStart(){
		HashMap<String , String> hash = new HashMap<>();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File("./processConfig.xml"));
			doc.getDocumentElement().normalize ();
			
			Element node = doc.getElementById("purge");
			hash.put("purgeTime", node.getElementsByTagName("time").item(0).getTextContent());
			hash.put("purgeFlowValue", node.getElementsByTagName("flow").item(0).getTextContent());
			
			node = doc.getElementById("flow");
			hash.put("flowTime", node.getElementsByTagName("time").item(0).getTextContent());
			hash.put("deviation", node.getElementsByTagName("flow").item(0).getTextContent());
			
			NodeList flows = doc.getElementsByTagName("flowvalue");
			
			for (int temp = 0; temp < flows.getLength(); temp++) {
		            Node nNode = flows.item(temp);
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            		Element eElement = (Element) nNode;
	            		hash.put("flowValue"+temp, eElement.getElementsByTagName("flowvalue").item(0).getTextContent());
	            	}
			 }
			
		}	catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hash;
	}
	
}
