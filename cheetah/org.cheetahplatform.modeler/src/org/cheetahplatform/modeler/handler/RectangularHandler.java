package org.cheetahplatform.modeler.handler;

import org.cheetahplatform.modeler.action.RectangularAction;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class RectangularHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new RectangularAction().run();
		return null;
	}

}
