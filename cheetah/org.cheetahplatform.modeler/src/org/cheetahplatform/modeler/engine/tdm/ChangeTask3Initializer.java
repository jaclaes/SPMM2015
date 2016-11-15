package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.modeler.engine.IdValidator;
import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask3Initializer {

	private final TDMProcess process;

	public ChangeTask3Initializer(TDMProcess process) {
		this.process = process;
	}

	public void initialize() {
		IdValidator validator = new IdValidator();
		// last used id = -104

		TDMTestInitializer initializer1 = new TDMTestInitializer(process, validator, "a) T must be preceded by R", -1001);
		initializer1.addExecutionAssertion("T", 0, 0, 1, 30, false, -2);
		initializer1.addActivityInstance("Q", 0, -104);
		initializer1.addActivityInstance("R", 1, -3);
		initializer1.addActivityInstance("S", 2, -4);

		TDMTestInitializer initializer2 = new TDMTestInitializer(process, validator, "b) R must be executed exactly once", -5);
		initializer2.addActivityInstance("Q", 0, -6);
		initializer2.addActivityInstance("R", 1, -7);
		initializer2.addExecutionAssertion("Q", 2, false, -100);
		initializer2.addExecutionAssertion("R", 3, false, -101);

		TDMTestInitializer initializer3 = new TDMTestInitializer(process, validator, "c) S must be executed at least once", -8);
		initializer3.addTerminationAssertion(0, 0, 3, 45, false, -9);
		initializer3.addTerminationAssertion(4, 7, true, -10);
		initializer3.addActivityInstance("Q", 0, -11);
		initializer3.addActivityInstance("R", 1, -13);
		initializer3.addActivityInstance("S", 2, -14);
		initializer3.addActivityInstance("U", 3, -15);

		TDMTestInitializer initializer4 = new TDMTestInitializer(process, validator, "d) P must be followed by T and W", -17);
		initializer4.addActivityInstance("Q", 0, -18);
		initializer4.addActivityInstance("R", 1, -19);
		initializer4.addActivityInstance("S", 2, -20);
		initializer4.addActivityInstance("U", 3, -21);
		initializer4.addActivityInstance("P", 5, -22);
		initializer4.addActivityInstance("W", 6, -23);
		initializer4.addActivityInstance("T", 7, -102);
		initializer4.addTerminationAssertion(6, 0, 7, 45, false, -24);
		initializer4.addTerminationAssertion(8, 11, true, -25);

		TDMTestInitializer initializer5 = new TDMTestInitializer(process, validator, "e) T may be the last activity", -26);
		initializer5.addTerminationAssertion(5, 8, true, -28);
		initializer5.addActivityInstance("Q", 0, -29);
		initializer5.addActivityInstance("R", 1, -31);
		initializer5.addActivityInstance("S", 2, -32);
		initializer5.addActivityInstance("U", 3, -33);
		initializer5.addActivityInstance("T", 4, -34);

		TDMTestInitializer initializer6 = new TDMTestInitializer(process, validator, "f) subtrace <Q,R,S> must be supported", -37);
		initializer6.addActivityInstance("Q", 0, -38);
		initializer6.addActivityInstance("R", 1, -39);
		initializer6.addActivityInstance("S", 2, -40);

		TDMTestInitializer initializer7 = new TDMTestInitializer(process, validator, "g) subtrace <R,S,U> must be supported", -41);
		initializer7.addActivityInstance("Q", 0, -103);
		initializer7.addActivityInstance("R", 2, -42);
		initializer7.addActivityInstance("S", 3, -43);
		initializer7.addActivityInstance("U", 4, -44);

		TDMTestInitializer initializer8 = new TDMTestInitializer(process, validator, "h) subtrace <R,P,W> must be supported", -45);
		initializer8.addActivityInstance("Q", 0, -46);

		initializer8.addActivityInstance("R", 4, -49);
		initializer8.addActivityInstance("P", 5, -50);
		initializer8.addActivityInstance("W", 6, -51);
	}
}
