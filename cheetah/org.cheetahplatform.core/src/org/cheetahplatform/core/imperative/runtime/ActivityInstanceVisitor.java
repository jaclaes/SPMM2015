package org.cheetahplatform.core.imperative.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         08.07.2009
 */
public class ActivityInstanceVisitor extends AbstractNodeInstanceVisitor {
	private List<IImperativeNodeInstance> activities;

	public ActivityInstanceVisitor() {
		activities = new ArrayList<IImperativeNodeInstance>();
	}

	/**
	 * Returns the activities.
	 * 
	 * @return the activities
	 */
	public List<IImperativeNodeInstance> getActivites() {
		return activities;
	}

	@Override
	public void visitActivity(ImperativeActivityInstance instance) {
		super.visitActivity(instance);
		activities.add(instance);
	}

	@Override
	public void visitEndNodeInstance(EndNodeInstance instance) {
		super.visitEndNodeInstance(instance);
		activities.add(instance);
	}

	@Override
	public void visitLateBindingBox(LateBindingBoxInstance instance) {
		super.visitLateBindingBox(instance);
		activities.add(instance);
	}

	@Override
	public void visitLateModelingBox(LateModelingBoxInstance instance) {
		super.visitLateModelingBox(instance);
		activities.add(instance);
	}
}
