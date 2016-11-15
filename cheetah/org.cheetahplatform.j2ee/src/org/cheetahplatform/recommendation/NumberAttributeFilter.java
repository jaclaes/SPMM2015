package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.Attribute;
import pmsedit.logmodel.LogEntry;

/**
 * This filter removes every activity not having a specific numeric value.
 * 
 * @author Christian Haisjackl
 */
public class NumberAttributeFilter extends Filter {
	/**
	 * Creates a new Number Attribute Filter.
	 * 
	 * @param attribute
	 *            The attribute to search for
	 */
	protected NumberAttributeFilter(String attribute) {
		super(attribute);
	}

	/**
	 * The new trace only contains activities which are lying below, between or above specific numeric bounds.
	 * 
	 * @see org.cheetahplatform.recommendation.Filter#filter(org.cheetahplatform.recommendation.Trace, String, int)
	 */
	@Override
	public Trace filter(Trace trace, String filterValue, int order) {
		List<LogEntry> l = new Vector<LogEntry>();
		double fValue = Double.parseDouble(filterValue);

		for (LogEntry e : trace.getEntries()) {
			List<Attribute> attrList = e.getData();
			for (int i = 0; i < attrList.size(); i++) {
				if (attrList.get(i).getName().equals(attribute)) {
					double value = Double.parseDouble(attrList.get(i).getContent());
					switch (order) {
					case Constants.FILTER_NE:
						if (value < fValue)
							l.add(e);
						break;
					case Constants.FILTER_LT:
						if (value < fValue)
							l.add(e);
						break;
					case Constants.FILTER_LE:
						if (value <= fValue)
							l.add(e);
						break;
					case Constants.FILTER_EQ:
						if (value == fValue)
							l.add(e);
						break;
					case Constants.FILTER_GE:
						if (value >= fValue)
							l.add(e);
						break;
					case Constants.FILTER_GT:
						if (value > fValue)
							l.add(e);
						break;
					default:
						throw new RuntimeException("Filter has wrong directives");
					}
					break;
				}
			}
		}

		return new Trace(l, trace.getTargetFunction());
	}
}
