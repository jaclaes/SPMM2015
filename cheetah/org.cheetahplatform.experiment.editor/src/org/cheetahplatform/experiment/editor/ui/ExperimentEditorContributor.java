package org.cheetahplatform.experiment.editor.ui;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

public class ExperimentEditorContributor extends ActionBarContributor {

	/**
	 * Create actions managed by this contributor.
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		IWorkbenchWindow iww = getPage().getWorkbenchWindow(); 
		
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction((RetargetAction)ActionFactory.COPY.create(iww));
		addRetargetAction((RetargetAction)ActionFactory.PASTE.create(iww));
		
		
	}

	/**
	 * Add actions to the given toolbar.
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager toolBarManager) {
	
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		// currently none
	}
	
	

}
