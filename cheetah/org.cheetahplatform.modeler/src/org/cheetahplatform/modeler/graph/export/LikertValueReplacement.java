package org.cheetahplatform.modeler.graph.export;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.csv.IValueReplacement;
import org.cheetahplatform.common.logging.csv.LegendEntry;

public class LikertValueReplacement implements IValueReplacement {

	protected List<String> attributeNames;
	protected Map<String, Integer> likertValues;

	public LikertValueReplacement(String attributeName) {
		Assert.isNotNull(attributeName);
		this.attributeNames = new ArrayList<String>();
		attributeNames.add(attributeName);
		likertValues = new HashMap<String, Integer>();
	}

	public void addAttributeName(String attributeName) {
		attributeNames.add(attributeName.trim());
	}

	/**
	 * @param answer
	 * @param value
	 */
	public void addValue(String answer, int value) {
		likertValues.put(answer.trim(), value);
	}

	@Override
	public boolean applies(Attribute attribute) {
		return attributeNames.contains(attribute.getName().trim());
	}

	@Override
	public String getReplacedContent(Attribute attribute) {
		Integer value = likertValues.get(attribute.getContent().trim());
		return String.valueOf(value);
	}

	@Override
	public List<LegendEntry> getReplacementLegend() {
		List<LegendEntry> legend = new ArrayList<LegendEntry>();
		for (Map.Entry<String, Integer> entry : likertValues.entrySet()) {
			legend.add(new LegendEntry(String.valueOf(entry.getValue()), entry.getKey()));
		}

		Collections.sort(legend, new Comparator<LegendEntry>() {
			@Override
			public int compare(LegendEntry entry1, LegendEntry entry2) {
				int difference = Integer.parseInt(entry1.getSymbol()) - Integer.parseInt(entry2.getSymbol());
				if (difference != 0) {
					return difference;
				}

				return Collator.getInstance().compare(entry1.getExplanation(), entry2.getExplanation());
			}
		});

		return legend;
	}

}
