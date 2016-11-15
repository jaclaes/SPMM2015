/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.imperative.modeling.NodeVisitor;
import org.cheetahplatform.core.imperative.modeling.IImperativeNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;

public class LateModelingBoxInstance extends AbstractBoxInstance {
	private static final long serialVersionUID = 5064065302242993901L;

	private class ActivityCheckVisistor extends NodeVisitor {

		@Override
		public void visitActivity(ImperativeActivity activity) {
			super.visitActivity(activity);

			Assert.isTrue(((LateModelingBox) node).contains(activity));
		}
	}

	public LateModelingBoxInstance(ImperativeProcessInstance processInstance, IImperativeNode node) {
		super(processInstance, node);
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitLateModelingBox(this);
		if (instance != null) {
			instance.getStartInstance().accept(visitor);
		}
		visitSuccessors(visitor);
	}

	@Override
	public void selectSubProcess(ImperativeProcessSchema schema) {
		schema.accept(new ActivityCheckVisistor());

		super.selectSubProcess(schema);
	}
}
