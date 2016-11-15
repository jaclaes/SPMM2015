/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.fitnesse;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.core.imperative.modeling.sese.AbstractFragment;
import org.cheetahplatform.core.imperative.modeling.sese.ISingleEntrySingleExitComponent;
import org.cheetahplatform.core.imperative.modeling.sese.SingleEntrySingleExitNode;
import org.eclipse.core.runtime.Assert;

import fit.ActionFixture;

public class SingleEntrySingleExitFixture extends ActionFixture {
	private Map<String, ISingleEntrySingleExitComponent> nameToComponent;

	public void children() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		ISingleEntrySingleExitComponent component = evaluateExpression();

		int expected = Integer.parseInt(cells.more.more.text());
		int actual = component.getChildren().size();
		if (expected == actual) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, String.valueOf(actual));
		}
	}

	public void endNode() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		ISingleEntrySingleExitComponent component = evaluateExpression();
		Assert.isTrue(component instanceof AbstractFragment, "Expected instance of " + AbstractFragment.class.getSimpleName()
				+ ", but was: " + component.getClass().getSimpleName());
		String expected = cells.more.more.text();
		String actual = ((AbstractFragment) component).getEnd().getName();

		if (expected.equals(actual)) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, actual);
		}
	}

	private ISingleEntrySingleExitComponent evaluateExpression() {
		String expression = cells.more.text();
		ISingleEntrySingleExitComponent toStore = null;

		if (!ImperativeFitnessHelper.hasChildAccessorSyntax(expression)) {
			toStore = nameToComponent.get(expression);
		} else {
			int child = ImperativeFitnessHelper.parseOccurrence(expression);
			toStore = nameToComponent.get(ImperativeFitnessHelper.parseName(expression));
			Assert.isNotNull(toStore, "There is no component for name " + expression);
			Assert.isTrue(child < toStore.getChildren().size(), "Component " + expression + " has less than " + child + " children");
			toStore = toStore.getChildren().get(child);
		}

		Assert.isNotNull(toStore);
		return toStore;
	}

	public void name() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		ISingleEntrySingleExitComponent component = evaluateExpression();
		Assert.isTrue(component instanceof SingleEntrySingleExitNode, "Expected instance of "
				+ SingleEntrySingleExitNode.class.getSimpleName() + ", but was: " + component.getClass().getSimpleName());
		String expected = cells.more.more.text();
		String actual = ((SingleEntrySingleExitNode) component).getActivity().getName();

		if (expected.equals(actual)) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, actual);
		}
	}

	@Override
	public void start() throws Throwable {
		super.start();

		nameToComponent = new HashMap<String, ISingleEntrySingleExitComponent>();
		ISingleEntrySingleExitComponent component = ImperativeFitnessHelper.INSTANCE.decomposeToSingleEntrySingleExit();
		nameToComponent.put("component", component);
	}

	public void startNode() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		ISingleEntrySingleExitComponent component = evaluateExpression();
		Assert.isTrue(component instanceof AbstractFragment, "Expected instance of " + AbstractFragment.class.getSimpleName()
				+ ", but was: " + component.getClass().getSimpleName());
		String expected = cells.more.more.text();
		String actual = ((AbstractFragment) component).getStart().getName();

		if (expected.equals(actual)) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, actual);
		}
	}

	public void store() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		ISingleEntrySingleExitComponent toStore = evaluateExpression();

		String nameToStore = cells.more.more.text();
		nameToComponent.put(nameToStore, toStore);
		right(cells.more.more.more);
		cells.more.more.more.addToBody("Ok!");
	}

	public void type() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		ISingleEntrySingleExitComponent component = evaluateExpression();
		String expected = cells.more.more.text();
		String actual = component.getClass().getSimpleName();

		if (expected.equals(actual)) {
			right(cells.more.more);
		} else {
			wrong(cells.more.more, actual);
		}
	}
}
