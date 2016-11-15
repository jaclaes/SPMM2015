/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.imperative.modeling.sese;

import java.util.List;

import org.cheetahplatform.core.imperative.modeling.EndNode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.LateBindingBox;
import org.cheetahplatform.core.imperative.modeling.LateModelingBox;
import org.cheetahplatform.core.imperative.modeling.StartNode;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;

public interface ISingleEntrySingleExitComponent {
	/**
	 * Add the given component as child.
	 * 
	 * @param child
	 *            the child to add
	 */
	void addChild(ISingleEntrySingleExitComponent child);

	/**
	 * Append the given activity to this component - create a new child if necessary.
	 * 
	 * @param activity
	 *            the activity to be appended
	 * @return the current component, i.e., this if no new child has been added, the new child otherwise
	 */
	ISingleEntrySingleExitComponent appendActivity(ImperativeActivity activity);

	ISingleEntrySingleExitComponent appendAndJoin(SingleEntrySingleExitDecomposer decomposer, AndJoin join);

	ISingleEntrySingleExitComponent appendAndSplit(AndSplit split);

	ISingleEntrySingleExitComponent appendEnd(EndNode node);

	ISingleEntrySingleExitComponent appendLateBindingBoxEnd(LateBindingBox node);

	ISingleEntrySingleExitComponent appendLateBindingBoxStart(LateBindingBox node);

	ISingleEntrySingleExitComponent appendLateModelingBoxEnd(LateModelingBox node);

	ISingleEntrySingleExitComponent appendLateModelingBoxStart(LateModelingBox node);

	ISingleEntrySingleExitComponent appendLoopEnd(SingleEntrySingleExitDecomposer decomposer, LoopEnd end);

	ISingleEntrySingleExitComponent appendLoopStart(LoopStart start);

	ISingleEntrySingleExitComponent appendStart(StartNode node);

	ISingleEntrySingleExitComponent appendXorJoin(SingleEntrySingleExitDecomposer decomposer, XorJoin join);

	ISingleEntrySingleExitComponent appendXorSplit(XorSplit split);

	/**
	 * Return the children.
	 * 
	 * @return the children
	 */
	List<ISingleEntrySingleExitComponent> getChildren();

	ISingleEntrySingleExitComponent getParent();
}
