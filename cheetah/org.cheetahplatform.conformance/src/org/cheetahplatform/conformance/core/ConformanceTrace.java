package org.cheetahplatform.conformance.core;

import java.util.ArrayList;
import java.util.List;

public class ConformanceTrace {
	private List<ConformanceActivity> trace;

	public ConformanceTrace() {
		trace = new ArrayList<ConformanceActivity>();
	}

	public void add(ConformanceActivity activity) {
		trace.add(activity);
	}
}
