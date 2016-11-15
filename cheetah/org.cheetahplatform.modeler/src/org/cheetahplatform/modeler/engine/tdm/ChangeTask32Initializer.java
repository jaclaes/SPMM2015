package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask32Initializer extends ChangeTask3XInitializer {

	public ChangeTask32Initializer(TDMProcess process) {
		super(process);

		initializeX2Mapping();
	}

	@Override
	public void initialize() {
		createTest5();
		createTest4();
		createTest3();
		createTest2();
		createTest1();
	}

}
