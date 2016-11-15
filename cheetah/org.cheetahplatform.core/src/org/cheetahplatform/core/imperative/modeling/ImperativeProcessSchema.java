/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.runtime.ImperativeProcessInstance;

public class ImperativeProcessSchema extends ProcessSchema {
	private static final long serialVersionUID = -7253734506573349147L;
	private final IImperativeNode start;
	private final IImperativeNode end;

	public ImperativeProcessSchema() {
		this("");
	}

	public ImperativeProcessSchema(String name) {
		super(name);
		this.start = new StartNode();
		this.nodes.add(this.start);
		this.end = new EndNode();
		this.nodes.add(this.end);
	}

	public void accept(INodeVisitor visitor) {
		start.accept(visitor);
	}

	/**
	 * Create a new task and add it to the existing ones.
	 * 
	 * @param name
	 *            the name of the task
	 * @return a new task
	 */
	public ImperativeActivity createActivity(String name) {
		ImperativeActivity activity = new ImperativeActivity(name);
		nodes.add(activity);
		return activity;
	}

	public AndJoin createAndJoin(String name) {
		AndJoin join = new AndJoin(name);
		nodes.add(join);
		return join;
	}

	public AndSplit createAndSplit(String name) {
		AndSplit split = new AndSplit(name);
		nodes.add(split);
		return split;
	}

	public LateBindingBox createLateBindingNode(String name) {
		LateBindingBox lateBindingNode = new LateBindingBox(name);
		nodes.add(lateBindingNode);
		return lateBindingNode;
	}

	public LateModelingBox createLateModelingBox(String name) {
		LateModelingBox box = new LateModelingBox(name);
		nodes.add(box);
		return box;
	}

	public LoopEnd createLoopEnd(String name) {
		LoopEnd end = new LoopEnd(name);
		nodes.add(end);
		return end;
	}

	public LoopStart createLoopStart(String name) {
		LoopStart start = new LoopStart(name);
		nodes.add(start);
		return start;
	}

	public XorJoin createXorJoin(String name) {
		XorJoin join = new XorJoin(name);
		nodes.add(join);
		return join;
	}

	public XorSplit createXorSplit(String splitName) {
		XorSplit split = new XorSplit(splitName);
		nodes.add(split);
		return split;
	}

	/**
	 * Returns the end.
	 * 
	 * @return the end
	 */
	public IImperativeNode getEnd() {
		return end;
	}

	@Override
	public IImperativeNode getNode(long id) {
		return (IImperativeNode) super.getNode(id);
	}

	/**
	 * Returns the start.
	 * 
	 * @return the start
	 */
	public IImperativeNode getStart() {
		return start;
	}

	public ImperativeProcessInstance instantiate() {
		return new ImperativeProcessInstance(this);
	}

	@Override
	public ImperativeProcessInstance instantiate(String name) {
		return new ImperativeProcessInstance(this, name);
	}
}
