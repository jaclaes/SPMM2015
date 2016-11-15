package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class TDMReplicationDemo2Initializer extends AbstractTDMInitializer {

	public TDMReplicationDemo2Initializer(TDMProcess process) {
		super(process);
	}

	@Override
	public void initialize() {
		TDMTestInitializer initializer1 = new TDMTestInitializer(process, validator, "a) Write article before Submit article");
		initializer1.addActivityInstance("Write article", 0, 2, validator.getLowestId() - 1);
		initializer1.addActivityInstance("Submit article", 2);
		initializer1.addExecutionAssertion("Submit article", 0, 0, 1, 15, false);
		initializer1.addTerminationAssertion(0, 0, 2, 45, false);
		initializer1.addTerminationAssertion(3, 0, 4, 0, true);

		TDMTestInitializer initializer2 = new TDMTestInitializer(process, validator,
				"b) <Write, Submit, Reject, Rewrite> is a valid trace; No acceptance after rejection");
		initializer2.addActivityInstance("Write article", 0);
		initializer2.addActivityInstance("Submit article", 1);
		initializer2.addActivityInstance("Article rejected", 2);
		initializer2.addActivityInstance("Rewrite article", 3);
		initializer2.addExecutionAssertion("Article accepted", 3, 0, 6, 0, false);
	}

}
