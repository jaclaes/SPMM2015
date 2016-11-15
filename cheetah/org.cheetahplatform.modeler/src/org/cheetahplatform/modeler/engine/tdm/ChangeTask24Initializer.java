package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask24Initializer extends ChangeTask2XInitializer {

	public ChangeTask24Initializer(TDMProcess process) {
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
