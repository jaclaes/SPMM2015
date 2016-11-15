package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.modeler.engine.ITDMInitializer;
import org.cheetahplatform.modeler.engine.IdValidator;
import org.cheetahplatform.tdm.engine.TDMProcess;

public abstract class AbstractTDMInitializer implements ITDMInitializer {
	protected TDMProcess process;
	protected IdValidator validator;

	protected AbstractTDMInitializer(TDMProcess process) {
		this.process = process;
		validator = new IdValidator();
		validator.validate(-1000);
	}

}
