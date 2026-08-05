//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file TemperatureSosLookUpTable.java
*    @author marcos
*    @date 11 de out de 2016
*    @details <Detailed Description>
* 
*/
//=============================================================================
package util;

import java.util.HashMap;

/**
 * @author marcos
 *
 */
public class TemperatureSosLookUpTable {
	
	public final static HashMap<Double, Double> lookUpTable = new HashMap<>();
		
		static
		{
			lookUpTable.put(0.00000000000000e+000, 1.40238744000000e+003);
			lookUpTable.put(1.00000000000000e+001, 1.44727957965388e+003);
			lookUpTable.put(2.00000000000000e+001, 1.48235829856432e+003);
			lookUpTable.put(2.50000000000000e+001, 1.49670444077783e+003);
			lookUpTable.put(3.00000000000000e+001, 1.50914502152605e+003);
			lookUpTable.put(3.50000000000000e+001, 1.51982681204079e+003);
			lookUpTable.put(4.00000000000000e+001, 1.52888147289024e+003);
			lookUpTable.put(5.00000000000000e+001, 1.54256760639062e+003);
			lookUpTable.put(6.00000000000000e+001, 1.55099953496976e+003);
			lookUpTable.put(7.00000000000000e+001, 1.55480746060519e+003);
			lookUpTable.put(7.40000000000000e+001,  1.55515184104861e+003);
		
		}
}
