package org.cheetahplatform.core.declarative.constraint;

import java.text.MessageFormat;

import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public class SelectionConstraint extends AbstractDeclarativeConstraintWithOneActivity {

	private static final long serialVersionUID = 4846492318631303038L;
	protected static final int NO_MINIMUM_SET = 0;
	protected static final int NO_MAXIMUM_SET = Integer.MAX_VALUE;

	protected int minimum;
	protected int maximum;

	public SelectionConstraint(DeclarativeActivity activity, int minimum, int maximum) {
		super(activity);

		this.minimum = minimum;
		this.maximum = maximum;
	}

	public boolean canTerminate(DeclarativeProcessInstance processInstance) {
		int instanceCount = processInstance.getCompletedNodeInstanceCount(activity);
		return minimum <= instanceCount;
	}

	public String getDescription() {
		if (minimum == NO_MINIMUM_SET) {
			return MessageFormat.format("Activity ''{0}'' must be executed at most {1} time(s).", activity.getName(), maximum);
		}
		if (maximum == NO_MAXIMUM_SET) {
			return MessageFormat.format("Activity ''{0}'' must be executed at least {1} time(s).", activity.getName(), minimum);
		}
		if (minimum == maximum) {
			return MessageFormat.format("Activity ''{0}'' must be executed exactly {1} time(s).", activity.getName(), minimum);
		}

		return MessageFormat.format("Activity ''{0}'' must be executed at least {1} time(s) and at most {2} time(s).", activity.getName(),
				minimum, maximum);
	}

	public boolean isExecutable(DeclarativeActivity activity, DeclarativeProcessInstance processInstance) {
		if (!this.activity.equals(activity)) {
			return true;
		}

		int instanceCount = processInstance.getUncanceledCount(activity);
		return instanceCount < maximum;
	}

	/**
	 * @param maximum
	 *            the maximum to set
	 */
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	/**
	 * @param minimum
	 *            the minimum to set
	 */
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

}
