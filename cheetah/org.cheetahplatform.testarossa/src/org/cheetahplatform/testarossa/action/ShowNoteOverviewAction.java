package org.cheetahplatform.testarossa.action;

import org.eclipse.swt.program.Program;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.12.2009
 */
public class ShowNoteOverviewAction extends ProcessInstanceSpecificAction {

	public static final String id = "org.cheetahplatform.testarossa.action.ShowNoteOverviewAction";

	public ShowNoteOverviewAction() {
		super(id, "img/note_overview.gif", "img/note_overview_disabled.png");
		setToolTipText("Show Note Overview");
	}

	@Override
	public void run() {
		NoteOverviewCreator noteOverviewCreator = new NoteOverviewCreator();
		Program.launch(noteOverviewCreator.toHtmlFile().getAbsolutePath());
	}
}
