package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask23Initializer extends ChangeTask2XInitializer {

	public ChangeTask23Initializer(TDMProcess process) {
		super(process);

		initializeX3Mapping();
	}

	@Override
	public void initialize() {
		createTest4();
		createTest2();
		createTest1();
		createTest5();
		createTest3();
	}

}
