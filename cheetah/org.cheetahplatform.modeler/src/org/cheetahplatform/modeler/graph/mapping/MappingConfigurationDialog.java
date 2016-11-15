package org.cheetahplatform.modeler.graph.mapping;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Assert;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.survey.Constants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         19.10.2009
 */
public class MappingConfigurationDialog extends TitleAreaDialog {

	private abstract class AbstractSelectionSensitiveAction extends Action implements ISelectionChangedListener {
		private StructuredViewer viewer;

		public AbstractSelectionSensitiveAction(StructuredViewer viewer) {
			Assert.isNotNull(viewer);
			this.viewer = viewer;
			viewer.addSelectionChangedListener(this);
			setEnabled(false);
		}

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			setEnabled(!viewer.getSelection().isEmpty());
		}
	}

	private class AddPossibleNameAction extends Action {

		public AddPossibleNameAction() {
			setText("Add Possible Name");
		}

		@Override
		public void run() {
			InputDialog dialog = new InputDialog(composite.getShell(), "Enter a possible name.",
					"Please enter a name that might be used for an activity.", "", new IInputValidator() {

						@Override
						public String isValid(String newText) {
							if (newText.trim().isEmpty())
								return "Please enter a name.";
							return null;
						}
					});

			if (dialog.open() == Window.OK) {
				getParagraph().addPossibleActivityName(dialog.getValue());
				composite.getPossibleNamesViewer().refresh();
			}
		}

	}

	private class ChangeParagraphColorAction extends AbstractSelectionSensitiveAction {
		public ChangeParagraphColorAction() {
			super(composite.getParagraphTableViewer());
			setText("Change Color");
		}

		@Override
		public void run() {
			ColorDialog dialog = new ColorDialog(composite.getShell());
			RGB color = dialog.open();
			if (color == null)
				return;

			getParagraph().setColor(color);
			composite.getParagraphTableViewer().refresh();
		}
	}

	private class RemovePossibleNameAction extends AbstractSelectionSensitiveAction {
		public RemovePossibleNameAction() {
			super(composite.getPossibleNamesViewer());
			setText("Remove Possible Name");
		}

		@Override
		public void run() {
			IStructuredSelection selection = (IStructuredSelection) composite.getPossibleNamesViewer().getSelection();
			if (selection.isEmpty()) {
				return;
			}

			String name = (String) selection.getFirstElement();
			getParagraph().removePossibleActivityName(name);
			composite.getPossibleNamesViewer().refresh();
		}
	}

	private MappingConfigurationComposite composite;

	private MappingConfigurationModel model;

	public MappingConfigurationDialog(Shell parentShell) {
		super(parentShell);
		model = new MappingConfigurationModel();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			try {
				ParagraphProvider.save();
			} catch (SQLException e) {
				MessageDialog.openError(composite.getShell(), "Error", "An error occured when saving the paragraphs.");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		parent.setBackground(Constants.BACKGROUND_COLOR);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		return super.createButtonBar(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(Constants.BACKGROUND_COLOR);
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		setTitle("Mapping Configuration");
		setMessage("By defining mapping rules some of the activities might be mapped automatically to the corresponding paragraphs.");

		composite = new MappingConfigurationComposite(container, SWT.NONE);
		ComboViewer processComboViewer = composite.getProcessComboViewer();
		processComboViewer.setContentProvider(new ArrayContentProvider());
		processComboViewer.setLabelProvider(new LabelProvider());
		processComboViewer.setInput(model.getProcesses());
		processComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				processSelectionChanged();
			}
		});

		TableViewer possibleNamesViewer = composite.getPossibleNamesViewer();
		possibleNamesViewer.setContentProvider(new ArrayContentProvider());
		possibleNamesViewer.setLabelProvider(new LabelProvider());
		MenuManager possibleNameViewerMenuManager = new MenuManager();
		possibleNameViewerMenuManager.add(new AddPossibleNameAction());
		possibleNameViewerMenuManager.add(new RemovePossibleNameAction());

		possibleNamesViewer.getTable().setMenu(possibleNameViewerMenuManager.createContextMenu(possibleNamesViewer.getTable()));

		TableViewer paragraphTableViewer = composite.getParagraphTableViewer();
		paragraphTableViewer.setContentProvider(new ArrayContentProvider());
		paragraphTableViewer.setLabelProvider(model.createParagraphLabelProvider());
		paragraphTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				paragraphSelectionChanged();
			}
		});

		MenuManager paragraphViewerMenuManager = new MenuManager();
		paragraphViewerMenuManager.add(new ChangeParagraphColorAction());
		paragraphTableViewer.getTable().setMenu(paragraphViewerMenuManager.createContextMenu(paragraphTableViewer.getTable()));

		composite.getParagraphTableViewer().getTable().addListener(SWT.EraseItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				paintParagraphBackgroundColor(event);
			}
		});

		if (!model.getProcesses().isEmpty()) {
			processComboViewer.setSelection(new StructuredSelection(model.getProcesses().iterator().next()));
		}

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}

	private Paragraph getParagraph() {
		IStructuredSelection selection = (IStructuredSelection) composite.getParagraphTableViewer().getSelection();
		if (selection.isEmpty()) {
			composite.getPossibleNamesViewer().setInput(Collections.emptyList());
			return null;
		}

		Paragraph paragraph = (Paragraph) selection.getFirstElement();
		return paragraph;
	}

	/**
	 * Paints the selection background for the reference line table.
	 * 
	 * @param event
	 *            the event send out for painting the background
	 */
	protected void paintParagraphBackgroundColor(Event event) {
		Paragraph paragraph = (Paragraph) event.item.getData();
		RGB color = paragraph.getColor();
		Color backgrounndColor = SWTResourceManager.getColor(color);
		event.gc.setBackground(backgrounndColor);
		event.gc.fillRectangle(event.x, event.y, event.width, event.height);

		if (paragraph.equals(getParagraph())) {
			event.gc.setForeground(SWTResourceManager.getColor(0, 0, 0));
			event.gc.drawRectangle(event.x, event.y, event.width - 1, event.height - 1);
		}
		event.detail &= ~SWT.SELECTED;
	}

	protected void paragraphSelectionChanged() {
		Paragraph paragraph = getParagraph();
		if (paragraph == null)
			return;
		composite.getPossibleNamesViewer().setInput(paragraph.getPossibleActivityNames());
	}

	protected void processSelectionChanged() {
		IStructuredSelection selection = (IStructuredSelection) composite.getProcessComboViewer().getSelection();
		if (selection.isEmpty()) {
			composite.getParagraphTableViewer().setInput(Collections.emptyList());
			return;
		}

		String process = (String) selection.getFirstElement();
		List<Paragraph> paragraphs = model.getParagraphs(process);
		composite.getParagraphTableViewer().setInput(paragraphs);
		if (!paragraphs.isEmpty()) {
			composite.getParagraphTableViewer().setSelection(new StructuredSelection(paragraphs.get(0)));
		}
	}
}
