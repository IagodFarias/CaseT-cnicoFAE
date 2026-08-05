//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file ComboBoxChoice.java
*    @author marcos
*    @date 24 de set de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view;

/**
 * @author marcos
 *
 */
public class ComboBoxChoice {
	private long id;
	private String name;
	
	
	/**
	 * Creates a object for class ComboBoxChoice.java
	 */
	public ComboBoxChoice(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Function returns the value of attribute id
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * Function sets the value for attribute id
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Function returns the value of attribute name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Function sets the value for attribute name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	} 
}
