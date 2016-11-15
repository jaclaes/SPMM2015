package org.cheetahplatform.tdm.modeler.test;

import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_WORKSPACE;
import static org.cheetahplatform.modeler.configuration.IConfiguration.ALLOW_MODIFICATION_OF_TESTS;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.common.ui.gef.ScrollingGraphicalViewerWithForeignSupport;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.generic.GenericEditPartFactory;
import org.cheetahplatform.modeler.generic.GenericMenuManager;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.daily.editpart.ActivityEditPart;
import org.cheetahplatform.tdm.daily.editpart.WorkspaceEditPart;
import org.cheetahplatform.tdm.daily.figure.VerticalScrollBar;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.ITestListener;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.explorer.TDMProjectExplorerView;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.swtdesigner.ResourceManager;

public class TDMTestEditor extends EditorPart implements ITestListener {

	private class TestPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(TDMCommand.ATTRIBUTE_TEST_NAME)) {
				setPartName(((TDMTest) evt.getSource()).getName());
			}
		}

	}

	private class WorkspacePropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			testChanged();
		}

	}

	public static final String ID = "org.cheetahplatform.tdm.TDMTestEditor";

	public static TDMTestWorkspace getTDMWorkspace(TDMTest test) {
		List<IIdentifiableObject> workspaces = Services.getCheetahObjectLookup().getObjectsFromNamespace(NAMESPACE_WORKSPACE);
		for (IIdentifiableObject workspace : workspaces) {
			if (((TDMTestWorkspace) workspace).getTest().equals(test)) {
				return ((TDMTestWorkspace) workspace);
			}
		}

		return null;
	}

	public static Workspace getWorkspace(TDMTest test) {
		TDMTestWorkspace workspace = getTDMWorkspace(test);
		if (workspace == null) {
			return null;
		}

		return workspace.getWorkspace();
	}

	private TDMTestEditorModel model;
	private TDMTestEditorComposite composite;
	private TestPropertyChangeListener propertyChangeListener;
	private ScrollingGraphicalViewer viewer;

	private void addKeyHandler(final ScrollingGraphicalViewer viewer) {
		KeyHandler handler = new KeyHandler();
		handler.put(KeyStroke.getPressed(SWT.DEL, SWT.DEL, SWT.NONE), new Action() {

			@Override
			public void run() {
				List selection = viewer.getSelectedEditParts();
				CompoundCommand command = new CompoundCommand();
				for (Object editPart : selection) {
					Command deleteCommand = ((EditPart) editPart).getCommand(new Request(RequestConstants.REQ_DELETE));
					if (deleteCommand != null) {
						command.add(deleteCommand);
					}
				}

				if (command.canExecute()) {
					viewer.getEditDomain().getCommandStack().execute(command);
				}
			}
		});
		viewer.setKeyHandler(handler);
	}

	private EditDomain createEditDomain() {
		EditDomain domain = new CustomEditDomain() {

			@Override
			public void mouseWheelScrolled(Event event, EditPartViewer viewer) {
				super.mouseWheelScrolled(event, viewer);

				WorkspaceEditPart workspace = (WorkspaceEditPart) viewer.getContents();
				VerticalScrollBar scrollBar = (VerticalScrollBar) workspace.getScrollPane().getVerticalScrollBar();
				if (event.count > 0) {
					scrollBar.stepUp();
				} else {
					scrollBar.stepDown();
				}

			}
		};

		return domain;
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new TDMTestEditorComposite(parent, SWT.NONE);

		initialize();
	}

	@Override
	public void dispose() {
		super.dispose();

		if (!model.isTestDeleted()) {
			AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_CLOSE_TEST_EDITOR);
			entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, model.getTest().getCheetahId());
			entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, model.getTest().getName());
			model.log(entry);
		}

		model.clearConstraintHighlighting();
		model.getTest().removePropertyListener(propertyChangeListener);
		model.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// not needed, saved anyway by logging
	}

	@Override
	public void doSaveAs() {
		// not needed, saved anyway by logging
	}

	public TDMTestEditorModel getModel() {
		return model;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof TDMTestEditorInput)) {
			throw new PartInitException("Unsupported editor input: " + input);
		}

		setSite(site);
		setInput(input);
		TDMTest test = ((TDMTestEditorInput) input).getTest();
		propertyChangeListener = new TestPropertyChangeListener();
		test.addPropertyListener(propertyChangeListener);
		Workspace existingWorkspace = getWorkspace(test);

		IPromLogger logger = ((TDMTestEditorInput) input).getLogger();
		model = new TDMTestEditorModel(test, existingWorkspace, logger);
		setPartName(test.getName());
	}

	private void initialize() {
		viewer = new ScrollingGraphicalViewerWithForeignSupport();
		addKeyHandler(viewer);

		Control control = viewer.createControl(composite);
		control.setData(ScrollingGraphicalViewerWithForeignSupport.GRAPHICAL_VIEWER, viewer);
		viewer.setEditDomain(createEditDomain());
		viewer.setEditPartFactory(new GenericEditPartFactory());
		viewer.setContents(model.getWorkspace());
		viewer.setContextMenu(new GenericMenuManager(viewer));
		composite.layout(true, true);

		model.getWorkspace().addPropertyChangeListener(new WorkspacePropertyChangeListener());
		model.getTest().addTestListener(this);
		model.updateTestFromWorkspace();
		((CustomEditDomain) viewer.getEditDomain()).setEditable(CheetahPlatformConfigurator.getBoolean(ALLOW_MODIFICATION_OF_TESTS));

		Services.getCheetahObjectLookup().registerObject(NAMESPACE_WORKSPACE, new TDMTestWorkspace(model.getTest(), model.getWorkspace()));
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		List selection = viewer.getSelectedEditParts();
		model.clearModelHighlighting();

		for (Object editPart : selection) {
			if (editPart instanceof ActivityEditPart) {
				((ActivityEditPart) editPart).updateConstraintHighlighting();
			}
		}

		TDMProjectExplorerView explorer = (TDMProjectExplorerView) getEditorSite().getWorkbenchWindow().getActivePage()
				.findView(TDMProjectExplorerView.ID);
		if (explorer == null) {
			return;
		}

		explorer.select(model.getTest());
	}

	public void testChanged() {
		model.updateTestFromWorkspace();
	}

	@Override
	public void testFailed(TDMTest test, List<ITDMStep> failures) {
		updateEditorImage("img/tdm/test_failed.gif");
		model.updateActivityColoring();
	}

	@Override
	public void testPassed(TDMTest test) {
		updateEditorImage("img/tdm/test_passed.gif");
		model.updateActivityColoring();
	}

	@SuppressWarnings("unused")
	protected void updateEditorImage(String imagePath) {
		// dang buggy eclipse implementation (see below)
		if (true) {
			return;
		}

		if (imagePath.equals(model.getCurrentTitleImageUrl())) {
			return;
		}

		Image image = ResourceManager.getPluginImage(Activator.getDefault(), imagePath);
		try {
			setTitleImage(image);
		} catch (SWTException e) {
			// just ignore and try again - buggy workbench part implementation :-(
			try {
				setTitleImage(image);
			} catch (Exception e1) {
				// just ignore
			}
		}
		model.setCurrentTitleImageUrl(imagePath);
	}
}
