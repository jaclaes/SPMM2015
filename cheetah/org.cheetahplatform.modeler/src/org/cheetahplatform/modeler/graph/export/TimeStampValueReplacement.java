package org.cheetahplatform.modeler.graph.export;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.csv.IValueReplacement;
import org.cheetahplatform.common.logging.csv.LegendEntry;

public class TimeStampValueReplacement implements IValueReplacement {

	private static final String TIME_FORMAT = "dd.MM.yyyy hh:mm:ss";

	@Override
	public boolean applies(Attribute attribute) {
		return CommonConstants.ATTRIBUTE_TIMESTAMP.equals(attribute.getName().trim());
	}

	@Override
	public String getReplacedContent(Attribute attribute) {
		Date date = new Date(attribute.getLongContent());
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		return format.format(date);
	}

	@Override
	public List<LegendEntry> getReplacementLegend() {
		return Collections.emptyList();
	}
}
