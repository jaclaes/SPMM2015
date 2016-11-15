package org.cheetahplatform.tdm.engine;

public class TDMTestFailedException extends Exception {

	private static final long serialVersionUID = -5985178771231958826L;

	private final ITDMStep failureCause;

	public TDMTestFailedException(ITDMStep cause) {
		failureCause = cause;
	}

	public ITDMStep getFailureCause() {
		return failureCause;
	}

}
