/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.modeling;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.cheetahplatform.core.imperative.modeling.routing.AndJoin;
import org.cheetahplatform.core.imperative.modeling.routing.AndSplit;
import org.cheetahplatform.core.imperative.modeling.routing.LoopEnd;
import org.cheetahplatform.core.imperative.modeling.routing.LoopStart;
import org.cheetahplatform.core.imperative.modeling.routing.XorJoin;
import org.cheetahplatform.core.imperative.modeling.routing.XorSplit;
import org.cheetahplatform.core.imperative.modeling.sese.AbstractFragment;
import org.cheetahplatform.core.imperative.modeling.sese.AndFragment;
import org.cheetahplatform.core.imperative.modeling.sese.ISingleEntrySingleExitComponent;
import org.cheetahplatform.core.imperative.modeling.sese.LoopFragment;
import org.cheetahplatform.core.imperative.modeling.sese.Polygon;
import org.cheetahplatform.core.imperative.modeling.sese.ProcessFragment;
import org.cheetahplatform.core.imperative.modeling.sese.SingleEntrySingleExitDecomposer;
import org.cheetahplatform.core.imperative.modeling.sese.SingleEntrySingleExitNode;
import org.cheetahplatform.core.imperative.modeling.sese.XorFragment;
import org.junit.Test;

public class SingleEntrySingleExitDecomposerTest {
	@Test
	public void andFragment() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		AndSplit split = schema.createAndSplit("Split");
		schema.getStart().addSuccessor(split);
		ImperativeActivity a = schema.createActivity("A");
		split.addSuccessor(a);
		ImperativeActivity b = schema.createActivity("B");
		split.addSuccessor(b);

		AndJoin join = schema.createAndJoin("Join");
		a.addSuccessor(join);
		b.addSuccessor(join);
		join.addSuccessor(schema.getEnd());

		SingleEntrySingleExitDecomposer decomposer = new SingleEntrySingleExitDecomposer();
		schema.getStart().accept(decomposer);
		ISingleEntrySingleExitComponent component = decomposer.getComponent();

		ISingleEntrySingleExitComponent uncastedAndFragment = component.getChildren().get(0);
		assertEquals(AndFragment.class, uncastedAndFragment.getClass());
		AndFragment andFragment = (AndFragment) uncastedAndFragment;
		assertEquals(split, andFragment.getStart());
		assertEquals(join, andFragment.getEnd());

		List<ISingleEntrySingleExitComponent> andChildren = andFragment.getChildren();
		assertEquals(2, andChildren.size());
		assertEquals(Polygon.class, andChildren.get(0).getClass());
		assertEquals(Polygon.class, andChildren.get(1).getClass());

		List<ISingleEntrySingleExitComponent> branchA = andChildren.get(0).getChildren();
		assertEquals(SingleEntrySingleExitNode.class, branchA.get(0).getClass());
		assertEquals(a, ((SingleEntrySingleExitNode) branchA.get(0)).getActivity());

		List<ISingleEntrySingleExitComponent> branchB = andChildren.get(1).getChildren();
		assertEquals(SingleEntrySingleExitNode.class, branchB.get(0).getClass());
		assertEquals(b, ((SingleEntrySingleExitNode) branchB.get(0)).getActivity());
	}

	@Test
	public void loopFragment() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		LoopStart start = schema.createLoopStart("Start");
		schema.getStart().addSuccessor(start);
		ImperativeActivity a = schema.createActivity("A");
		start.addSuccessor(a);

		LoopEnd end = schema.createLoopEnd("End");
		a.addSuccessor(end);
		end.addSuccessor(start);
		end.addSuccessor(schema.getEnd());

		SingleEntrySingleExitDecomposer decomposer = new SingleEntrySingleExitDecomposer();
		schema.getStart().accept(decomposer);

		ISingleEntrySingleExitComponent component = decomposer.getComponent();
		ISingleEntrySingleExitComponent uncastedLoopFragment = component.getChildren().get(0);
		assertEquals(LoopFragment.class, uncastedLoopFragment.getClass());
		LoopFragment loopFragment = (LoopFragment) uncastedLoopFragment;
		assertEquals(start, loopFragment.getStart());
		assertEquals(end, loopFragment.getEnd());

		List<ISingleEntrySingleExitComponent> xorChildren = loopFragment.getChildren();
		assertEquals(1, xorChildren.size());
		assertEquals(Polygon.class, xorChildren.get(0).getClass());

		List<ISingleEntrySingleExitComponent> branchA = xorChildren.get(0).getChildren();
		assertEquals(SingleEntrySingleExitNode.class, branchA.get(0).getClass());
		assertEquals(a, ((SingleEntrySingleExitNode) branchA.get(0)).getActivity());
	}

	@Test
	public void simpleSequence() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		ImperativeActivity a = schema.createActivity("A");
		schema.getStart().addSuccessor(a);
		ImperativeActivity b = schema.createActivity("B");
		a.addSuccessor(b);
		b.addSuccessor(schema.getEnd());

		SingleEntrySingleExitDecomposer decomposer = new SingleEntrySingleExitDecomposer();
		schema.getStart().accept(decomposer);

		ISingleEntrySingleExitComponent component = decomposer.getComponent();
		assertEquals(ProcessFragment.class, component.getClass());
		assertEquals(schema.getStart(), ((AbstractFragment) component).getStart());
		assertEquals(schema.getEnd(), ((AbstractFragment) component).getEnd());

		List<ISingleEntrySingleExitComponent> children = component.getChildren();
		assertEquals(1, children.size());
		assertEquals(Polygon.class, children.get(0).getClass());

		List<ISingleEntrySingleExitComponent> polygonChildren = children.get(0).getChildren();
		assertEquals(2, polygonChildren.size());

		assertEquals(SingleEntrySingleExitNode.class, polygonChildren.get(0).getClass());
		assertEquals(SingleEntrySingleExitNode.class, polygonChildren.get(1).getClass());

		assertEquals(a, ((SingleEntrySingleExitNode) polygonChildren.get(0)).getActivity());
		assertEquals(b, ((SingleEntrySingleExitNode) polygonChildren.get(1)).getActivity());
	}

	@Test
	public void xorFragment() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		XorSplit split = schema.createXorSplit("Split");
		schema.getStart().addSuccessor(split);
		ImperativeActivity a = schema.createActivity("A");
		split.addSuccessor(a);
		ImperativeActivity b = schema.createActivity("B");
		split.addSuccessor(b);

		XorJoin join = schema.createXorJoin("Join");
		a.addSuccessor(join);
		b.addSuccessor(join);
		join.addSuccessor(schema.getEnd());

		SingleEntrySingleExitDecomposer decomposer = new SingleEntrySingleExitDecomposer();
		schema.getStart().accept(decomposer);

		ISingleEntrySingleExitComponent component = decomposer.getComponent();
		ISingleEntrySingleExitComponent uncastedXorFragment = component.getChildren().get(0);
		assertEquals(XorFragment.class, uncastedXorFragment.getClass());
		XorFragment xorFragment = (XorFragment) uncastedXorFragment;
		assertEquals(split, xorFragment.getStart());
		assertEquals(join, xorFragment.getEnd());

		List<ISingleEntrySingleExitComponent> xorChildren = xorFragment.getChildren();
		assertEquals(2, xorChildren.size());
		assertEquals(Polygon.class, xorChildren.get(0).getClass());
		assertEquals(Polygon.class, xorChildren.get(1).getClass());

		List<ISingleEntrySingleExitComponent> branchA = xorChildren.get(0).getChildren();
		assertEquals(SingleEntrySingleExitNode.class, branchA.get(0).getClass());
		assertEquals(a, ((SingleEntrySingleExitNode) branchA.get(0)).getActivity());

		List<ISingleEntrySingleExitComponent> branchB = xorChildren.get(1).getChildren();
		assertEquals(SingleEntrySingleExitNode.class, branchB.get(0).getClass());
		assertEquals(b, ((SingleEntrySingleExitNode) branchB.get(0)).getActivity());
	}
}
