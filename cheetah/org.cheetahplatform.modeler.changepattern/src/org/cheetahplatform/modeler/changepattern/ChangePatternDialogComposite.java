package org.cheetahplatform.modeler.changepattern;

import org.cheetahplatform.common.ui.PlainMultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         16.06.2010
 */
public class ChangePatternDialogComposite extends Composite {

	private PlainMultiLineButton serialInsertButton;
	private PlainMultiLineButton parallelInsertButton;
	private PlainMultiLineButton deleteButton;
	private PlainMultiLineButton conditionalInsertButton;
	private PlainMultiLineButton updateConditionButton;
	private PlainMultiLineButton renameActivityButton;
	private PlainMultiLineButton embedInLoopButton;
	private PlainMultiLineButton embedInConditionalButton;
	private PlainMultiLineButton undoButton;
	private PlainMultiLineButton layoutButton;

	public ChangePatternDialogComposite(Composite parent, int style) {
		super(parent, style);

		setBackground(SWTResourceManager.getColor(255, 255, 255));
		setLayout(new GridLayout(2, true));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		setLayoutData(layoutData);
		GridData buttonLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false);

		Image image = null;
		Image disabledImage = null;
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_SERIAL_INSERT)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/serialinsert16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/serialinsertdisabled16.png");
			serialInsertButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Serial Insert", image, disabledImage);
			serialInsertButton.setLayoutData(buttonLayoutData);
			serialInsertButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_PARALLEL_INSERT)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/parallelinsert16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/parallelinsertdisabled16.png");
			parallelInsertButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Parallel Insert", image, disabledImage);
			parallelInsertButton.setLayoutData(buttonLayoutData);
			parallelInsertButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_CONDITIONAL_INSERT)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/conditionalinsert16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/conditionalinsertdisabled16.png");
			conditionalInsertButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Conditional Insert", image, disabledImage);
			conditionalInsertButton.setLayoutData(buttonLayoutData);
			conditionalInsertButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/conditionalinsert16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/conditionalinsertdisabled16.png");
			embedInConditionalButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Embed in Conditional Branch", image, disabledImage);
			embedInConditionalButton.setLayoutData(buttonLayoutData);
			embedInConditionalButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_EMBED_IN_LOOP)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/embedinloop16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/embedinloopdisabled16.png");
			embedInLoopButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Embed in Loop", image, disabledImage);
			embedInLoopButton.setLayoutData(buttonLayoutData);
			embedInLoopButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_UPDATE_CONDITION)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/updatecondition16.gif");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/updateconditiondisabled16.png");
			updateConditionButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Update Condition", image, disabledImage);
			updateConditionButton.setLayoutData(buttonLayoutData);
			updateConditionButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_RENAME_ACTIVITY)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/renameactivity16.gif");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/renameactivitydisabled16.png");
			renameActivityButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Rename Activity", image, disabledImage);
			renameActivityButton.setLayoutData(buttonLayoutData);
			renameActivityButton.setEnabled(false);
		}

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/delete16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/deletedisabled16.png");
			deleteButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Delete Process Fragment", image, disabledImage);
			deleteButton.setLayoutData(buttonLayoutData);
			deleteButton.setEnabled(false);
		}
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_UNDO)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/undo16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/undodisabled16.png");
			undoButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Undo", image, disabledImage);
			undoButton.setLayoutData(buttonLayoutData);
			undoButton.setEnabled(false);
		}
		
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.CHANGE_PATTERN_LAYOUT)) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/layout16.png");
			disabledImage = ResourceManager.getPluginImage(Activator.getDefault(), "img/changepattern/layout16.png");
			layoutButton = new PlainMultiLineButton(this, SWT.HORIZONTAL, "Layout", image, disabledImage);
			layoutButton.setLayoutData(buttonLayoutData);
			layoutButton.setEnabled(false);
		}
	}

	public PlainMultiLineButton getLayoutButton() {
		return layoutButton;
	}

	public PlainMultiLineButton getUndoButton() {
		return undoButton;
	}

	/**
	 * Returns the conditionalInsertButton.
	 * 
	 * @return the conditionalInsertButton
	 */
	public PlainMultiLineButton getConditionalInsertButton() {
		return conditionalInsertButton;
	}

	/**
	 * Returns the deleteButton.
	 * 
	 * @return the deleteButton
	 */
	public PlainMultiLineButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * Returns the embedInConditionalButton.
	 * 
	 * @return the embedInConditionalButton
	 */
	public PlainMultiLineButton getEmbedInConditionalButton() {
		return embedInConditionalButton;
	}

	/**
	 * Returns the embedInLoopButton.
	 * 
	 * @return the embedInLoopButton
	 */
	public PlainMultiLineButton getEmbedInLoopButton() {
		return embedInLoopButton;
	}

	/**
	 * Returns the parallelInsertButton.
	 * 
	 * @return the parallelInsertButton
	 */
	public PlainMultiLineButton getParallelInsertButton() {
		return parallelInsertButton;
	}

	/**
	 * Returns the renameActivityButton.
	 * 
	 * @return the renameActivityButton
	 */
	public PlainMultiLineButton getRenameActivityButton() {
		return renameActivityButton;
	}

	/**
	 * Returns the serialInsertButton.
	 * 
	 * @return the serialInsertButton
	 */
	public PlainMultiLineButton getSerialInsertButton() {
		return serialInsertButton;
	}

	/**
	 * Returns the updateConditionButton.
	 * 
	 * @return the updateConditionButton
	 */
	public PlainMultiLineButton getUpdateConditionButton() {
		return updateConditionButton;
	}
}
