//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file SetEnableBuffer.java
*    @author marcos
*    @date 14 de nov de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package bciapi.command.model;

import bciapi.command.parent.CommandParent;
import util.PropertiesReaderUtil;

/**
 * @author marcos
 *
 */
public class SetEnableBuffer extends CommandParent{
	
	private int bufferEnable;
	
	private long windowTime;
	
	private int bufferSize; 
	
	/**
	 * 
	 * Creates a object for class SetFlowRate.java
	 */
	public SetEnableBuffer(){
		super.command = PropertiesReaderUtil.getProperty("field.setenablebuffer.val");
	}

	
	/**
	 * Function returns the value of attribute bufferEnable
	 * @return the bufferEnable
	 */
	public int getBufferEnable() {
		return bufferEnable;
	}


	/**
	 * Function sets the value for attribute bufferEnable
	 * @param bufferEnable the bufferEnable to set
	 */
	public void setBufferEnable(int bufferEnable) {
		this.bufferEnable = bufferEnable;
	}


	/**
	 * Function returns the value of attribute windowTime
	 * @return the windowTime
	 */
	public long getWindowTime() {
		return windowTime;
	}

	/**
	 * Function sets the value for attribute windowTime
	 * @param windowTime the windowTime to set
	 */
	public void setWindowTime(long windowTime) {
		this.windowTime = windowTime;
	}


	/**
	 * Function returns the value of attribute bufferSize
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}


	/**
	 * Function sets the value for attribute bufferSize
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	
}
