package org.cheetahplatform.literatemodeling;

import org.cheetahplatform.literatemodeling.action.AddCommentAction;
import org.cheetahplatform.literatemodeling.action.CreateActivityAction;
import org.cheetahplatform.literatemodeling.action.GenerateReportAction;
import org.cheetahplatform.literatemodeling.action.SetConditionAction;
import org.cheetahplatform.literatemodeling.model.ILiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.views.AssociationsView;
import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.cheetahplatform.modeler.graph.IGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.editpart.EdgeLabelEditPart;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.swtdesigner.SWTResourceManager;

public class LiterateModelingEditor extends AbstractExperimentalGraphEditor {

	public static final String ID = "org.cheetahplatform.literatemodeling.literatemodelingeditor";

	public static IViewPart findView(String id) {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();

		return page.findView(id);
	}

	public static LiterateModelingEditor getActiveEditor() {
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();

		IEditorPart part = page.getActiveEditor();
		if (part instanceof LiterateModelingEditor) {
			return (LiterateModelingEditor) part;
		}
		return null;
	}

	private LiterateModel model;

	private SourceEditorWrapper sourceEditor;

	public LiterateModelingEditor() {
		super(EditorRegistry.BPMN);
	}

	public void addAssociationViewListener(AssociationsView associationsView) {
		associationsView.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				if (selection.isEmpty()) {
					return;
				}

				ScrollingGraphicalViewer viewer = getGraphViewer().getViewer();
				viewer.deselectAll();
				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				for (Object object : structuredSelection.toList()) {
					ILiterateModelingAssociation assoc = (ILiterateModelingAssociation) object;
					for (GraphElement element : assoc.getGraphElements()) {
						EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(element);
						viewer.reveal(editPart);
						viewer.appendSelection(editPart);
					}
				}
			}
		});
	}

	private void addProcessNameChangedListener() {
		sourceEditor.getProcessNameText().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String name = sourceEditor.getProcessNameText().getText();
				setPartName(name);
			}
		});
	}

	@Override
	protected IGraphicalGraphViewerAdvisor createGraphAdvisor() {
		return new LiterateModelingEditorViewerAdvisor(((GraphEditorInput) getEditorInput()).getGraph());
	}

	@Override
	public void createPartControl(Composite parent) {
		SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		sash.setBackground(new FormToolkit(Display.getDefault()).getColors().getColor(IFormColors.H_BOTTOM_KEYLINE2));

		// create GraphEditor Composite
		Composite composite = new Composite(sash, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.marginRight = 1;
		composite.setLayout(layout);
		ManagedForm managedForm = new ManagedForm(composite);
		managedForm.getForm().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		ScrolledForm graphForm = managedForm.getForm();
		graphForm.setText("Process Model");
		managedForm.getToolkit().decorateFormHeading(graphForm.getForm());
		graphForm.getBody().setLayout(new GridLayout());
		graphForm.getBody().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		super.createPartControl(graphForm.getBody());

		sourceEditor = new SourceEditorWrapper(sash, model);
		sourceEditor.addAction(new CreateActivityAction(getGraphViewer(), sourceEditor.getSourceViewer().getSelectionProvider(), model));
		sourceEditor.addAction(new SetConditionAction(getGraphViewer().getGraph(), sourceEditor.getSourceViewer().getSelectionProvider(),
				model));
		sourceEditor.addAction(new AddCommentAction(getGraphViewer().getGraph(), sourceEditor.getSourceViewer().getSelectionProvider(),
				model));
		sourceEditor.addAction(new GenerateReportAction(model, getGraphViewer()));
		sourceEditor.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();

				ScrollingGraphicalViewer viewer = getGraphViewer().getViewer();
				viewer.deselectAll();
				for (Object obj : selection.toList()) {
					EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(obj);
					viewer.reveal(editPart);
					viewer.appendSelection(editPart);
				}
			}
		});

		getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();

				sourceEditor.getSourceViewer().getTextWidget().setStyleRanges(new StyleRange[0]);

				if (selection.isEmpty()) {
					return;
				}

				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				for (Object object : structuredSelection.toList()) {
					IGenericEditPart editPart = (IGenericEditPart) object;
					IGenericModel graphElement = editPart.getModel();

					// get the parent of a EdgeLabelEditPart (a EdgeEditPart)
					// the EdgeEditPart may be associated with a text passage
					if (editPart instanceof EdgeLabelEditPart) {
						editPart = (IGenericEditPart) ((EdgeLabelEditPart) editPart).getParent();
						graphElement = editPart.getModel();
					}

					if (!(graphElement instanceof GraphElement)) {
						return;
					}

					if (model.isAssociationDefined((GraphElement) graphElement)) {
						Color background = LiterateModelingConstants.SELECTION_COLOR;
						Color foreground = SWTResourceManager.getColor(255, 255, 255);
						StyleRange styleRange = model.getStyleRange((GraphElement) graphElement, foreground, background, SWT.NORMAL);
						TextPresentation textPresentation = new TextPresentation();
						textPresentation.addStyleRange(styleRange);
						sourceEditor.getSourceViewer().changeTextPresentation(textPresentation, true);
					}
				}
			}
		});

		sourceEditor.getSourceViewer().addTextListener(new ITextListener() {
			@Override
			public void textChanged(TextEvent event) {
				model.udpateAssociations(event.getOffset(), event.getText().length(), event.getLength());
			}
		});

		addProcessNameChangedListener();
		setPartName(model.getName());
	}

	@Override
	public void dispose() {
		super.dispose();
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return;
		}

		IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return;
		}
		IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null) {
			return;
		}
		IViewPart associationsView = activePage.findView(AssociationsView.ID);
		if (associationsView != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(associationsView);
		}

	}

	public CommandStack getCommandStack() {
		return viewer.getViewer().getEditDomain().getCommandStack();
	}

	/**
	 * @return
	 * 
	 */
	public LiterateModel getLiterateModel() {
		return model;
	}

	@Override
	protected void initializeViewer() {
		super.initializeViewer();
		model = (LiterateModel) viewer.getGraph();
		GraphCommandStack stack = (GraphCommandStack) viewer.getViewer().getEditDomain().getCommandStack();
		stack.setGraph(viewer.getGraph());

	}

	@Override
	public boolean isDirty() {
		return false;
	}
}
