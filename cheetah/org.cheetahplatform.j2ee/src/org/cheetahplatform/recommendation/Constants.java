/**
 * 
 */
package org.cheetahplatform.recommendation;

/**
 * This class contains constants needed by the recommendation engine.
 * 
 * @author Christian Haisjackl
 */
public class Constants {
	/** For the trace filter, attribute has to be not equal then filtered value */
	public static final int FILTER_NE = -5;
	/** For the trace filter, attribute value has to be lesser then filtered value */
	public static final int FILTER_LT = -2;
	/** For the trace filter, attribute value has to be lesser or equal then filtered value */
	public static final int FILTER_LE = -1;
	/** For the trace filter, attribute has to be equal then filtered value */
	public static final int FILTER_EQ = 0;
	/** For the trace filter, attribute value has to be greater or equal then filtered value */
	public static final int FILTER_GE = 1;
	/** For the trace filter, attribute value has to be greater then filtered value */
	public static final int FILTER_GT = 2;
}
