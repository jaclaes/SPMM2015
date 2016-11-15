package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.tdm.engine.TDMProcess;

public class ChangeTask35Initializer extends ChangeTask3XInitializer {

	public ChangeTask35Initializer(TDMProcess process) {
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
