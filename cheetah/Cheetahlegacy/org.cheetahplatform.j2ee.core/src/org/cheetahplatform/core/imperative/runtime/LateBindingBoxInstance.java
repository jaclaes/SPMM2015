/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;

public class LateBindingBoxInstance extends AbstractBoxInstance {

	private static final long serialVersionUID = -8949385595589721895L;

	public LateBindingBoxInstance(ImperativeProcessInstance processInstance, LateBindingBox box) {
		super(processInstance, box);
	}

	public void accept(INodeInstanceVisitor visitor) {
		visitor.visitLateBindingBox(this);
		if (instance != null) {
			instance.visit(visitor);
		}

		visitSuccessors(visitor);
	}

	@Override
	public void selectSubProcess(ImperativeProcessSchema sequence) {
		Assert.isTrue(((LateBindingBox) node).containsSequence(sequence));

		super.selectSubProcess(sequence);
	}

}
