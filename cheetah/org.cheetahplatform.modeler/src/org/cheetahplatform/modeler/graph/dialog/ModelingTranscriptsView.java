package org.cheetahplatform.modeler.graph.dialog;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.model.ModelingTranscript;
import org.cheetahplatform.modeler.importer.ModelingTranscriptModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.swtdesigner.SWTResourceManager;

public class ModelingTranscriptsView extends ViewPart {
	private class TranscriptsLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
		@Override
		public Color getBackground(Object element) {
			ModelingTranscript transcript = (ModelingTranscript) element;
			IStructuredSelection currentCommand = replayModel.getCurrentCommand();
			IStructuredSelection lastExecutedCommand = replayModel.getLastExecutedCommand();

			if (currentCommand.isEmpty()) {
				// we are done executing
				return SWTResourceManager.getColor(255, 177, 177);
			}

			CommandDelegate commandToExecute = (CommandDelegate) currentCommand.getFirstElement();
			Date timestamp = commandToExecute.getAuditTrailEntry().getTimestamp();
			if (transcript.getStartTime().getTime() - timestamp.getTime() > 0) {
				return SWTResourceManager.getColor(255, 255, 255);
			}

			if (!lastExecutedCommand.isEmpty()) {
				CommandDelegate lastExecuted = (CommandDelegate) lastExecutedCommand.getFirstElement();
				if (transcript.getEndTime().getTime() - lastExecuted.getAuditTrailEntry().getTimestamp().getTime() < 0) {
					return SWTResourceManager.getColor(255, 177, 177);
				}
			}
			return SWTResourceManager.getColor(169, 255, 171);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ModelingTranscript transcript = (ModelingTranscript) element;
			SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
			switch (columnIndex) {
			case 0:
				Date date = transcript.getStartTime();
				return format.format(date);
			case 1:
				Date endTime = transcript.getEndTime();
				return format.format(endTime);
			case 2:
				return transcript.getOriginator();
			case 3:
				return transcript.getText();
			}
			throw new IllegalStateException("Illegal column index");
		}

		@Override
		public Color getForeground(Object arg0) {
			return null;
		}
	}

	private class UpdateRunnable implements ICommandReplayerCallback {
		@Override
		public void processed(CommandDelegate command, boolean last) {
			refresh();
		}
	}

	public static final String ID = "org.cheetahplatform.view.modelingtranscriptview";

	private ReplayModel replayModel;

	private ModelingTranscriptModel transcriptModel;

	private ModelingTranscriptsComposite composite;

	private UpdateRunnable callBack;

	private void addEditTranscriptsListener() {
		composite.getMoveBackwardButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveBackward();
			}
		});
		composite.getMoveForwardButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveForward();
			}
		});

		composite.getEditModelingTranscriptButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editModelingTranscript();
			}
		});

		composite.getTranscriptsTableViewer().addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				editModelingTranscript();
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		setPartName("Modeling Transcripts");

		composite = new ModelingTranscriptsComposite(parent, SWT.NONE);
		TableViewer tableViewer = composite.getTranscriptsTableViewer();

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TranscriptsLabelProvider());

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.EDIT_MODELING_TRANSCRIPTS)) {
			addEditTranscriptsListener();
		}

		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		this.replayModel = (ReplayModel) selection.getFirstElement();
		callBack = new UpdateRunnable();
		replayModel.addCallbackListener(callBack);
		try {
			transcriptModel = new ModelingTranscriptModel(replayModel.getProcessInstanceDatabaseHandle());
			composite.getTranscriptsTableViewer().setInput(transcriptModel.getModelingTranscripts());
			updateButtons(true);
		} catch (SQLException e) {
			updateButtons(false);
		}
	}

	@Override
	public void dispose() {
		replayModel.removeCallbackListener(callBack);
		super.dispose();
	}

	protected void editModelingTranscript() {
		IStructuredSelection selection = (IStructuredSelection) composite.getTranscriptsTableViewer().getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(getViewSite().getShell(), "No Transcript Selected", "Please select a transcript to be edited.");
			return;
		}
		ModelingTranscript transcript = (ModelingTranscript) selection.getFirstElement();

		ModelingTranscriptEditDialog dialog = new ModelingTranscriptEditDialog(getViewSite().getShell(), transcript);
		if (dialog.open() == Window.OK) {
			try {
				transcriptModel.saveTranscript(transcript);
				transcriptModel.sortTranscripts();
				refresh();
			} catch (SQLException e) {
				handleSQLException(e);
			}
		}
	}

	private void handleSQLException(SQLException e) {
		MessageDialog.openError(getViewSite().getShell(), "Error", "Unable to save modeling transcript.");
		Activator.logError("Unable to store transcript.", e);
	}

	protected void moveBackward() {
		try {
			transcriptModel.changeOffset(1);
			refresh();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	protected void moveForward() {
		try {
			transcriptModel.changeOffset(-1);
			refresh();
		} catch (SQLException e) {
			handleSQLException(e);
		}
	}

	public void refresh() {
		composite.getTranscriptsTableViewer().refresh();
	}

	@Override
	public void setFocus() {
		composite.getTranscriptsTableViewer().getTable().setFocus();
	}

	private void updateButtons(boolean state) {
		composite.getMoveBackwardButton().setEnabled(state);
		composite.getMoveForwardButton().setEnabled(state);
		composite.getEditModelingTranscriptButton().setEnabled(state);
	}
}
