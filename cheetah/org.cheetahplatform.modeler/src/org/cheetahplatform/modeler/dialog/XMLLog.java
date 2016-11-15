package org.cheetahplatform.modeler.dialog;

import java.sql.Timestamp;

public class XMLLog {
	private int id;
	private Timestamp timestamp;

	public XMLLog(int id, Timestamp timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}

	public int getId() {
		return id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

}