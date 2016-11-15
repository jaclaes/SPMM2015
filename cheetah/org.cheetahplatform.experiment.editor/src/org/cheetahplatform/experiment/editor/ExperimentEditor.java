package org.cheetahplatform.experiment.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.experiment.editor.desc.BPMNDescriptor;
import org.cheetahplatform.experiment.editor.desc.ChangePatternDescriptor;
import org.cheetahplatform.experiment.editor.desc.ComprehensionDescriptor;
import org.cheetahplatform.experiment.editor.desc.DecSerFlowDescriptor;
import org.cheetahplatform.experiment.editor.desc.FeedbackDescriptor;
import org.cheetahplatform.experiment.editor.desc.MessageDescriptor;
import org.cheetahplatform.experiment.editor.desc.ModelListDescriptor;
import org.cheetahplatform.experiment.editor.desc.QuestionListDescriptor;
import org.cheetahplatform.experiment.editor.desc.SurveyDescriptor;
import org.cheetahplatform.experiment.editor.desc.TutorialDescriptor;
import org.cheetahplatform.experiment.editor.prop.NameHelper;
import org.cheetahplatform.experiment.editor.ui.CopyElementAction;
import org.cheetahplatform.experiment.editor.ui.PasteElementAction;
import org.cheetahplatform.experiment.editor.views.IValidator;
import org.cheetahplatform.experiment.editor.views.Message;
import org.cheetahplatform.experiment.editor.views.ValidationViewModel;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.experiment.editor.xml.ExpEditorMarshallerException;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.GraphEditor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.IGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class ExperimentEditor extends GraphEditor implements CommandStackListener, ISelectionListener, IValidator{

	public static final String ID = "org.cheetahplatform.experiment.ExperimentEditor";  //$NON-NLS-1$
	public static final String SAVEASCOMMAND = "org.cheetahplatform.experiment.editor.ui.SaveAs";  //$NON-NLS-1$
	
	private EditDomain editDomain;
	private ActionRegistry actionRegistry;
	private List<String> selectionActions = new ArrayList<String>();
	private List<String> stackActions = new ArrayList<String>();
	private List<String> propertyActions = new ArrayList<String>();
	
	private List<INodeDescriptor> nodeDescriptors;
	private List<IEdgeDescriptor> edgeDescriptors;
	private NameHelper nameHelper;
	
	
	private class ExperimentEditorViewer extends GraphicalGraphViewerWithFlyoutPalette {
		public ExperimentEditorViewer(Composite parent,
				IGraphicalGraphViewerAdvisor advisor) {
			super(parent, advisor);			
		}
		@Override
		protected EditDomain createEditDomain() {
			return getEditDomain();
		}	
		
	}
	
	public ExperimentEditor(){
		editDomain = new GraphEditDomain();
		nameHelper = new NameHelper();	
	}
	
	@Override
	public void createPartControl(Composite parent) {
		IGraphicalGraphViewerAdvisor advisor = createGraphAdvisor();

		viewer = new ExperimentEditorViewer(parent, advisor);
		initializeViewer();
	}

	@Override
	protected void initializeViewer() {
		super.initializeViewer();
		getSite().setSelectionProvider(getViewer());
	}

	/**
	 * Lazily creates and returns the action registry.
	 * @return the action registry
	 */
	protected ActionRegistry getActionRegistry() {
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}
	
	/**
	 * Creates actions for this editor.  Subclasses should override this method to create
	 * and register actions with the {@link ActionRegistry}.
	 */
	protected void createActions() {
		ActionRegistry registry = getActionRegistry();
		IAction action;
		
		action = new UndoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());
		
		action = new RedoAction(this);
		registry.registerAction(action);
		getStackActions().add(action.getId());
		
		action = new SelectAllAction(this);
		registry.registerAction(action);
		
		action = new DeleteAction((IWorkbenchPart)this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new SaveAction(this);
		registry.registerAction(action);
		getPropertyActions().add(action.getId());
		
		action = new CopyElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new PasteElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		registry.registerAction(new PrintAction(this));
	}
	
	/**
	 * Returns the list of {@link IAction IActions} dependant on property changes in the
	 * Editor.  These actions should implement the {@link UpdateAction} interface so that they
	 * can be updated in response to property changes.  An example is the "Save" action.
	 * @return the list of property-dependant actions
	 */
	protected List<String> getPropertyActions() {
		return propertyActions;
	}
	
	/**
	 * Returns the list of <em>IDs</em> of Actions that are dependant on changes in the
	 * workbench's {@link ISelectionService}. The associated Actions can be found in the
	 * action registry. Such actions should implement the {@link UpdateAction} interface so
	 * that they can be updated in response to selection changes.
	 * @see #updateActions(List)
	 * @return the list of selection-dependant action IDs
	 */
	protected List<String> getSelectionActions() {
		return selectionActions;
	}
	
	/**
	 * Returns the list of <em>IDs</em> of Actions that are dependant on the CommmandStack's
	 * state. The associated Actions can be found in the action registry. These actions should
	 * implement the {@link UpdateAction} interface so that they can be updated in response to
	 * command stack changes.  An example is the "undo" action.
	 * @return the list of stack-dependant action IDs
	 */
	protected List<String> getStackActions() {
		return stackActions;
	}
	
	@Override
	protected List<IEdgeDescriptor> createEdgeDescriptors() {
		if (edgeDescriptors == null){
			edgeDescriptors = EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN);
			
		}	
		return edgeDescriptors;	
	}

	@Override
	protected List<INodeDescriptor> createNodeDescriptors() {
		if (nodeDescriptors == null){
			nodeDescriptors = new ArrayList<INodeDescriptor>();
			nodeDescriptors.add((INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_XOR_GATEWAY));
			nodeDescriptors.add((INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_START_EVENT));
			nodeDescriptors.add((INodeDescriptor) EditorRegistry.getDescriptor(EditorRegistry.BPMN_END_EVENT));
			nodeDescriptors.add(new TutorialDescriptor());
			nodeDescriptors.add(new BPMNDescriptor());
			nodeDescriptors.add(new ChangePatternDescriptor());
			nodeDescriptors.add(new DecSerFlowDescriptor());
			nodeDescriptors.add(new ComprehensionDescriptor());
			nodeDescriptors.add(new SurveyDescriptor());
			nodeDescriptors.add(new QuestionListDescriptor());
			nodeDescriptors.add(new ModelListDescriptor());
			nodeDescriptors.add(new FeedbackDescriptor());
			nodeDescriptors.add(new MessageDescriptor());
		}
		return nodeDescriptors;
	}	
	
	@Override
	public boolean isDirty() {
		return getCommandStack().isDirty();
	}
	
	protected EditDomain getEditDomain(){
		return editDomain;
		
	}
	
	public CommandStack getCommandStack() {
		return getEditDomain().getCommandStack();
	}
	
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		
		getCommandStack().addCommandStackListener(this);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		initializeActionRegistry();
		setEditorTitle();
	}
		
	protected void setEditorTitle(){
		ExperimentEditorInput in = (ExperimentEditorInput) getEditorInput();
		
		if (in.getFilePath() != null) {
			String path = in.getFilePath();
			setPartName(path.substring(path.lastIndexOf(File.separatorChar)+1, path.length()-4));
			setContentDescription(path);
		} else {
			String newName = nameHelper.next(Messages.UNTITLED);
			setPartName(newName);
			setContentDescription(newName);
		}
	}

	/** 
	 * Returns the adapter for the specified key.
	 * 
	 * <P><EM>IMPORTANT</EM> certain requests, such as the property sheet, may be made before
	 * or after {@link #createPartControl(Composite)} is called. The order is unspecified by
	 * the Workbench.
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class type) {
		if (type == CommandStack.class)
			return getCommandStack();
		if (type == ActionRegistry.class)
			return getActionRegistry();
		if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class) {
			PropertySheetPage page = new PropertySheetPage();
			page.setRootEntry(new UndoablePropertySheetEntry(getCommandStack()));
			return page;
		}
		if (type == IPropertySheetPage.class){
			return super.getAdapter(type);
		}
		return super.getAdapter(type);
	}
	
	/**
	 * Initializes the ActionRegistry.  This registry may be used by {@link
	 * ActionBarContributor ActionBarContributors} and/or {@link ContextMenuProvider
	 * ContextMenuProviders}.
	 * <P>This method may be called on Editor creation, or lazily the first time {@link
	 * #getActionRegistry()} is called.
	 */
	protected void initializeActionRegistry() {
		createActions();
		updateActions(propertyActions);
		updateActions(stackActions);
	}
	
	protected boolean checkForErrors() {
		boolean res = true;

		boolean containsErrors = false;
		for (Message m : validate()){
			if (m.getType() == Message.Type.ERROR) {
				containsErrors = true;
				break;
			}
		}
		
		if (containsErrors){
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			res = MessageDialog.openQuestion(shell, Messages.ERROR_IN_CONFIG,
						Messages.ERROR_IN_CONFIG_LONG);
		}
		return res;
	}
	

	@Override
	public void doSave(IProgressMonitor monitor) {
		ExperimentEditorInput input = (ExperimentEditorInput)getEditorInput();
		if (input.getFilePath() != null){	
			if (checkForErrors()) {
				ExperimentEditorMarshaller marsh = new ExperimentEditorMarshaller();
				try {
					marsh.marshallToFile(input.getGraph(), input.getFilePath());
				} catch (ExpEditorMarshallerException ex) {
					Activator.log(IStatus.ERROR, 
							 Messages.UNABLE_TO_SAVE, ex);
				}
				getCommandStack().markSaveLocation();
				setEditorTitle();			
				ValidationViewModel.getInstance().update((ExperimentGraph)getGraph(), validate());
			}
		} else {
			doSaveAs();
		}
	}

	@Override
	public void doSaveAs() {
		if (checkForErrors()) {
			final IHandlerService handlerService = (IHandlerService) PlatformUI
					.getWorkbench().getService(IHandlerService.class);
			try {
				handlerService.executeCommand(SAVEASCOMMAND, new Event());
				getCommandStack().markSaveLocation();
				setEditorTitle();
				ValidationViewModel.getInstance().update((ExperimentGraph)getGraph(), validate());
			} catch (Exception ex) {
				Activator.log(IStatus.ERROR, 
						 Messages.SAVE_FAILED, ex);
			}
		}

	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	
	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
	 */
	protected void firePropertyChange(int property) {
		super.firePropertyChange(property);
		updateActions(propertyActions);
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		updateActions(stackActions);
		ValidationViewModel.getInstance().update((ExperimentGraph)getGraph(), validate());
	}
	
	/**
	 * A convenience method for updating a set of actions defined by the given List of action
	 * IDs. The actions are found by looking up the ID in the {@link #getActionRegistry()
	 * action registry}. If the corresponding action is an {@link UpdateAction}, it will have
	 * its <code>update()</code> method called.
	 * @param actionIds the list of IDs to update
	 */
	protected void updateActions(List<String> actionIds) {
		ActionRegistry registry = getActionRegistry();
		Iterator<String> iter = actionIds.iterator();
		while (iter.hasNext()) {
			IAction action = registry.getAction(iter.next());
			if (action instanceof UpdateAction)
				((UpdateAction)action).update();
		}
	}

	
	/**
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// If not the active editor, ignore selection changed.
		if (this.equals(getSite().getPage().getActiveEditor()))
			updateActions(selectionActions);
	}
	
	@Override
	public List<Message> validate() {
		ExperimentGraph model = (ExperimentGraph) getGraph();
		
		List<Message> msgs = new ArrayList<Message>();
		if (model.getProcess() == null || model.getProcess().isEmpty()){
			msgs.add(new Message(Message.Type.ERROR, getPartName(), Messages.PROCESS_NAME_EMPTY));
		}
		if (model.getEmail() == null || model.getEmail().isEmpty()){
			msgs.add(new Message(Message.Type.ERROR, getPartName(), Messages.EMAIL_EMPTY));
		}
		if (model.getUrl() == null || model.getUrl().isEmpty()){
			msgs.add(new Message(Message.Type.ERROR, getPartName(), Messages.DB_URI_EMPTY));
		}
		if (model.getUser() == null || model.getUser().isEmpty()){
			msgs.add(new Message(Message.Type.ERROR, getPartName(), Messages.DB_USERNAME_EMPTY));
		}
		if (model.getPassword() == null || model.getPassword().isEmpty()){
			msgs.add(new Message(Message.Type.ERROR, getPartName(), Messages.DB_PASSWD_EMPTY));
		}
		return msgs;
	}

	@Override
	public void dispose() {	
		super.dispose();
		ValidationViewModel.getInstance().remove((ExperimentGraph)getGraph());
	}
	
	

}
