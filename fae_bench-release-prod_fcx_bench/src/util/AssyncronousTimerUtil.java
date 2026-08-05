//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file AssyncronousTimerUtil.java
*    @author marcos
*    @date 11 de dez de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author marcos
 *
 */
public class AssyncronousTimerUtil  {
	
	
	private ExecutorService threadpool = Executors.newFixedThreadPool(1);
	
	private Future<?> future;
	
	TimeOutCounterUtil timeOutCounter;

	public AssyncronousTimerUtil(long timeout, String name){
		timeOutCounter = new TimeOutCounterUtil(timeout, name);
	}
	
	public void startCounter(){
		 future = threadpool.submit(timeOutCounter);
	}
	
	public boolean isDone(){
		return future.isDone();
	}
	
	public boolean kill(){
		threadpool.shutdown();
		return this.future.cancel(true);
	}
	
	public boolean isCancelled(){
		return this.future.isCancelled();
	}
}