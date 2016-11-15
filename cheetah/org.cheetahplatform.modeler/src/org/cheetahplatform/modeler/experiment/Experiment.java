package org.cheetahplatform.modeler.experiment;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.IExperimentalWorkflowActivity;
import org.cheetahplatform.modeler.engine.WorkflowConfiguration;
import org.cheetahplatform.modeler.engine.configurations.AbstractExperimentalConfiguration;
import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.experiment.editor.model.TutorialNode;
import org.cheetahplatform.modeler.experiment.editor.xml.ExpEditorMarshallerException;
import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.cheetahplatform.modeler.experiment.editor.xml.INodeDescriptorRegistry;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class Experiment extends AbstractExperimentalConfiguration {

	protected static class ActivityProvider {
		private IActivityFactory factory;
		private List<ActivityType> activityTypes;

		public ActivityProvider(IActivityFactory factory) {
			this.factory = factory;
			activityTypes = new ArrayList<ActivityType>();
		}

		public void addActivityType(ActivityType type) {
			activityTypes.add(type);
		}

		public IActivityFactory getFactory() {
			return factory;
		}

		public boolean supports(String type, String subtype) {
			boolean result = false;
			for (ActivityType at : activityTypes) {
				if (at.getType().equals(type)) {
					if (at.getSubtype() == null) {
						result = true;
						break;
					} else if (at.getSubtype().equals(subtype)) {
						result = true;
						break;
					}

				}
			}
			return result;
		}
	}

	protected static class ActivityType {
		private String type;
		private String subtype;

		public ActivityType(String type, String subtype) {
			this.type = type;
			this.subtype = subtype;
		}

		public String getSubtype() {
			return subtype;
		}

		public String getType() {
			return type;
		}

		public void setSubtype(String subtype) {
			this.subtype = subtype;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	public static final String EXPERIMENT = "experiment.xml";
	public static final String NOSTARTNODE = "Start node of experiment configuration not found";

	private static final String ACTIVITY_PROVIDER_EXTENSION = "org.cheetahplatform.activity.provider";
	private static int NOTANUMBER = Integer.MAX_VALUE;
	private List<ActivityProvider> activityProviders;
	private ExperimentGraph graph;
	private Process experimentalProcess = new Process("dummy");
	private List<Process> processes;
	private Map<Process, Graph> processInitialGraphMap;

	private List<WorkflowConfiguration> workflowConfigs;

	public Experiment() {
		workflowConfigs = new ArrayList<WorkflowConfiguration>();
		activityProviders = new ArrayList<ActivityProvider>();
		processes = new ArrayList<Process>();
		processInitialGraphMap = new HashMap<Process, Graph>();

		for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(ACTIVITY_PROVIDER_EXTENSION)) {
			if (element.getName().equals("activityFactory")) {
				try {
					IActivityFactory factory = (IActivityFactory) element.createExecutableExtension("class");
					ActivityProvider provider = new ActivityProvider(factory);
					activityProviders.add(provider);
					for (IConfigurationElement actType : element.getChildren()) {
						provider.addActivityType(new ActivityType(actType.getAttribute("type"), actType.getAttribute("subtype")));
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		createConfigurations();
	}

	protected void addProcess(Process process) {
		processes.add(process);
	}

	protected void addProcess(Process process, Graph initialGraph) {
		addProcess(process);
		processInitialGraphMap.put(process, initialGraph);
	}

	@Override
	public boolean allowsRecovering() {
		return true;
	}

	private void configureExperiment(ExperimentGraph graph) {
		// set the experiment process name that is stored in the graph
		setExperimentalProcess(new Process(graph.getProcess()));
		addProcess(getExperimentProcess());

		// use the logging configuration stored in the graph
		XMLLogHandler storage = XMLLogHandler.getInstance();
		storage.setEmail(graph.getEmail());
		storage.setUrl(graph.getUrl());
		storage.setUser(graph.getUser());
		storage.setPassword(graph.getPassword());

		// set the database connection to get the experiment ID from the db
		try {
			Connection connection = org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection();
			// In replay mode the connection should not be overwritten!!!
			if (connection == null) {
				IDatabaseConnector databaseConnector = org.cheetahplatform.common.Activator.getDatabaseConnector();
				databaseConnector.setDatabaseURL(graph.getUrl());
				databaseConnector.setDefaultCredentials(graph.getUser(), graph.getPassword());

				CheetahPlatformConfigurator.setBoolean(IConfiguration.SHOW_START_MODELING_DIALOG, graph.isStartModelingDialogShown());
			}

		} catch (SQLException ex) {
			Activator.logError("Could't get db connection", ex);
		}

	}

	private IExperimentalWorkflowActivity createActivity(Node node) {
		IExperimentalWorkflowActivity result = null;
		String nodeType = node.getDescriptor().getId();

		String subtype = null;
		if (nodeType.equals(INodeDescriptorRegistry.TUTORIAL)) {
			TutorialNode tutNode = (TutorialNode) node;
			subtype = tutNode.getTutorial();
		}

		for (ActivityProvider provider : activityProviders) {
			if (provider.supports(nodeType, subtype)) {
				result = provider.getFactory().createActivity(nodeType, subtype, node);
				if (result instanceof AbstractModelingActivity) {
					AbstractModelingActivity modelingActivity = (AbstractModelingActivity) result;
					addProcess(modelingActivity.getProcess());
					addProcess(modelingActivity.getProcess(), modelingActivity.getInitialGraph());
				}
				break;
			}
		}
		return result;
	}

	@Override
	public List<WorkflowConfiguration> createConfigurations() {
		if (workflowConfigs.isEmpty()) {
			ExperimentGraph graph = getGraph();
			if (graph != null) {
				configureExperiment(graph);
				Node startNode = null;
				// find the start node
				for (Node node : graph.getNodes()) {
					if (node.getDescriptor().getId().equals(EditorRegistry.BPMN_START_EVENT)) {
						startNode = node;
						break;
					}
				}
				if (startNode == null) {
					Activator.logError(NOSTARTNODE, new RuntimeException(NOSTARTNODE));
				} else {
					readConfiguration(newConfig(NOTANUMBER), startNode);
				}
			}
		}
		return workflowConfigs;
	}

	@Override
	public Process getExperimentProcess() {
		return experimentalProcess;
	}

	/**
	 * 
	 * @return The graph loaded from the well known file path or NULL.
	 */
	private ExperimentGraph getGraph() {
		if (this.graph == null) {
			URL url = Platform.getInstanceLocation().getURL();
			String filePath = url.getPath() + EXPERIMENT;
			File file = new File(filePath);
			if (file.exists()) {
				ExperimentEditorMarshaller marsh = new ExperimentEditorMarshaller();
				try {
					this.graph = (ExperimentGraph) marsh.unmarshallFromFile(filePath);
				} catch (ExpEditorMarshallerException ex) {
					// relax - it't ok to be null :)
				}
			}
		}
		return graph;
	}

	@Override
	public Map<Process, Graph> getInitialModelMap() {
		// return a copy of the process initial graph map,
		// because the graph will be changed during replay
		Map<Process, Graph> copy = new HashMap<Process, Graph>();
		for (Process p : processInitialGraphMap.keySet()) {
			copy.put(p, ExperimentGraph.copyGraph(processInitialGraphMap.get(p)));
		}
		return copy;
	}

	@Override
	public List<Process> getProcesses() {
		return processes;
	}

	private WorkflowConfiguration newConfig(Integer id) {
		WorkflowConfiguration config = new WorkflowConfiguration(id);
		workflowConfigs.add(config);
		return config;
	}

	private void readConfiguration(WorkflowConfiguration config, Node node) {
		IExperimentalWorkflowActivity activity = createActivity(node);
		if (activity != null) {
			config.add(activity);
		}

		Iterator it = node.getSourceConnections().iterator();
		while (it.hasNext()) {
			Edge edge = (Edge) it.next();

			// System.out.println(edge.getName() + " [" + edge.getSource().getName() + "(" + edge.getSource().getId() + ")]->["
			// + edge.getTarget().getName() + "(" + edge.getTarget().getId() + ")]");

			WorkflowConfiguration newConfig = config;
			if (it.hasNext()) { // clone config for all branches except the last one.
				newConfig = newConfig(config.getId());
				newConfig.addAll(new ArrayList<IExperimentalWorkflowActivity>(config.getActivites()));
			}
			updateConfigCode(newConfig, edge.getName());
			readConfiguration(newConfig, edge.getTarget());
		}
	}

	public void setExperimentalProcess(Process process) {
		this.experimentalProcess = process;
	}

	private void updateConfigCode(WorkflowConfiguration config, String name) {
		if (name == null || name.equals("")) {
			return;
		}
		name = name.trim();
		Integer edgeCode = Integer.parseInt(name);
		if (config.getId() == NOTANUMBER) {
			config.setId(edgeCode);
		} else {
			config.setId(Integer.parseInt(config.getId() + name));
		}
	}
}
