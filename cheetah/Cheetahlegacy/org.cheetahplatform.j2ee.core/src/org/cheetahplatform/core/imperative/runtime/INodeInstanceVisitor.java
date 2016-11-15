/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.runtime;

import java.util.List;

import org.cheetahplatform.core.imperative.runtime.routing.AbstractJoinInstance;
import org.cheetahplatform.core.imperative.runtime.routing.AndSplitInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopEndInstance;
import org.cheetahplatform.core.imperative.runtime.routing.LoopStartInstance;
import org.cheetahplatform.core.imperative.runtime.routing.XorSplitInstance;

public interface INodeInstanceVisitor {
	boolean hasVisited(IImperativeNodeInstance xorJoinInstance);

	boolean hasVisited(List<IImperativeNodeInstance> instances);

	void visitActivity(ImperativeActivityInstance instance);

	void visitAndJoin(AbstractJoinInstance instance);

	void visitAndSplit(AndSplitInstance instance);

	void visitEndNodeInstance(EndNodeInstance instance);

	void visitLateBindingBox(LateBindingBoxInstance instance);

	void visitLateModelingBox(LateModelingBoxInstance instance);

	void visitLoopEnd(LoopEndInstance instance);

	void visitLoopStart(LoopStartInstance instance);

	void visitStartNode(StartNodeInstance instance);

	void visitXorJoin(AbstractJoinInstance instance);

	void visitXorSplit(XorSplitInstance instance);
}
