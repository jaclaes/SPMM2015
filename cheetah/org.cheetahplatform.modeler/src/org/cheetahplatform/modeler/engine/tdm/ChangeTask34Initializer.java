package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask34Initializer extends ChangeTask3XInitializer {

	public ChangeTask34Initializer(TDMProcess process) {
		super(process);

		initializeX4Mapping();
	}

	@Override
	public void initialize() {
		createTest3();
		createTest1();
		createTest2();
		createTest5();
		createTest4();
	}

}
