//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TimeOutCounterUtil.java
*    @author marcos
*    @date 12 de abr de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.util.concurrent.Callable;

/**
 * @author marcos
 *
 */
public class TimeOutCounterUtil implements Callable<Object> {

	private long time;
	
	private String name;
	
	/**
	 * Creates a object for class TimeOutCounterUtil.java
	 */
	public TimeOutCounterUtil(long timeOut, String name) {
		this.time = timeOut;
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@SuppressWarnings("static-access")
	@Override
	public Object call() throws Exception {
		Thread.currentThread().setName(name);
		Thread.currentThread().sleep(time);
		return true;
	}

}
