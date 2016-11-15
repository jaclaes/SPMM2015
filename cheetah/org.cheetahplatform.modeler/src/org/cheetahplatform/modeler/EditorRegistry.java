package org.cheetahplatform.modeler;

import static org.cheetahplatform.modeler.engine.TDMModelingActivity.TDM_MODELING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.AssertionFailedException;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.modeler.bpmn.ActivityDescriptor;
import org.cheetahplatform.modeler.bpmn.AndGatewayDescriptor;
import org.cheetahplatform.modeler.bpmn.BPMNEditor;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.bpmn.EndEventDescriptor;
import org.cheetahplatform.modeler.bpmn.HierarchicalActivityDescriptor;
import org.cheetahplatform.modeler.bpmn.StartEventDescriptor;
import org.cheetahplatform.modeler.bpmn.XorGatewayDescriptor;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.DecSerFlowEditor;
import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.ChainedPrecedenceConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.ChainedResponseConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.ChainedSuccessionConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.CoexistenceConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.DecSerFlowActivityDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.InitConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.LastConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiExclusiveChoiceAuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiExclusiveChoiceDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiPrecedenceConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiResponseConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiSuccessionConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MutualExclusionConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.NegationResponseConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.PrecedenceConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.RespondedExistenceConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.ResponseConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.SuccessionConstraintDescriptor;
import org.cheetahplatform.modeler.engine.DecSerFlowModelingActivity;
import org.cheetahplatform.modeler.engine.OperationSpanActivity;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.modeler.graph.GraphEditorInput;
import org.cheetahplatform.modeler.graph.descriptor.EdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Registry for {@link INodeDescriptor} and {@link IEdgeDescriptor} objects.
 * 
 * @author zugi
 * 
 */
public class EditorRegistry {
	public static final String BPMN = "BPMN";
	public static final String CHANGE_PATTERN = "CHANGE_PATTERN";
	public static final String BPMN_SEQUENCE_FLOW = BPMN + ".SEQUENCE_FLOW";
	public static final String BPMN_ACTIVITY = BPMN + ".ACTIVITY";
	public static final String BPMN_ACTIVITY_HIERARCHICAL = BPMN_ACTIVITY + ".HIERARCHICAL";
	public static final String BPMN_START_EVENT = BPMN + ".START_EVENT";
	public static final String BPMN_END_EVENT = BPMN + ".END_EVENT";
	public static final String BPMN_AND_GATEWAY = BPMN + ".AND";
	public static final String BPMN_XOR_GATEWAY = BPMN + ".XOR";

	public static final String DECSERFLOW = "DECSERFLOW";
	public static final String DECSERFLOW_ACTIVITY = DECSERFLOW + ".ACTIVITY";
	public static final String DECSERFLOW_RESPONSE = DECSERFLOW + ".RESPONSE";
	public static final String DECSERFLOW_COEXISTENCE = DECSERFLOW + ".COEXISTENCE";
	public static final String DECSERFLOW_RESPONDED_EXISTENCE = DECSERFLOW + ".RESPONDED_EXISTENCE";
	public static final String DECSERFLOW_MUTUAL_EXCLUSION = DECSERFLOW + ".MUTUAL_EXCLUSION";
	public static final String DECSERFLOW_SELECTION = DECSERFLOW + ".SELECTION";
	public static final String DECSERFLOW_PRECEDENCE = DECSERFLOW + ".PRECEDENCE";
	public static final String DECSERFLOW_SUCCESSION = DECSERFLOW + ".SUCCESSION";
	public static final String DECSERFLOW_CHAINED_SUCCESSION = DECSERFLOW + ".CHAINED_SUCCESSION";
	public static final String DECSERFLOW_CHAINED_RESPONSE = DECSERFLOW + ".CHAINED_RESPONSE";
	public static final String DECSERFLOW_INIT = DECSERFLOW + ".INIT";
	public static final String DECSERFLOW_LAST = DECSERFLOW + ".LAST";
	public static final String DECSERFLOW_NEGATION_RESPONSE = DECSERFLOW + ".NEGATION_RESPONSE";
	public static final String DECSERFLOW_MULTI_RESPONSE = DECSERFLOW + ".MULTI_RESPONSE";
	public static final String DECSERFLOW_MULTI_PRECENDENCE = DECSERFLOW + ".MULTI_PRECEDENCE";
	public static final String DECSERFLOW_AUXILIARY_NODE = DECSERFLOW + ".AUXILIARY_NODE";
	public static final String DECSERFLOW_MULTI_SUCCESSION = DECSERFLOW + ".MULTI_SUCCESSION";
	public static final String DECSERFLOW_CHAINED_PRECEDENCE = DECSERFLOW + ".CHAINED_PRECEDENCE";
	public static final String DECSERFLOW_MULTI_EXCLUSIVE_CHOICE = DECSERFLOW + ".MULTI_EXCLUSIVE_CHOICE";
	public static final String DECSERFLOW_AUXILIARY_NODE_FOR_MULTI_EXCLUSIVE_CHOICE = DECSERFLOW
			+ ".AUXILIARY_NODE_FOR_MULTI_EXCLUSIVE_CHOICE";

	public static final String LITERATE_MODELING = "LITERATE_MODELING";

	private static List<INodeDescriptor> BPMN_NODES;
	private static List<IEdgeDescriptor> BPMN_EDGES;
	private static List<INodeDescriptor> DECSERFLOW_NODES;
	private static List<IEdgeDescriptor> DECSERFLOW_EDGES;

	private static final List<IGraphElementDescriptor> ALL_DESCRIPTORS;
	private static final List<IModelingEditorProvider> EDITOR_PROVIDERS;

	static {
		createBpmnDescriptors();
		createDecSerFlowDescriptors();

		ALL_DESCRIPTORS = new ArrayList<IGraphElementDescriptor>();
		ALL_DESCRIPTORS.addAll(BPMN_NODES);
		ALL_DESCRIPTORS.addAll(BPMN_EDGES);
		ALL_DESCRIPTORS.addAll(DECSERFLOW_NODES);
		ALL_DESCRIPTORS.addAll(DECSERFLOW_EDGES);

		EDITOR_PROVIDERS = new ArrayList<IModelingEditorProvider>();
		if (Platform.isRunning()) {
			for (IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor("org.cheetahplatform.editor")) {

				if (element.getName().equals("editor")) {
					try {
						IModelingEditorProvider provider = (IModelingEditorProvider) element.createExecutableExtension("class");
						EDITOR_PROVIDERS.add(provider);
					} catch (CoreException e) {
						org.cheetahplatform.common.Activator.logError("Could not create an executable extension", e);
					}
				}
			}
		}
	}

	private static void createBpmnDescriptors() {
		List<INodeDescriptor> tempNodes = new ArrayList<INodeDescriptor>();
		tempNodes.add(new ActivityDescriptor());
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.HIERARCHICAL_ACTIVITIES)) {
			tempNodes.add(new HierarchicalActivityDescriptor());
		}

		tempNodes.add(new AndGatewayDescriptor());
		tempNodes.add(new XorGatewayDescriptor());
		tempNodes.add(new StartEventDescriptor());
		tempNodes.add(new EndEventDescriptor());
		BPMN_NODES = Collections.unmodifiableList(tempNodes);

		List<IEdgeDescriptor> tempEdges = new ArrayList<IEdgeDescriptor>();
		tempEdges.add(new EdgeDescriptor("img/bpmn/sequenceflow.gif", "Sequence Flow", BPMN_SEQUENCE_FLOW));
		BPMN_EDGES = Collections.unmodifiableList(tempEdges);
	}

	private static void createDecSerFlowDescriptors() {
		List<INodeDescriptor> nodes = new ArrayList<INodeDescriptor>();
		nodes.add(new DecSerFlowActivityDescriptor());
		nodes.add(new AuxiliaryNodeDescriptor());
		nodes.add(new MultiExclusiveChoiceAuxiliaryNodeDescriptor());
		DECSERFLOW_NODES = Collections.unmodifiableList(nodes);

		List<IEdgeDescriptor> edges = new ArrayList<IEdgeDescriptor>();
		edges.add(new SelectionConstraintDescriptor());
		edges.add(new InitConstraintDescriptor());
		edges.add(new LastConstraintDescriptor());
		edges.add(new PrecedenceConstraintDescriptor());
		edges.add(new ResponseConstraintDescriptor());
		edges.add(new SuccessionConstraintDescriptor());
		edges.add(new ChainedPrecedenceConstraintDescriptor());
		edges.add(new ChainedResponseConstraintDescriptor());
		edges.add(new ChainedSuccessionConstraintDescriptor());
		edges.add(new RespondedExistenceConstraintDescriptor());
		edges.add(new CoexistenceConstraintDescriptor());
		edges.add(new MutualExclusionConstraintDescriptor());
		edges.add(new NegationResponseConstraintDescriptor());
		edges.add(new MultiPrecedenceConstraintDescriptor());
		edges.add(new MultiSuccessionConstraintDescriptor());
		edges.add(new MultiResponseConstraintDescriptor());
		edges.add(new MultiExclusiveChoiceDescriptor());
		DECSERFLOW_EDGES = Collections.unmodifiableList(edges);
	}

	/**
	 * Return the descriptor for a specific id.
	 * 
	 * @param id
	 *            the id
	 * @return the corresponding graph descriptor, <code>null</code> if no corresponding was found
	 */
	public static IGraphElementDescriptor getDescriptor(String id) {
		for (IGraphElementDescriptor descriptor : ALL_DESCRIPTORS) {
			if (descriptor.getId().equals(id)) {
				return descriptor;
			}
		}

		return null;
	}

	/**
	 * Collects all {@link INodeDescriptor} and {@link IEdgeDescriptor} objects.
	 * 
	 * @param id
	 *            the id
	 * @return all descriptors for the given id
	 */
	public static List<IGraphElementDescriptor> getDescriptors(String id) {
		List<IGraphElementDescriptor> descriptors = new ArrayList<IGraphElementDescriptor>();
		descriptors.addAll(getNodeDescriptors(id));
		descriptors.addAll(getEdgeDescriptors(id));

		return descriptors;
	}

	public static String getEclipseEditorId(String id) {
		if (BPMN.equals(id)) {
			return BPMNEditor.ID;
		} else if (DECSERFLOW.equals(id)) {
			return DecSerFlowEditor.ID;
		} else if (LITERATE_MODELING.equals(id)) {
			return LiterateModelingEditor.ID;
		} else {
			for (IModelingEditorProvider provider : EDITOR_PROVIDERS) {
				String translated = provider.translateToEclipseEditorId(id);
				if (translated != null) {
					return translated;
				}
			}

			throw new AssertionFailedException("No editor for id:" + id);
		}
	}

	/**
	 * Returns the edge descriptors for a specific class of edges, e.g., {@link #BPMN}.
	 * 
	 * @param id
	 *            the id
	 * @return all corresponding edge descriptors
	 */
	public static List<IEdgeDescriptor> getEdgeDescriptors(String id) {
		if (BPMN.equals(id)) {
			return BPMN_EDGES;
		}
		if (DECSERFLOW.equals(id) || TDM_MODELING.equals(id)) {
			return DECSERFLOW_EDGES;
		}
		if (CHANGE_PATTERN.equals(id)) {
			return BPMN_EDGES;
		}

		return Collections.emptyList();
	}

	/**
	 * Returns the node descriptors for a specific class of edges, e.g., {@link #BPMN}.
	 * 
	 * @param id
	 *            the id
	 * @return all corresponding edge descriptors
	 */
	public static List<INodeDescriptor> getNodeDescriptors(String id) {
		if (BPMN.equals(id)) {
			return BPMN_NODES;
		}
		if (DECSERFLOW.equals(id) || TDM_MODELING.equals(id)) {
			return DECSERFLOW_NODES;
		}

		if (CHANGE_PATTERN.equals(id)) {
			return BPMN_NODES;
		}

		return Collections.emptyList();
	}

	public static boolean isModelingActivityId(String id) {
		boolean isBPMN = id.equals(BPMNModelingActivity.ID);
		boolean isDecSerFlow = id.equals(DecSerFlowModelingActivity.ID);
		boolean isTDM = id.equals(TDMModelingActivity.TDM_MODELING);
		boolean isOperationSpan = id.equals(OperationSpanActivity.TYPE_OPERATION_SPAN);

		if (isBPMN || isDecSerFlow || isTDM || isOperationSpan) {
			return true;
		}

		for (IModelingEditorProvider provider : EDITOR_PROVIDERS) {
			if (provider.isModelingEditorActivityId(id)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Open the appropriate editor for the given id.
	 * 
	 * @param id
	 *            the id
	 * @return the opened editor
	 */
	public static IEditorPart openEditor(String id, Graph graph, List<Attribute> attributes, Process process) {
		IEditorInput input = new GraphEditorInput(graph, attributes, process);
		return openEditor(id, input);
	}

	/**
	 * Open the appropriate editor for the given id.
	 * 
	 * @param id
	 *            the id
	 * @return the opened editor
	 */
	public static IEditorPart openEditor(String id, Graph graph, List<Attribute> attributes, Process process, String descriptionText) {
		IEditorInput input = new GraphEditorInput(graph, attributes, process, descriptionText);
		return openEditor(id, input);
	}

	public static IEditorPart openEditor(String id, IEditorInput input) {
		String editorId = getEclipseEditorId(id);

		try {
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
			return editor;
		} catch (PartInitException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not open a graph editor.", e);
			Activator.getDefault().getLog().log(status);
			throw new AssertionFailedException("Could not open a graph editor.");
		}
	}

	/**
	 * Register a descriptor.
	 * 
	 * @param descriptor
	 *            the descriptor to be registered
	 */
	public static void registerDescriptor(IGraphElementDescriptor descriptor) {
		for (IGraphElementDescriptor current : ALL_DESCRIPTORS) {
			Assert.isTrue(!current.getId().equals(descriptor.getId()), "Descriptor already exists: " + descriptor.getId());
		}

		ALL_DESCRIPTORS.add(descriptor);
	}
}
