package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.modeler.engine.IdValidator;
import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask1Initializer extends AbstractTDMInitializer {

	public ChangeTask1Initializer(TDMProcess process) {
		super(process);
	}

	@Override
	public void initialize() {
		IdValidator validator = new IdValidator();

		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, "a) M always first", -1001);
		initializer.addActivityInstance("M", 5, -2);
		initializer.addExecutionAssertion("A", 0, false, -3);
		initializer.addExecutionAssertion("C", 0, false, -4);
		initializer.addExecutionAssertion("Y", 1, false, -5);
		initializer.addExecutionAssertion("D", 1, false, -6);
		initializer.addExecutionAssertion("G", 2, false, -7);
		initializer.addExecutionAssertion("J", 2, false, -8);
		initializer.addExecutionAssertion("I", 3, false, -9);
		initializer.addExecutionAssertion("L", 3, false, -10);

		TDMTestInitializer initializer2 = new TDMTestInitializer(process, validator, "b) I always last", -11);
		initializer2.addActivityInstance("M", 0, -12);
		initializer2.addActivityInstance("Y", 1, -13);
		initializer2.addActivityInstance("C", 2, -14);
		initializer2.addActivityInstance("G", 3, -15);
		initializer2.addActivityInstance("D", 4, -16);
		initializer2.addActivityInstance("A", 5, -17);
		initializer2.addActivityInstance("J", 6, -18);
		initializer2.addActivityInstance("I", 7, -19);
		initializer2.addExecutionAssertion("M", 8, false, -20);
		initializer2.addExecutionAssertion("Y", 8, false, -21);
		initializer2.addExecutionAssertion("C", 9, false, -22);
		initializer2.addExecutionAssertion("G", 9, false, -23);
		initializer2.addExecutionAssertion("D", 10, false, -24);
		initializer2.addExecutionAssertion("A", 10, false, -25);
		initializer2.addExecutionAssertion("L", 11, false, -26);
		initializer2.addTerminationAssertion(8, 12, true, -27);
		initializer2.addTerminationAssertion(0, 8, false, -28);

		TDMTestInitializer initializer3 = new TDMTestInitializer(process, validator, "c) <G,D,A>", -29);
		initializer3.addActivityInstance("M", 0, -30);
		initializer3.addActivityInstance("G", 1, -31);
		initializer3.addExecutionAssertion("A", 2, false, -32);
		initializer3.addExecutionAssertion("C", 2, false, -33);
		initializer3.addExecutionAssertion("Y", 3, false, -34);
		initializer3.addExecutionAssertion("M", 3, false, -35);
		initializer3.addExecutionAssertion("G", 4, false, -36);
		initializer3.addExecutionAssertion("J", 4, false, -37);
		initializer3.addExecutionAssertion("I", 5, false, -38);
		initializer3.addExecutionAssertion("L", 5, false, -39);
		initializer3.addActivityInstance("D", 6, -40);
		initializer3.addExecutionAssertion("D", 7, false, -41);
		initializer3.addExecutionAssertion("C", 7, false, -42);
		initializer3.addExecutionAssertion("Y", 8, false, -43);
		initializer3.addExecutionAssertion("M", 8, false, -44);
		initializer3.addExecutionAssertion("G", 9, false, -45);
		initializer3.addExecutionAssertion("J", 9, false, -46);
		initializer3.addExecutionAssertion("I", 10, false, -47);
		initializer3.addExecutionAssertion("L", 10, false, -48);
		initializer3.addActivityInstance("A", 11, -49);

		TDMTestInitializer initializer4 = new TDMTestInitializer(process, validator, "d) A,C,D,G,M and Y cannot be executed after J", -50);
		initializer4.addActivityInstance("M", 0, -51);
		initializer4.addActivityInstance("C", 1, -52);
		initializer4.addActivityInstance("Y", 2, -53);
		initializer4.addActivityInstance("J", 3, -54);
		initializer4.addExecutionAssertion("A", 4, false, -55);
		initializer4.addExecutionAssertion("C", 4, false, -56);
		initializer4.addExecutionAssertion("D", 5, false, -57);
		initializer4.addExecutionAssertion("G", 5, false, -58);
		initializer4.addExecutionAssertion("M", 6, false, -59);
		initializer4.addExecutionAssertion("Y", 6, false, -60);

		TDMTestInitializer initializer5 = new TDMTestInitializer(process, validator, "e) C,G and Y must be preceded by M", -61);
		initializer5.addExecutionAssertion("C", 0, false, -62);
		initializer5.addExecutionAssertion("G", 0, false, -63);
		initializer5.addExecutionAssertion("Y", 1, false, -64);
		initializer5.addActivityInstance("M", 2, -65);
		initializer5.addExecutionAssertion("C", 3, true, -66);
		initializer5.addExecutionAssertion("G", 4, true, -67);
		initializer5.addExecutionAssertion("Y", 5, true, -68);

		TDMTestInitializer initializer6 = new TDMTestInitializer(process, validator, "f) J preceded by C and Y", -69);
		initializer6.addActivityInstance("M", 0, -70);
		initializer6.addActivityInstance("C", 1, -71);
		initializer6.addActivityInstance("Y", 2, -72);
		initializer6.addActivityInstance("J", 4, -73);
		initializer6.addExecutionAssertion("J", 0, 0, 2, 45, false, -74);
		initializer6.addExecutionAssertion("J", 4, 0, 6, 0, true, -75);

		TDMTestInitializer initializer7 = new TDMTestInitializer(process, validator, "g) L can be executed at most 5 times", -76);
		initializer7.addActivityInstance("M", 0, -77);
		initializer7.addActivityInstance("Y", 1, -78);
		initializer7.addActivityInstance("C", 2, -79);
		initializer7.addActivityInstance("J", 3, -80);
		initializer7.addActivityInstance("L", 4, -81);
		initializer7.addActivityInstance("L", 5, -82);
		initializer7.addActivityInstance("L", 6, -83);
		initializer7.addActivityInstance("L", 7, -84);
		initializer7.addActivityInstance("L", 8, -85);
		initializer7.addExecutionAssertion("L", 4, 0, 7, 45, true, -86);
		initializer7.addExecutionAssertion("L", 9, false, -87);
	}
}
