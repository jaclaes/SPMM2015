package org.cheetahplatform.core.declarative.constraint;

public class ConstraintEvaluation {
	private boolean result;
	private IDeclarativeConstraint constraint;

	public ConstraintEvaluation(boolean result, IDeclarativeConstraint constraint) {
		this.result = result;
		this.constraint = constraint;
	}

	/**
	 * @return the constraint
	 */
	public IDeclarativeConstraint getConstraint() {
		return constraint;
	}

	public boolean getResult() {
		return result;
	}

}
