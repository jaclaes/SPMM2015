package org.cheetahplatform.modeler.graph.mapping;

import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RENAME;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.editpart.EdgeEditPart;
import org.cheetahplatform.modeler.graph.editpart.NodeEditPart;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

public class MapParagraphDialog extends Dialog {

	private class UpdateRunnable implements ICommandReplayerCallback {

		@Override
		public void processed(CommandDelegate command, boolean last) {
			composite.getCommandsViewer().refresh();
			ISelection selection = replayModel.getCurrentCommand();
			composite.getCommandsViewer().setSelection(selection);

			GraphElement graphElement = command.getCommand().getGraphElement();
			if (graphElement != null && graphElement.getDescriptor().getId().equals(EditorRegistry.BPMN_ACTIVITY)) {
				paragraphChanged(graphElement, command);
			}

			if (RENAME.equals(command.getType())) {
				paragraphChanged(graphElement, command);
			}

			model.loadEdgeConditionMapping();

			if (last) {
				updateSemanticalScore();
			} else {
				composite.getScoreLabel().setText("Calculating Semantical Score...");
			}
		}
	}

	private MapParagraphComposite composite;
	private final MapParagraphModel model;
	private ReplayModel replayModel;
	private ActivityDescriptor delegate;
	private EdgeDescriptor edgeDescriptorDelegate;
	private UpdateRunnable callBack;

	public MapParagraphDialog(Shell parentShell, ProcessInstanceDatabaseHandle graph) {
		super(parentShell);

		this.model = new MapParagraphModel(graph);
	}

	private void addDnDSupportForEdgeCondition(Label label, final EdgeCondition condition) {
		DragSource source = new DragSource(label, DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT | DND.DROP_MOVE);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragSetData(DragSourceEvent event) {
				model.setCurrentEdgeCondition(condition);
				event.data = condition.getName();
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				event.doit = true;
			}

		});
	}

	private void addDnDSupportForParagraph(Label label, final Paragraph paragraph) {
		DragSource source = new DragSource(label, DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT | DND.DROP_MOVE);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragSetData(DragSourceEvent event) {
				model.setCurrentParagraph(paragraph);
				event.data = paragraph.getDescription();
			}

			@Override
			public void dragStart(DragSourceEvent event) {
				event.doit = true;
			}

		});
	}

	private void addListener() {
		composite.getCommandsViewer().addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				jumpTo();
			}
		});
		getShell().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY).removeDelegate(delegate);
				EditorRegistry.getDescriptor(EditorRegistry.BPMN_START_EVENT).removeDelegate(delegate);
				EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW).removeDelegate(edgeDescriptorDelegate);
			}
		});
		composite.getForwardButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				jumpToNextManualIntervention();
			}
		});
	}

	@Override
	public boolean close() {
		replayModel.removeCallbackListener(callBack);
		return super.close();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite realParent = (Composite) super.createDialogArea(parent);
		composite = new MapParagraphComposite(realParent, SWT.NONE);
		initialize();
		getShell().setText("Paragraph Mapping: " + model.getHandle().getId());
		getShell().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				model.dispose();
			}
		});

		return realParent;
	}

	protected void droppedEdgeConditionOnEditPart(EdgeEditPart editPart) {
		EdgeCondition currentEdgeCondition = model.getCurrentEdgeCondition();
		if (currentEdgeCondition == null) {
			MessageDialog.openWarning(getShell(), "Invalid Drop", "Paragraphs can only be dropped on nodes.");
			return; // dropped a edge condition
		}

		Edge edge = editPart.getModel();
		RGB color = currentEdgeCondition.getColor();
		if (currentEdgeCondition.equals(MapParagraphModel.REMOVE_EDGE_CONDITION)) {
			color = null;
		}

		edge.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, color);
		model.setEdgeCondition(edge, currentEdgeCondition);
		updateSemanticalScore();
	}

	private void droppedParagraphOnEditPart(NodeEditPart editPart) {
		if (model.getCurrentParagraph() == null) {
			MessageDialog.openWarning(getShell(), "Invalid Drop", "Edge conditions cannot be dropped on nodes.");
			return; // dropped a edge condition
		}

		Node node = editPart.getModel();
		RGB color = model.getCurrentParagraph().getColor();
		if (model.getCurrentParagraph().equals(MapParagraphModel.REMOVE_PARAGRAPH)) {
			color = null;
		}

		node.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, color);
		model.setParagraph(node.getId(), model.getCurrentParagraph(), (CommandDelegate) replayModel.getCurrentCommand().getFirstElement(),
				replayModel.getCommands());
		composite.getCommandsViewer().refresh();
		updateSemanticalScore();
	}

	@Override
	protected Point getInitialSize() {
		Rectangle bounds = getShell().getDisplay().getBounds();
		return new Point(700, bounds.height);
	}

	@Override
	protected int getShellStyle() {
		return SWT.SHELL_TRIM;
	}

	private void initialize() {
		String type = model.getGraphType();
		Graph graph = AbstractModelingActivity.loadInitialGraph(model.getProcess(), EditorRegistry.BPMN);
		Process process = model.getProcess();
		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS, process.getId()));
		GraphEditor editor = (GraphEditor) EditorRegistry.openEditor(type, graph, attributes, process);

		initializeParagraphMapping();
		initializeEdgeConditionMapping();

		initializeCommandsViewer(editor, graph);
		jumpToNextManualIntervention();
		addListener();
	}

	private void initializeCommandsViewer(GraphEditor editor, Graph graph) {
		CommandStack stack = editor.getViewer().getEditDomain().getCommandStack();
		replayModel = new ReplayModel(stack, model.getHandle(), graph);
		model.setReplayModel(replayModel);
		callBack = new UpdateRunnable();
		replayModel.addCallbackListener(callBack);

		TreeViewer viewer = composite.getCommandsViewer();
		viewer.setContentProvider(replayModel.createContentProvider());
		viewer.setLabelProvider(model.createLabelProvider(replayModel.createLabelProvider()));
		viewer.setInput(replayModel.getCommands());
	}

	protected void initializeEdgeConditionMapping() {
		for (EdgeCondition condition : model.getEdgeConditions()) {
			Label label = new Label(composite.getEdgeConditionsComposite(), SWT.BORDER | SWT.WRAP);
			label.setText(condition.getName());
			label.setBackground(SWTResourceManager.getColor(condition.getColor()));
			label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			addDnDSupportForEdgeCondition(label, condition);
		}

		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(EditorRegistry.BPMN_SEQUENCE_FLOW);
		edgeDescriptorDelegate = new EdgeDescriptor(null, null, null) {
			@Override
			public void dropped(DropTargetEvent event, AbstractGraphicalEditPart targetEditPart) {
				droppedEdgeConditionOnEditPart((EdgeEditPart) targetEditPart);
			}

		};
		descriptor.addDelegate(edgeDescriptorDelegate);
	}

	protected void initializeParagraphMapping() {
		List<Paragraph> paragraphs = model.getParagraphs();
		for (Paragraph paragraph : paragraphs) {
			Label label = new Label(composite.getParagraphsComposite(), SWT.BORDER | SWT.WRAP);
			label.setText(paragraph.getDescription());
			label.setBackground(SWTResourceManager.getColor(paragraph.getColor()));
			label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			addDnDSupportForParagraph(label, paragraph);
		}

		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(EditorRegistry.BPMN_ACTIVITY);
		delegate = new ActivityDescriptor() {
			@Override
			public void dropped(DropTargetEvent event, AbstractGraphicalEditPart targetEditPart) {
				droppedParagraphOnEditPart((NodeEditPart) targetEditPart);
			}

		};
		descriptor.addDelegate(delegate);
		EditorRegistry.getDescriptor(EditorRegistry.BPMN_START_EVENT).addDelegate(delegate);
	}

	protected void jumpTo() {
		IStructuredSelection selection = (IStructuredSelection) composite.getCommandsViewer().getSelection();
		try {
			replayModel.executeTo(selection);
		} catch (Exception e) {
			MessageDialog.openError(getShell(), "Error", e.getMessage());
		}
	}

	protected void jumpToNextManualIntervention() {
		try {
			model.jumpToNextManualIntervention();
		} catch (Exception e) {
			Activator.logError(e.getMessage(), e);
			MessageDialog.openError(getShell(), "Error", e.getMessage());
		}
	}

	private void paragraphChanged(GraphElement element, CommandDelegate command) {
		model.paragraphChanged(element, command);
	}

	protected void updateSemanticalScore() {
		composite.getScoreLabel().setText(model.getSemanticalEvaluationScore());
	}
}
