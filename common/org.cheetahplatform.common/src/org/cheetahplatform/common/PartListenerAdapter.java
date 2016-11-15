package org.cheetahplatform.common;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public abstract class PartListenerAdapter implements IPartListener {

	@Override
	public void partActivated(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// ignore
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// ignore
	}
}
