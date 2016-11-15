package org.cheetahplatform.modeler.engine;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.PartListenerAdapter;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.xml.XMLPromReader;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.AbstractExperimentalGraphEditor;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.Perspective;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.CommandReplayer;
import org.cheetahplatform.modeler.graph.dialog.LayoutDialog;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public abstract class AbstractModelingActivity extends AbstractExperimentsWorkflowActivity {

	public static Graph loadInitialGraph(Graph graph, String path) {
		URL input = null;
		String prefix = "resource/";

		try {
			if (Platform.isRunning()) {
				input = FileLocator.find(Activator.getDefault().getBundle(), new Path(prefix + path), new HashMap<Object, Object>());
			} else {
				File file = new File(prefix + path);
				input = file.toURI().toURL();
			}

			String postfix = path.substring(path.lastIndexOf('.') + 1);
			if (postfix.compareToIgnoreCase("MXML") == 0) {
				restoreGraph(input.openStream(), graph);
			} else if (postfix.compareToIgnoreCase("XML") == 0) {
				graph = restoreGraphXML(input);
			} else {
				throw new RuntimeException("unkown file format of initial model");
			}

		} catch (Exception e) {
			Activator.logError("Could not load inital model: " + path, e); //$NON-NLS-1$
		}

		return graph;
	}

	public static Graph loadInitialGraph(Process process, String graphType) {
		Graph graph = new Graph(EditorRegistry.getDescriptors(graphType));
		String path = ProcessRepository.getInitialModelPath(process);
		if (path != null) {
			Graph initialGraph = loadInitialGraph(graph, path);
			if (initialGraph != null) {
				return initialGraph;
			}
		}
		Graph initialGraph = ProcessRepository.getInitialModel(process);
		return initialGraph != null ? initialGraph : graph;
	}

	public static void resetIdGenerator(Graph initialGraph) {
		long maximumId = 0;
		List<GraphElement> graphElements = initialGraph.getGraphElements();
		for (GraphElement graphElement : graphElements) {
			if (maximumId < graphElement.getId()) {
				maximumId = graphElement.getId();
			}
		}

		Services.getIdGenerator().setMinimalId(maximumId + 100);
	}

	public static Graph restoreGraph(Graph initialGraph, ProcessInstance instance) {
		GraphCommandStack stack = new GraphCommandStack();
		stack.setGraph(initialGraph);

		CommandReplayer replayer = new CommandReplayer(stack, initialGraph, instance);
		List<CommandDelegate> commands = replayer.getCommands();
		for (int i = 0; i < commands.size(); i++) {
			CommandDelegate command = commands.get(i);
			if (command.getCommand() instanceof TDMCommand) {
				continue;
			}
			command.execute();
		}

		resetIdGenerator(initialGraph);
		return initialGraph;
	}

	public static Graph restoreGraph(InputStream initialProcess, Graph initialGraph) throws Exception {
		XMLPromReader reader = new XMLPromReader(initialProcess);
		return restoreGraph(initialGraph, reader.getProcess().getInstances().get(0));
	}

	public static Graph restoreGraphXML(URL url) throws Exception {
		ExperimentEditorMarshaller marshaller = new ExperimentEditorMarshaller();
		return marshaller.unmarshallFromFile(FileLocator.toFileURL(url).getFile());
	}

	private AbstractExperimentalGraphEditor editor;
	private final Process process;
	private Graph initialGraph;
	private String instanceId;
	protected final String graphEditorId;
	private LayoutDialog layoutDialog;
	private final boolean optional;
	private static ToolBarManager toolBarManager;

	protected AbstractModelingActivity(String id, String graphEditorId, Graph initialGraph, Process process, boolean optional) {
		super(id);
		this.optional = optional;

		Assert.isNotNull(process);
		this.initialGraph = initialGraph;
		this.process = process;
		this.graphEditorId = graphEditorId;
		toolBarManager = null;
	}

	/**
	 * Install listener which make the activity finish.
	 */
	private void addFinishListener() {
		final WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final ICoolBarManager menuManager = ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow()).getCoolBarManager2();
		// menuManager.removeAll();

		if (toolBarManager == null) {
			toolBarManager = new ToolBarManager();
			menuManager.add(toolBarManager);
		} else {
			toolBarManager.removeAll();
		}

		window.getActivePage().addPartListener(new PartListenerAdapter() {

			@Override
			public void partClosed(IWorkbenchPart part) {
				String editorId = EditorRegistry.getEclipseEditorId(graphEditorId);

				if (part.getSite().getId().equals(editorId)) {
					if (window.getActivePage() != null) {
						window.getActivePage().removePartListener(this);
						setFinished(true);
					}

					// menuManager.removeAll();
					menuManager.update(true);
				}
			}
		});

		toolBarManager.add(new FinishAction(this, editor.getGraph(), optional));
		toolBarManager.update(true);
		menuManager.update(true);
	}

	protected List<Attribute> collectAttributes() {
		return new ArrayList<Attribute>();
	}

	@Override
	protected void doExecute() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(Perspective.ID);
		workbench.getActiveWorkbenchWindow().getActivePage().setPerspective(perspective);

		List<Attribute> attributes = collectAttributes();
		attributes.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS, process.getId()));

		editor = openEditor(attributes);

		ProcessInstance instance = editor.getModel().getInstance();
		instanceId = instance.getId();
		addFinishListener();

		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_LAYOUT_DIALOG)) {
			layoutDialog = new LayoutDialog(editor.getEditorSite().getShell(), editor.getGraphViewer());
			layoutDialog.open();
		}

		setFinished(false);
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> attributes = super.getData();
		attributes.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, instanceId));
		return attributes;
	}

	public Graph getInitialGraph() {
		return initialGraph;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public Process getProcess() {
		return process;
	}

	protected AbstractExperimentalGraphEditor openEditor(List<Attribute> attributes) {
		return (AbstractExperimentalGraphEditor) EditorRegistry.openEditor(graphEditorId, initialGraph, attributes, process);
	}

	@Override
	protected void postExecute() {
		super.postExecute();

		if (layoutDialog != null) {
			layoutDialog.close();
		}
	}

	@Override
	public void setFinished(boolean state) {
		super.setFinished(state);

		if (state) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
		}
	}

	public void setInstanceId(String s) {
		instanceId = s;
	}
}
