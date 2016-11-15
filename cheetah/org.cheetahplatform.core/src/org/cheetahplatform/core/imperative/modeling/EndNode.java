/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import org.cheetahplatform.core.common.modeling.IProcessInstance;
import org.cheetahplatform.core.imperative.runtime.EndNodeInstance;
import org.cheetahplatform.core.imperative.runtime.IImperativeNodeInstance;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;

//TODO consistency checks
public class EndNode extends ImperativeActivity {
	private static final long serialVersionUID = 3803711483894738427L;

	public EndNode() {
		super("End");
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEnd(this);
	}

	@Override
	public IImperativeNodeInstance instantiate(IProcessInstance processInstance) {
		return new EndNodeInstance((ImperativeProcessInstance) processInstance, this);
	}

	@Override
	public String getType() {
		return IImperativeNode.TYPE_END_NODE;
	}

}
