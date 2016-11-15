package org.cheetahplatform.tdm.engine;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;

public interface ITDMStep {
	void execute(DeclarativeProcessInstance instance) throws TDMTestFailedException;

	ITDMFailureCause getFailure();

	Object getSource();

	DateTime getStartTime();
}
