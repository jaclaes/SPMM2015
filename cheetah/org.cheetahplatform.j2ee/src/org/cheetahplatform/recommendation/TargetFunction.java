package org.cheetahplatform.recommendation;



import java.util.List;

import pmsedit.logmodel.Attribute;
import pmsedit.logmodel.LogEntry;
import pmsedit.logmodel.ProcessInstance;



/**
 * This class represents the target value of a trace.
 * 
 * @author Christian Haisjackl
 */
public class TargetFunction {
	/** Getting minimum value is more important. */
	public static final boolean CALC_MIN = false;
	/** Getting maximum value is more important. */
	public static final boolean CALC_MAX = true;
	/** Decides whether the min or the max value is more important. Default is maximum. */
	private boolean calcMaximumValue = CALC_MAX;
	private String attributeName;



	/**
	 * Creates a new Target Function
	 * 
	 * @param attributeName The name of the attribute which should be as high or as few as possible.
	 * @param calcMaxValue true if the maximum should be recommended, false if the minimum should be
	 *           recommended.
	 */
	public TargetFunction( String attributeName, boolean calcMaxValue ) {
		calcMaximumValue = calcMaxValue;
		this.attributeName = attributeName;
	}



	/**
	 * This method adds a specific target value to the log entry's value.
	 * 
	 * @param entry The entry to add the value
	 * @param targetValue The additional target value
	 */
	public void addTargetValue( LogEntry entry, double targetValue ) {
		List<Attribute> attrList = entry.getData();
		for( int i = 0; i < attrList.size(); i++ ) {
			if( attrList.get( i ).getName().equals( attributeName ) ) {
				double newTargetValue = ( calcMaximumValue ) ? targetValue : -targetValue;
				double tmp = ( calcMaximumValue ) ? Double.parseDouble( attrList.get( i ).getContent() ) : -Double.parseDouble( attrList.get( i ).getContent() );
				attrList.get( i ).setContent( ( new Double( newTargetValue + tmp ) ).toString() );
			}
		}
	}



	/**
	 * This method calculates the target value of a trace.
	 * 
	 * @param log A list of log entries.
	 * @return The target value
	 */
	public double calcTargetValue( List<LogEntry> log ) {
		double targetValue = 0.0;
		for( LogEntry l : log ) {
			List<Attribute> attrList = l.getData();
			for( int i = 0; i < attrList.size(); i++ ) {
				if( attrList.get( i ).getName().equals( attributeName ) )
					targetValue += Double.parseDouble( attrList.get( i ).getContent() );
			}
		}
		return ( calcMaximumValue ) ? targetValue : -targetValue;
	}



	/**
	 * This method calculates the target value, it adds the entry's value to the old value.
	 * 
	 * @param entry The new entry in the list
	 * @param oldTargetValue The old target value
	 * @return The new target value
	 */
	public double calcTargetValue( LogEntry entry, double oldTargetValue ) {
		List<Attribute> attrList = entry.getData();
		double newTargetValue = oldTargetValue;
		for( int i = 0; i < attrList.size(); i++ ) {
			if( attrList.get( i ).getName().equals( attributeName ) )
				newTargetValue += Double.parseDouble( attrList.get( i ).getContent() );
		}
		return newTargetValue;
	}



	/**
	 * This method returns the target value process instance.
	 * 
	 * @param pi The process instance which should contain the target value.
	 * @return The global target value or NaN if there is no fitting attribute
	 */
	public double getGlobalTargetValue( ProcessInstance pi ) {
		double targetValue = Double.NaN;
		List<Attribute> attrList = pi.getData();
		for( int i = 0; i < attrList.size(); i++ ) {
			if( attrList.get( i ).getName().equals( attributeName ) ) {
				targetValue = Double.parseDouble( attrList.get( i ).getContent() );
				break;
			}
		}
		return targetValue;
	}



	/**
	 * This method sets a specific target value to the log entry.
	 * 
	 * @param entry The entry to change the value
	 * @param targetValue The new target value
	 */
	public void setTargetValue( LogEntry entry, double targetValue ) {
		double newTargetValue = ( calcMaximumValue ) ? targetValue : -targetValue;
		List<Attribute> attrList = entry.getData();
		for( int i = 0; i < attrList.size(); i++ ) {
			if( attrList.get( i ).getName().equals( attributeName ) )
				attrList.get( i ).setContent( ( new Double( newTargetValue ) ).toString() );
		}
	}



	/**
	 * @return TRUE, if the maximum value is the preferred value else false
	 */
	public boolean getCalcMaxValue() {
		return calcMaximumValue;
	}



	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return attributeName;
	}
}
