/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.core.imperative.runtime.routing.AbstractJoinInstance;
import org.cheetahplatform.core.imperative.runtime.routing.AndSplitInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopEndInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopStartInstance;
import org.cheetahplatform.core.imperative.runtime.routing.XorSplitInstance;

public class AbstractNodeInstanceVisitor implements INodeInstanceVisitor {
	private final Set<IImperativeNodeInstance> visitedInstances;

	public AbstractNodeInstanceVisitor() {
		this.visitedInstances = new HashSet<IImperativeNodeInstance>();
	}

	/**
	 * @return the visitedInstances
	 */
	public Set<IImperativeNodeInstance> getVisitedInstances() {
		return visitedInstances;
	}

	public boolean hasVisited(IImperativeNodeInstance instance) {
		return visitedInstances.contains(instance);
	}

	public final boolean hasVisited(List<IImperativeNodeInstance> instances) {
		return visitedInstances.containsAll(instances);
	}

	public void visitActivity(ImperativeActivityInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitAndJoin(AbstractJoinInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitAndSplit(AndSplitInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitEndNodeInstance(EndNodeInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitLateBindingBox(LateBindingBoxInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitLateModelingBox(LateModelingBoxInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitLoopEnd(LoopEndInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitLoopStart(LoopStartInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitStartNode(StartNodeInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitXorJoin(AbstractJoinInstance instance) {
		visitedInstances.add(instance);
	}

	public void visitXorSplit(XorSplitInstance instance) {
		visitedInstances.add(instance);
	}

}
