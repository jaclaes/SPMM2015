package org.cheetahplatform.literatemodeling;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.literatemodeling.command.DocumentChangeCommand;
import org.cheetahplatform.literatemodeling.command.LiterateCommands;
import org.cheetahplatform.literatemodeling.command.TextChangeCommand;
import org.cheetahplatform.literatemodeling.model.ILiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class SourceEditorWrapper implements PropertyChangeListener {

	/**
	 * Internal Interface that combines Mouse and Key Listener
	 * 
	 * @author thomas
	 * 
	 */
	interface ICursorListener extends MouseListener, KeyListener {
		// nothing to add here
	};

	private final ManagedForm managedForm;
	private final Composite sourceEditorComposite;
	private Text processNameText;
	private Text processDescriptionText;
	private final LiterateModel model;
	private SourceViewer sourceViewer;
	private Font font;
	private List<ISelectionChangedListener> listeners;
	private ICursorListener cursorListener;

	/**
	 * @param sash
	 * @param model
	 */
	public SourceEditorWrapper(SashForm sash, final LiterateModel model) {
		this.model = model;
		listeners = new ArrayList<ISelectionChangedListener>();
		Composite composite = new Composite(sash, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 1;
		composite.setLayout(layout);
		managedForm = new ManagedForm(composite);
		ScrolledForm form = managedForm.getForm();
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		form.setText("Literate Model");
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());
		Composite theBody = form.getBody();
		theBody.setLayout(new GridLayout());

		createProcessAttributesSection(theBody);

		Section dialgoueDocumentSection = toolkit.createSection(theBody, ExpandableComposite.TITLE_BAR);
		dialgoueDocumentSection.setText("Dialogue Document");

		sourceEditorComposite = toolkit.createComposite(theBody);
		sourceEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sourceEditorComposite.setLayout(new FillLayout());

		VerticalRuler vRuler = new VerticalRuler(12);
		int styles = SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
		sourceViewer = new SourceViewer(sourceEditorComposite, vRuler, styles);
		sourceViewer.setInput(model.getDocument());
		sourceViewer.getTextWidget().setFont(font);
		sourceViewer.getTextWidget().setWordWrap(true);

		sourceViewer.addTextListener(new ITextListener() {

			@Override
			public void textChanged(TextEvent event) {
				DocumentEvent docEvent = event.getDocumentEvent();
				if (docEvent != null) {
					Command command = new DocumentChangeCommand(model, docEvent, true);
					command.execute();
				}

			}

		});

		sourceViewer.getTextWidget().addMouseListener(getCursorListener());
		sourceViewer.getTextWidget().addKeyListener(getCursorListener());

		sourceViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged();
			}

		});

		getProcessNameText().setText(model.getName());
		getProcessDescriptionText().setText(model.getDescription());
		addProcessNameChangedListener();
		addProcessDescriptionListener();
		model.addPropertyChangeListener(this);
	}

	/**
	 * @param action
	 */
	public void addAction(IAction action) {
		ScrolledForm form = managedForm.getForm();
		form.getToolBarManager().add(action);
		form.getToolBarManager().update(true);
	}

	private void addProcessDescriptionListener() {
		getProcessDescriptionText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String description = getProcessDescriptionText().getText();
				Command com = new TextChangeCommand(model, LiterateCommands.TEXT_DESCRIPTION, description, true);
				com.execute();
			}
		});
	}

	private void addProcessNameChangedListener() {
		getProcessNameText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String name = getProcessNameText().getText();
				Command com = new TextChangeCommand(model, LiterateCommands.TEXT_NAME, name, true);
				com.execute();
			}
		});
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	private void createProcessAttributesSection(Composite theBody) {

		FormToolkit toolkit = managedForm.getToolkit();
		Section attributesSection = toolkit.createSection(theBody, ExpandableComposite.TITLE_BAR);
		attributesSection.setText("Process Attributes");

		Composite header = toolkit.createComposite(theBody);
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		header.setLayout(new GridLayout(2, false));
		Label processNameLabel = toolkit.createLabel(header, "Process Name");
		processNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		processNameText = toolkit.createText(header, "");
		processNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label processDescriptionLabel = toolkit.createLabel(header, "Short Description");
		processDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		processDescriptionText = toolkit.createText(header, "", SWT.MULTI);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		// layoutData.heightHint = 30;
		processDescriptionText.setLayoutData(layoutData);

		// Create font with size 10
		Font initialFont = processNameText.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setHeight(10);
		}
		font = new Font(Display.getCurrent(), fontData);
		processNameText.setFont(font);
		processDescriptionText.setFont(font);
	}

	private void fireSelectionChanged(List<GraphElement> elements) {
		StructuredSelection selection = new StructuredSelection(elements);
		SelectionChangedEvent event = new SelectionChangedEvent(sourceViewer, selection);

		for (ISelectionChangedListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}

	protected ICursorListener getCursorListener() {
		if (cursorListener == null) {
			cursorListener = new ICursorListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					handleSelectionChanged();
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// nothing to do
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// nothing to do
				}

				@Override
				public void mouseDown(MouseEvent e) {
					// nothing to do
				}

				@Override
				public void mouseUp(MouseEvent e) {
					handleSelectionChanged();
				}
			};
		}
		return cursorListener;
	}

	/**
	 * Returns the processDescriptionText.
	 * 
	 * @return the processDescriptionText
	 */
	public Text getProcessDescriptionText() {
		return processDescriptionText;
	}

	/**
	 * Returns the processNameText.
	 * 
	 * @return the processNameText
	 */
	public Text getProcessNameText() {
		return processNameText;
	}

	public SourceViewer getSourceViewer() {
		return sourceViewer;
	}

	protected void handleSelectionChanged() {
		ITextSelection selection = (ITextSelection) sourceViewer.getSelection();
		List<GraphElement> associatedElements = new ArrayList<GraphElement>();

		for (ILiterateModelingAssociation assoc : model.getAssociations()) {

			if (selection.getOffset() <= assoc.getOffset() + assoc.getLength()
					&& selection.getOffset() + selection.getLength() > assoc.getOffset()) {
				associatedElements.addAll(assoc.getGraphElements());
			}
		}
		fireSelectionChanged(associatedElements);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LiterateCommands.TEXT_NAME)) {
			processNameText.setText(model.getName());
		} else if (evt.getPropertyName().equals(LiterateCommands.TEXT_DESCRIPTION)) {
			processDescriptionText.setText(model.getDescription());
		}

	}

}
