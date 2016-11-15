package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class TDMReplicationDemo1Initializer extends AbstractTDMInitializer {

	public TDMReplicationDemo1Initializer(TDMProcess process) {
		super(process);
	}

	@Override
	public void initialize() {
		TDMTestInitializer initializer = new TDMTestInitializer(process, validator, "a) A must be executed before C can be executed");
		initializer.addActivityInstance("A", 0);
		initializer.addActivityInstance("B", 1);
		initializer.addActivityInstance("C", 2);
		initializer.addExecutionAssertion("C", 0, 0, 0, 45, false);
		initializer.addExecutionAssertion("C", 2, 0, 6, 0, true);

		TDMTestInitializer initializer2 = new TDMTestInitializer(process, validator, "b) B must be executed at least once");
		initializer2.addActivityInstance("A", 0);
		initializer2.addActivityInstance("D", 1);
		initializer2.addActivityInstance("B", 2);
		initializer2.addTerminationAssertion(3, 7, true);
		initializer2.addTerminationAssertion(0, 0, 2, 45, false);
	}

}
