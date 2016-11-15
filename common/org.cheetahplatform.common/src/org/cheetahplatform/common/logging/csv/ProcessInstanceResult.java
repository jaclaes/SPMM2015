package org.cheetahplatform.common.logging.csv;

import java.util.ArrayList;
import java.util.List;

public class ProcessInstanceResult {
	private List<IMetricResult> results;

	public ProcessInstanceResult() {
		this.results = new ArrayList<IMetricResult>();
	}

	public void addResult(IMetricResult result) {
		results.add(result);
	}

	public List<IMetricResult> getResults() {
		return results;
	}

}
