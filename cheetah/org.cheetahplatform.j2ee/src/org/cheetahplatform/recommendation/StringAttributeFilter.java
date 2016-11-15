package org.cheetahplatform.recommendation;

import java.util.List;
import java.util.Vector;

import pmsedit.logmodel.Attribute;
import pmsedit.logmodel.LogEntry;

/**
 * This filter searches for log entries which have a specific attribute value.
 * 
 * @author Christian Haisjackl
 */
public class StringAttributeFilter extends Filter {
	/**
	 * @param attribute
	 *            The attribute to search for
	 */
	protected StringAttributeFilter(String attribute) {
		super(attribute);
	}

	/**
	 * @param order
	 *            Only equal and not equal possible
	 * @see org.cheetahplatform.recommendation.Filter#filter(org.cheetahplatform.recommendation.Trace, String, int)
	 */
	@Override
	public Trace filter(Trace trace, String filterValue, int order) {
		List<LogEntry> l = new Vector<LogEntry>();

		for (LogEntry e : trace.getEntries()) {
			List<Attribute> attrList = e.getData();
			for (int i = 0; i < attrList.size(); i++) {
				if (attrList.get(i).getName().equals(attribute)) {
					String value = attrList.get(i).getContent();
					switch (order) {
					case Constants.FILTER_NE:
						if (!value.equals(filterValue))
							l.add(e);
						break;
					case Constants.FILTER_EQ:
						if (value.equals(filterValue))
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
