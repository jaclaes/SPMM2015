/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling;

import java.util.List;

import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public interface INodeVisitor {
	/**
	 * Visiting strategy for visiting merge nodes only once for all incoming branches.
	 */
	String MERGE_NODES_ONCE = "MERGE_NODES_ONCE";
	/**
	 * Visiting strategy for visiting merge nodes for each incoming branch.
	 */
	String MERGE_NODES_FOR_EACH_BRANCH = "MERGE_NODES_FOR_EACH_BRANCH";

	/**
	 * Returns the visiting strategy to be applied, either {@link #MERGE_NODES_ONCE} or {@value #MERGE_NODES_FOR_EACH_BRANCH}.
	 * 
	 * @return the strategy
	 */
	String getVisitingStrategy();

	boolean hasVisited(IImperativeNode node);

	boolean hasVisited(List<IImperativeNode> nodes);

	void visitActivity(ImperativeActivity node);

	void visitAndJoin(AndJoin node);

	void visitAndSplit(AndSplit node);

	void visitEnd(EndNode node);

	/**
	 * Returns <code>true</code> if all predecessor nodes of joins should be visited before going on with visiting successors,
	 * <code>false</code> otherwise.
	 * 
	 * @return
	 */
	boolean visitJoinPredecessorsFirst();

	void visitLateBindingBoxEnd(LateBindingBox node);

	void visitLateBindingBoxStart(LateBindingBox node);

	void visitLateModelingBoxEnd(LateModelingBox node);

	void visitLateModelingBoxStart(LateModelingBox node);

	void visitLoopEnd(LoopEnd node);

	void visitLoopStart(LoopStart node);

	void visitStart(StartNode node);

	void visitXorJoin(XorJoin node);

	void visitXorSplit(XorSplit node);
}
