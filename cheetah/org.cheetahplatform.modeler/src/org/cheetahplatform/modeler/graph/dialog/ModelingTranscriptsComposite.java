package org.cheetahplatform.modeler.graph.dialog;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class ModelingTranscriptsComposite extends Composite {

	private TableViewer transcriptsTableViewer;
	private PlainMultiLineButton moveForwardButton;
	private PlainMultiLineButton moveBackwardButton;
	private PlainMultiLineButton editModelingTranscriptButton;

	public ModelingTranscriptsComposite(Composite parent, int style) {
		super(parent, style);
		setBackground(org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setBackgroundMode(SWT.INHERIT_FORCE);
		setLayout(new GridLayout());
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite infoComposite = new Composite(this, SWT.NONE);
		infoComposite.setLayout(new GridLayout());
		infoComposite.setBackground(SWTResourceManager.getColor(255, 255, 255));
		infoComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Label label = new Label(infoComposite, SWT.NONE);
		label.setText("Red = Transcript happened before the last executed modeling step.\nGreen = Transcript happened after the last executed and prior to the next modeling step.\nWhite = Transcript happened after the next modeling step.");
		label.setBackground(SWTResourceManager.getColor(255, 255, 255));
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Group editModelingTranscriptsComposite = new Group(this, SWT.NONE);
		editModelingTranscriptsComposite.setText("Edit Modeling Transcripts");
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		layoutData.exclude = !CheetahPlatformConfigurator.getBoolean(IConfiguration.EDIT_MODELING_TRANSCRIPTS);
		editModelingTranscriptsComposite.setLayoutData(layoutData);
		editModelingTranscriptsComposite.setLayout(new GridLayout(3, true));

		Image upImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/up24.png");
		Image downImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/down24.png");
		moveForwardButton = new PlainMultiLineButton(editModelingTranscriptsComposite, SWT.NONE, "Decrease Timestamps", downImage,
				downImage);
		moveForwardButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		moveBackwardButton = new PlainMultiLineButton(editModelingTranscriptsComposite, SWT.NONE, "Increase Timestamps", upImage, upImage);
		moveBackwardButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Image editImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/edit24.png");
		editModelingTranscriptButton = new PlainMultiLineButton(editModelingTranscriptsComposite, SWT.NONE, "Edit Transcript", editImage,
				editImage);
		editModelingTranscriptButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Composite composite = new Composite(this, SWT.NONE);
		TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		transcriptsTableViewer = new TableViewer(composite, SWT.FULL_SELECTION);
		Table table = transcriptsTableViewer.getTable();
		transcriptsTableViewer.getTable().setHeaderVisible(true);
		transcriptsTableViewer.getTable().setLinesVisible(true);
		transcriptsTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumn timeStampColumn = new TableColumn(transcriptsTableViewer.getTable(), SWT.NONE);
		layout.setColumnData(timeStampColumn, new ColumnWeightData(10));
		timeStampColumn.setText("Starttime");

		TableColumn tblclmnEndtime = new TableColumn(table, SWT.NONE);
		layout.setColumnData(tblclmnEndtime, new ColumnWeightData(10));
		tblclmnEndtime.setText("Endtime");

		TableColumn originatorColumn = new TableColumn(transcriptsTableViewer.getTable(), SWT.NONE);
		layout.setColumnData(originatorColumn, new ColumnWeightData(10));
		originatorColumn.setText("Originator");

		TableColumn textColumn = new TableColumn(transcriptsTableViewer.getTable(), SWT.NONE);
		layout.setColumnData(textColumn, new ColumnWeightData(70));
		textColumn.setText("Transcript");
	}

	public PlainMultiLineButton getEditModelingTranscriptButton() {
		return editModelingTranscriptButton;
	}

	public PlainMultiLineButton getMoveBackwardButton() {
		return moveBackwardButton;
	}

	public PlainMultiLineButton getMoveForwardButton() {
		return moveForwardButton;
	}

	public TableViewer getTranscriptsTableViewer() {
		return transcriptsTableViewer;
	}
}
