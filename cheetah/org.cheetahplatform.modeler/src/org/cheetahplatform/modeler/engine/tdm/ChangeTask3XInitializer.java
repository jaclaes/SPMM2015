package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public abstract class ChangeTask3XInitializer extends AbstractChangeTestInitializer {

	protected ChangeTask3XInitializer(TDMProcess process) {
		super(process);
	}

	protected void createTest1() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") <" + p + "," + o + "," + n + "," + j
				+ "> is a valid trace; " + j + " must be executed at least once");
		initializer.addActivityInstance(p, 0);
		initializer.addActivityInstance(o, 1);
		initializer.addActivityInstance(n, 2);
		initializer.addActivityInstance(j, 3);
		initializer.addTerminationAssertion(0, 0, 3, 45, false);
		initializer.addTerminationAssertion(4, 0, 8, 0, true);

		testCount++;
	}

	protected void createTest2() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") " + f + " cannot be executed after " + m);
		initializer.addActivityInstance(a, 0);
		initializer.addActivityInstance(b, 1);
		initializer.addActivityInstance(j, 2);
		initializer.addActivityInstance(m, 4);
		initializer.addExecutionAssertion(f, 3, true);
		initializer.addExecutionAssertion(f, 5, 0, 9, 0, false);

		testCount++;
	}

	protected void createTest3() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") " + e + " must be preceded by " + a);
		initializer.addActivityInstance(a, 0);
		initializer.addActivityInstance(b, 1);
		initializer.addExecutionAssertion(e, 0, 0, 0, 45, false);
		initializer.addExecutionAssertion(e, 2, 0, 6, 0, true);

		testCount++;
	}

	protected void createTest4() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") <" + a + "," + b + "," + p + "," + o
				+ "," + n + "," + j + "," + h + "," + d + "," + c + "> is a valid trace; " + h + " must be followed by " + c);
		initializer.addActivityInstance(a, 0);
		initializer.addActivityInstance(b, 1);
		initializer.addActivityInstance(p, 2);
		initializer.addActivityInstance(o, 3);
		initializer.addActivityInstance(n, 4);
		initializer.addActivityInstance(j, 5);
		initializer.addActivityInstance(h, 6);
		initializer.addActivityInstance(d, 7);
		initializer.addActivityInstance(c, 8);
		initializer.addTerminationAssertion(7, 0, 8, 45, false);
		initializer.addTerminationAssertion(9, 0, 13, 0, true);

		testCount++;
	}

	protected void createTest5() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, testCount + ") " + h + " cannot be executed after " + g);
		initializer.addActivityInstance(g, 1);
		initializer.addExecutionAssertion(h, 0, 0, 1, 0, true);
		initializer.addExecutionAssertion(h, 1, 45, 6, 0, false);

		testCount++;
	}

}
