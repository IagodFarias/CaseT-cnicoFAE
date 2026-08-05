//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SocketConfigReaderUtil.java
*    @author marcos
*    @date 11 de ago de 2016
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

import si.dbcomm.util.ConversorNumbers;

/**
 * @author marcos
 *
 */
public class SocketConfigReaderUtil {

	
	/**
	 * Reads the socketConfig.xml to acquire the Ip and Port for connection
	 */
	public static HashMap<String, String> readConfigXML(ConversorNumbers conversorNumbers){
		HashMap<String , String> hash = new HashMap<>();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File("./socketConfig.xml"));
			doc.getDocumentElement ().normalize ();
			
			NodeList socketsList = doc.getElementsByTagName("socket");
			
			for (int temp = 0; temp < socketsList.getLength(); temp++) {
		            Node nNode = socketsList.item(temp);
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            		Element eElement = (Element) nNode;
	            		if(eElement.getAttribute("socketId").equals(ConversorNumbers.getStringValue(conversorNumbers))){
	            			hash.put("IP", eElement.getElementsByTagName("ip").item(0).getTextContent());
	            			hash.put("PORT", eElement.getElementsByTagName("port").item(0).getTextContent());
	            		}
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
