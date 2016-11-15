package org.cheetahplatform.modeler.handler;

import org.cheetahplatform.modeler.action.LayoutAction;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class LayoutHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new LayoutAction().run();
		return null;
	}

}
