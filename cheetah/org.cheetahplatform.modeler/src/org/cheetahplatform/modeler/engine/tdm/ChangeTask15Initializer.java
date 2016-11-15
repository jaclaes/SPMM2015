package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask15Initializer extends ChangeTask1XInitializer {

	public ChangeTask15Initializer(TDMProcess process) {
		super(process);

		initializeX5Mapping();
	}

	@Override
	public void initialize() {
		createTest2();
		createTest1();
		createTest5();
		createTest4();
		createTest3();
	}

}
