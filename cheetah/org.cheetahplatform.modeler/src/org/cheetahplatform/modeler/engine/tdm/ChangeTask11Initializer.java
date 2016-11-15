package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask11Initializer extends ChangeTask1XInitializer {

	public ChangeTask11Initializer(TDMProcess process) {
		super(process);

		initializeX1Mapping();
	}

	@Override
	public void initialize() {
		createTest1();
		createTest2();
		createTest3();
		createTest4();
		createTest5();
	}

}