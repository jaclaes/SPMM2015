package org.cheetahplatform.modeler.handler;

import org.cheetahplatform.modeler.action.AlignVerticallyAction;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class AlignVerticallyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AlignVerticallyAction().run();
		return null;
	}

}
