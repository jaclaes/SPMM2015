package org.cheetahplatform.modeler.graph.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.Assert;

public class ModelingTranscript {
	private Date startTime;
	private String originator;
	private String text;
	private long databaseId;
	private Date endTime;

	public ModelingTranscript(Date startTime, Date endTime, String originator, String text) {
		Assert.isNotNull(startTime);
		Assert.isNotNull(endTime);
		Assert.isNotNull(originator);
		Assert.isNotNull(text);
		this.startTime = startTime;
		this.endTime = endTime;
		this.originator = originator;
		this.text = text;
	}

	public ModelingTranscript(Date startTime, Date endTime, String originator, String text, long databaseId) {
		this(startTime, endTime, originator, text);
		this.databaseId = databaseId;
	}

	public void changeTranscriptOffset(int seconds) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(startTime);
		calendar.add(Calendar.SECOND, seconds);
		startTime = calendar.getTime();
	}

	public long getDatabaseId() {
		return databaseId;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getOriginator() {
		return originator;
	}

	public Date getStartTime() {
		return startTime;
	}

	public String getText() {
		return text;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public void setStartTime(Date transcriptDate) {
		this.startTime = transcriptDate;
	}

	public void setText(String text) {
		this.text = text;
	}
}
