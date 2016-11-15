package org.cheetahplatform.modeler.handler;

import org.cheetahplatform.modeler.action.AlignHorizontallyAction;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class AlignHorizontallyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AlignHorizontallyAction().run();
		return null;
	}

}
