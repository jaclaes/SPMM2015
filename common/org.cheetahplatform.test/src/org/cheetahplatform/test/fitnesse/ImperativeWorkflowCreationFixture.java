package org.cheetahplatform.test.fitnesse;

import static org.cheetahplatform.test.fitnesse.ImperativeFitnessHelper.INSTANCE;
import fit.ActionFixture;

public class ImperativeWorkflowCreationFixture extends ActionFixture {

	public void addToLateBindingBox() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String boxName = cells.more.text();
		String actions = cells.more.more.text();
		INSTANCE.addToLateBindingBox(boxName, actions);

		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void addToLateModelingBox() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String boxName = cells.more.text();
		String actions = cells.more.more.text();
		INSTANCE.addToLateModelingBox(boxName, actions);

		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	public void createActivity() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String activityName = cells.more.text();
		INSTANCE.createActivity(activityName);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createAndJoin() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createAndJoin(name);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createAndSplit() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createAndSplit(name);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createLateBindingBox() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}
		String name = cells.more.text();
		INSTANCE.createLateBindingBox(name);
		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createLateModelingBox() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createLateModelingBox(name);
		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createLoopEnd() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createLoopEnd(name);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createLoopStart() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createLoopStart(name);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createXorJoin() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createXorJoin(name);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void createXorSplit() {
		if (cells.more == null || cells.more.more == null) {
			throw new IllegalArgumentException("Must specify at least three columns");
		}

		String name = cells.more.text();
		INSTANCE.createXorSplit(name);

		cells.more.more.addToBody("Ok!");
		right(cells.more.more);
	}

	public void link() {
		if (cells.more == null || cells.more.more == null || cells.more.more.more == null) {
			throw new IllegalArgumentException("Must specify at least four columns");
		}

		String node1 = cells.more.text();
		String node2 = cells.more.more.text();

		INSTANCE.link(node1, node2);

		cells.more.more.more.addToBody("Ok!");
		right(cells.more.more.more);
	}

	@Override
	public void start() throws Throwable {
		INSTANCE.createProcessSchema();
		super.start();
	}

}