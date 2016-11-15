package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public abstract class ChangeTask1XInitializer extends AbstractChangeTestInitializer {

	public ChangeTask1XInitializer(TDMProcess process) {
		super(process);
	}

	protected void createTest1() {
		TDMTestInitializer initializerA = new TDMTestInitializer(process, validator, testCount + ") <" + b + "," + c + "," + d + "," + f
				+ "," + j + "> is a valid trace; " + j + " must be executed at least once");
		initializerA.addActivityInstance(b, 0);
		initializerA.addActivityInstance(c, 1);
		initializerA.addActivityInstance(d, 2);
		initializerA.addActivityInstance(f, 3);
		initializerA.addActivityInstance(j, 4);
		initializerA.addTerminationAssertion(0, 0, 4, 45, false);
		initializerA.addTerminationAssertion(5, 9, true);

		testCount++;
	}

	protected void createTest2() {
		TDMTestInitializer initializerB = new TDMTestInitializer(process, validator, testCount + ") " + b + " must be executed before " + d);
		initializerB.addActivityInstance(b, 0);
		initializerB.addActivityInstance(c, 1);
		initializerB.addExecutionAssertion(d, 0, 0, 0, 45, false);
		initializerB.addExecutionAssertion(d, 2, 0, 6, 0, true);

		testCount++;
	}

	protected void createTest3() {
		TDMTestInitializer initializerC = new TDMTestInitializer(process, validator, testCount + ") " + h + ", " + l + ", " + o + " and "
				+ p + " must be executed before " + g);
		initializerC.addActivityInstance(h, 0);
		initializerC.addActivityInstance(l, 1);
		initializerC.addActivityInstance(p, 2);
		initializerC.addActivityInstance(o, 3);
		initializerC.addActivityInstance(k, 4);
		initializerC.addActivityInstance(g, 6);
		initializerC.addExecutionAssertion(g, 0, 0, 3, 45, false);

		testCount++;
	}

	protected void createTest4() {
		TDMTestInitializer initializerD = new TDMTestInitializer(process, validator, testCount + ") <" + a + "," + f + "," + b + "," + c
				+ "," + d + "," + j + "> is a valid trace; " + e + " cannot be executed after " + a);
		initializerD.addActivityInstance(a, 0);
		initializerD.addActivityInstance(f, 2);
		initializerD.addActivityInstance(b, 3);
		initializerD.addActivityInstance(c, 4);
		initializerD.addActivityInstance(d, 5);
		initializerD.addActivityInstance(j, 6);
		initializerD.addExecutionAssertion(e, 1, 0, 7, 0, false);
		initializerD.addTerminationAssertion(7, 11, true);

		testCount++;
	}

	protected void createTest5() {
		TDMTestInitializer initializerE = new TDMTestInitializer(process, validator, testCount + ") " + e + " cannot be executed after "
				+ m);
		initializerE.addActivityInstance(m, 0);
		initializerE.addActivityInstance(i, 2);
		initializerE.addExecutionAssertion(e, 1, 15, 3, 0, false);

		testCount++;
	}

}
