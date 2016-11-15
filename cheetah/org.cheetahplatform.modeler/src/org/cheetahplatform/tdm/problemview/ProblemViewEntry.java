package org.cheetahplatform.tdm.problemview;

import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.TDMTest;

public class ProblemViewEntry {
	private ITDMStep step;
	private final TDMTest test;

	public ProblemViewEntry(TDMTest test, ITDMStep step) {
		this.test = test;
		this.step = step;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProblemViewEntry other = (ProblemViewEntry) obj;

		if (getMessage() == null) {
			if (other.getMessage() != null)
				return false;
		} else if (!getMessage().equals(other.getMessage()))
			return false;

		if (test == null) {
			if (other.test != null)
				return false;
		} else if (!getTest().equals(other.test))
			return false;

		if (step.getSource() == null) {
			if (other.step.getSource() != null)
				return false;
		} else if (!step.getSource().equals(other.step.getSource()))
			return false;

		return true;
	}

	public String getMessage() {
		return step.getFailure().getProblem();
	}

	public TDMTest getTest() {
		return test;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
		result = prime * result + ((test == null) ? 0 : test.hashCode());
		return result;
	}

	public void reveal() {
		step.getFailure().revealFailure();
	}

}