/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.EndNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.NodeVisitor;
import org.cheetahplatform.core.imperative.modeling.StartNode;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public class SingleEntrySingleExitDecomposer extends NodeVisitor {
	private final Map<INode, ISingleEntrySingleExitComponent> nodeToComponent;
	private ISingleEntrySingleExitComponent current;

	public SingleEntrySingleExitDecomposer() {
		super(MERGE_NODES_FOR_EACH_BRANCH, true);

		this.nodeToComponent = new HashMap<INode, ISingleEntrySingleExitComponent>();
	}

	/**
	 * Returns the current.
	 * 
	 * @return the current
	 */
	public ISingleEntrySingleExitComponent getComponent() {
		return current;
	}

	public ISingleEntrySingleExitComponent getComponent(INode join) {
		return nodeToComponent.get(join);
	}

	@Override
	public void visitActivity(ImperativeActivity activity) {
		super.visitActivity(activity);

		current = current.appendActivity(activity);
		nodeToComponent.put(activity, current);
	}

	@Override
	public void visitAndJoin(AndJoin node) {
		super.visitAndJoin(node);

		nodeToComponent.put(node, current.getParent());
		current = current.appendAndJoin(this, node);
	}

	@Override
	public void visitAndSplit(AndSplit node) {
		super.visitAndSplit(node);

		current = current.appendAndSplit(node);
		nodeToComponent.put(node, current);
	}

	@Override
	public void visitEnd(EndNode node) {
		super.visitEnd(node);

		current = current.appendEnd(node);
		nodeToComponent.put(node, current);
	}

	@Override
	public void visitLateBindingBoxEnd(LateBindingBox node) {
		super.visitLateBindingBoxEnd(node);

		current = current.appendLateBindingBoxEnd(node);
	}

	@Override
	public void visitLateBindingBoxStart(LateBindingBox node) {
		super.visitLateBindingBoxStart(node);

		current = current.appendLateBindingBoxStart(node);
		nodeToComponent.put(node, current);
	}

	@Override
	public void visitLateModelingBoxEnd(LateModelingBox node) {
		super.visitLateModelingBoxEnd(node);

		current = current.appendLateModelingBoxEnd(node);
		nodeToComponent.put(node, current);
	}

	@Override
	public void visitLateModelingBoxStart(LateModelingBox node) {
		super.visitLateModelingBoxStart(node);

		current = current.appendLateModelingBoxStart(node);
		nodeToComponent.put(node, current);
	}

	@Override
	public void visitLoopEnd(LoopEnd node) {
		super.visitLoopEnd(node);

		nodeToComponent.put(node, current.getParent());
		current = current.appendLoopEnd(this, node);
	}

	@Override
	public void visitLoopStart(LoopStart node) {
		super.visitLoopStart(node);

		current = current.appendLoopStart(node);
		nodeToComponent.put(node, current);
	}

	@Override
	public void visitStart(StartNode node) {
		super.visitStart(node);

		if (current != null) {
			current = current.appendStart(node);
		} else {
			current = new ProcessFragment(current, node);
		}
	}

	@Override
	public void visitXorJoin(XorJoin node) {
		super.visitXorJoin(node);

		nodeToComponent.put(node, current.getParent());
		current = current.appendXorJoin(this, node);
	}

	@Override
	public void visitXorSplit(XorSplit node) {
		super.visitXorSplit(node);

		current = current.appendXorSplit(node);
		nodeToComponent.put(node, current);
	}

}
