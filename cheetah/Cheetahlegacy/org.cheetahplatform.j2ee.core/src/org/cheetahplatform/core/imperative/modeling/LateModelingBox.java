/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.modeling.INodeInstance;
import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;
import org.cheetahplatform.core.imperative.runtime.LateModelingBoxInstance;

public class LateModelingBox extends AbstractNode {
	private static final long serialVersionUID = 1812098585685854176L;
	private List<ImperativeActivity> availableActivities;

	@SuppressWarnings("unused")
	private LateModelingBox() {
		// hibernate
	}

	public LateModelingBox(String name) {
		super(name);

		availableActivities = new ArrayList<ImperativeActivity>();
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitLateModelingBoxStart(this);
		for (ImperativeActivity activity : availableActivities) {
			activity.accept(visitor);
		}
		visitor.visitLateModelingBoxEnd(this);
		visitSuccessors(visitor);
	}

	public void addActivity(ImperativeActivity activity) {
		availableActivities.add(activity);
	}

	public boolean contains(ImperativeActivity activity) {
		return availableActivities.contains(activity);
	}

	public ImperativeActivity getAvailableAction(long id) {
		for (ImperativeActivity activity : availableActivities) {
			if (activity.getCheetahId() == id) {
				return activity;
			}
		}

		return null;
	}

	/**
	 * Returns the availableActions.
	 * 
	 * @return the availableActions
	 */
	public List<ImperativeActivity> getAvailableActivities() {
		return Collections.unmodifiableList(availableActivities);
	}

	public String getType() {
		return IImperativeNode.TYPE_LATE_MODELING_NODE;
	}

	public INodeInstance instantiate(IProcessInstance processInstance) {
		return new LateModelingBoxInstance((ImperativeProcessInstance) processInstance, this);
	}

}
