package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.modeler.engine.IdValidator;
import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask2Initializer extends AbstractTDMInitializer {

	public ChangeTask2Initializer(TDMProcess process) {
		super(process);
	}

	@Override
	public void initialize() {
		IdValidator validator = new IdValidator();

		TDMTestInitializer initializer1 = new TDMTestInitializer(process, validator, "a) Amount of C <= 1", -1001);
		initializer1.addActivityInstance("B", 0, -2);
		initializer1.addActivityInstance("C", 1, -3);
		initializer1.addExecutionAssertion("C", 2, false, -4);

		TDMTestInitializer initializer2 = new TDMTestInitializer(process, validator, "b) E must be preceded by G", -5);
		initializer2.addExecutionAssertion("E", 0, 0, 1, 30, false, -6);
		initializer2.addActivityInstance("G", 1, -7);
		initializer2.addActivityInstance("F", 2, -8);
		initializer2.addActivityInstance("D", 3, -9);
		initializer2.addActivityInstance("E", 4, -10);

		TDMTestInitializer initializer3 = new TDMTestInitializer(process, validator, "c) F must be executed exactly once", -11);
		initializer3.addActivityInstance("G", 0, -12);
		initializer3.addActivityInstance("F", 1, -13);
		initializer3.addExecutionAssertion("F", 2, false, -14);
		initializer3.addExecutionAssertion("G", 2, false, -15);

		TDMTestInitializer initializer4 = new TDMTestInitializer(process, validator, "d) G must be followed by I", -16);
		initializer4.addActivityInstance("G", 1, -17);
		initializer4.addActivityInstance("F", 2, -18);
		initializer4.addActivityInstance("I", 3, -19);
		initializer4.addTerminationAssertion(0, 0, 3, 20, false, -20);
		initializer4.addTerminationAssertion(4, 8, true, -21);

		TDMTestInitializer initializer5 = new TDMTestInitializer(process, validator, "e) I can be the last activity ", -22);
		initializer5.addActivityInstance("G", 1, -23);
		initializer5.addActivityInstance("F", 2, -24);
		initializer5.addActivityInstance("I", 3, -25);
		initializer5.addTerminationAssertion(4, 8, true, -27);

		TDMTestInitializer initializer6 = new TDMTestInitializer(process, validator, "f) Subtrace <G,F,I> must be supported", -28);
		initializer6.addActivityInstance("G", 0, -29);
		initializer6.addActivityInstance("F", 1, -30);
		initializer6.addActivityInstance("I", 2, -31);

		TDMTestInitializer initializer7 = new TDMTestInitializer(process, validator, "g) Subtrace <E,D,I> must be supported", -32);
		initializer7.addActivityInstance("G", 0, -33);
		initializer7.addActivityInstance("F", 1, -34);
		initializer7.addActivityInstance("D", 2, -35);
		initializer7.addActivityInstance("E", 3, -36);
		initializer7.addActivityInstance("D", 4, -37);
		initializer7.addActivityInstance("I", 5, -38);

		TDMTestInitializer initializer8 = new TDMTestInitializer(process, validator, "h) Subtrace <E,B,D> must be supported", -39);
		initializer8.addActivityInstance("G", 0, -40);
		initializer8.addActivityInstance("F", 1, -41);
		initializer8.addActivityInstance("D", 2, -42);
		initializer8.addActivityInstance("E", 3, -43);
		initializer8.addActivityInstance("B", 4, -44);
		initializer8.addActivityInstance("D", 5, -45);
	}
}
