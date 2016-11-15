/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.modeler.test.model;

import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.tdm.weekly.editpart.WeeklyConstraintEditPart;
import org.eclipse.gef.EditPart;

public class WeeklyConstraint extends GenericModel {
	private final IDeclarativeConstraint constraint;

	public WeeklyConstraint(IGenericModel parent, IDeclarativeConstraint constraint) {
		super(parent);

		this.constraint = constraint;
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new WeeklyConstraintEditPart(this);
	}

	public DeclarativeActivity getEndActivity() {
		return constraint.getEndActivities().get(0);

	}

	public DeclarativeActivity getStartActivity() {
		return constraint.getStartActivities().get(0);
	}

	public boolean isIncoming(WeeklyActivity activity) {
		return isSupportedConstraint() && constraint.getActivities().indexOf(activity.getActivity().getNode()) > 0;
	}

	public boolean isOutgoing(WeeklyActivity activity) {
		return isSupportedConstraint() && constraint.getActivities().indexOf(activity.getActivity().getNode()) == 0;
	}

	private boolean isSupportedConstraint() {
		return constraint instanceof PrecedenceConstraint;
	}

}
