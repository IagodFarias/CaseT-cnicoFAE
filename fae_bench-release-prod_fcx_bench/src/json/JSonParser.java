//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file JSonParser.java
*    @author Marcos Oliveira
*    @date 12 de mai de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package json;


import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bciapi.command.parent.CommandParent;

/**
 * @author Marcos Oliveira
 *
 */
public class JSonParser {
	
	private ObjectMapper objMapper = new ObjectMapper();
	
	/**
	 * Parses the received command to json
	 */
	public String parse(final CommandParent command){
		String retorno = "";
		try {
			String jsonInString = objMapper.writeValueAsString(command);
//			System.out.println(jsonInString);
			retorno = jsonInString;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	/**
	 * Parses an string received to a object 
	 * @param data
	 * @return
	 */
	public <T extends CommandParent> T toObject(String data, Class<T> type){
		T retorno = null;
		if(data != null){
			try {
				retorno = objMapper.readValue(data, type);
			} catch (IOException e) {
				System.err.println("ERRO: IOException JSonParser.toObject() for data = "+data+". Message = "+e.getMessage());
//			e.printStackTrace();
			}
		}
		return retorno;
	}

	public String getCommandName(String json){
		String retorno = null;
		if(json != null){
			if(!json.isEmpty()){
				try{
					JSONObject jsonObj = new JSONObject(json);
					retorno = jsonObj.get("command").toString();
				}catch(IllegalArgumentException iex){
					iex.printStackTrace();
					System.err.println("ERROR: IllegalArgumentException in JSonParser.getCommandName()");
				} catch (JSONException e) {
					System.err.println("ERROR: Command name not found in JSonParser.getCommandName()");
//					e.printStackTrace();
				}
			} 
			
		}
		return retorno;
	}
}
