package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public abstract class ChangeTask2XInitializer extends AbstractChangeTestInitializer {

	protected ChangeTask2XInitializer(TDMProcess process) {
		super(process);
	}

	protected void createTest1() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") <" + j + "," + k + "," + p + "," + l
				+ "> is a valid trace; " + l + " must be executed at least once");
		initializer.addActivityInstance(j, 0);
		initializer.addActivityInstance(k, 1);
		initializer.addActivityInstance(p, 2);
		initializer.addActivityInstance(l, 3);
		initializer.addTerminationAssertion(0, 0, 3, 45, false);
		initializer.addTerminationAssertion(4, 8, true);

		testCount++;
	}

	protected void createTest2() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") " + i + " cannot be executed after " + m);
		initializer.addActivityInstance(m, 1);
		initializer.addExecutionAssertion(i, 0, 0, 1, 0, true);
		initializer.addExecutionAssertion(i, 2, 0, 6, 0, false);

		testCount++;
	}

	protected void createTest3() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") " + c + " cannot be executed after " + d);
		initializer.addActivityInstance(d, 1);
		initializer.addExecutionAssertion(c, 0, 0, 1, 0, true);
		initializer.addExecutionAssertion(c, 2, 0, 6, 0, false);

		testCount++;
	}

	protected void createTest4() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") <" + j + "," + k + "," + l + "," + p
				+ "," + a + "," + e + "," + i + "," + f + "> is a valid trace; " + a + " must be followed by " + f);
		initializer.addActivityInstance(j, 0);
		initializer.addActivityInstance(k, 1);
		initializer.addActivityInstance(l, 2);
		initializer.addActivityInstance(p, 3);
		initializer.addActivityInstance(a, 4);
		initializer.addActivityInstance(e, 5);
		initializer.addActivityInstance(i, 6);
		initializer.addActivityInstance(f, 7);
		initializer.addTerminationAssertion(5, 0, 7, 45, false);
		initializer.addTerminationAssertion(8, 0, 12, 0, true);

		testCount++;
	}

	protected void createTest5() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") <" + j + "," + k + "," + l + "," + p
				+ "," + m + "," + n + "," + o + "," + p + "> is a valid trace; " + n + " must be followed by " + p);
		initializer.addActivityInstance(j, 0);
		initializer.addActivityInstance(k, 1);
		initializer.addActivityInstance(l, 2);
		initializer.addActivityInstance(p, 3);
		initializer.addActivityInstance(m, 4);
		initializer.addActivityInstance(n, 5);
		initializer.addActivityInstance(o, 6);
		initializer.addActivityInstance(p, 7);
		initializer.addTerminationAssertion(5, 0, 7, 45, false);
		initializer.addTerminationAssertion(8, 0, 12, 0, true);

		testCount++;
	}
}
