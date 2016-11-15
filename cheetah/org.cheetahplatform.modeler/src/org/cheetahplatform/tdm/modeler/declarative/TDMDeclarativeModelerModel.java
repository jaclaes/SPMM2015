package org.cheetahplatform.tdm.modeler.declarative;

import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_END;
import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_START;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_CONSTRAINTS;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_ACTIVITIES;
import static org.cheetahplatform.core.service.SimpleCheetahServiceLookup.NAMESPACE_DECLARATIVE_PROCESS_SCHEMA;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_CHAINED_PRECEDENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_CHAINED_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_CHAINED_SUCCESSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_COEXISTENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_INIT;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_LAST;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_PRECENDENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_SUCCESSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MUTUAL_EXCLUSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_NEGATION_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_PRECEDENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_RESPONDED_EXISTENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SELECTION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SUCCESSION;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.EDIT_SELECTION_CONSTRAINT;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MAXIMUM;
import static org.cheetahplatform.modeler.decserflow.descriptor.SelectionConstraintDescriptor.MINIMUM;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.CREATE_NODE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DELETE_NODE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.DESCRIPTOR;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.ID;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.NEW_NAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RECONNECT_EDGE;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.RENAME;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.SOURCE_NODE_ID;
import static org.cheetahplatform.modeler.graph.command.AbstractGraphCommand.TARGET_NODE_ID;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.constraint.AbstractDeclarativeConstraintWithOneActivity;
import org.cheetahplatform.core.declarative.constraint.AbstractDeclarativeConstraintWithTwoActivities;
import org.cheetahplatform.core.declarative.constraint.ChainedPrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.ChainedResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.ChainedSuccessionConstraint;
import org.cheetahplatform.core.declarative.constraint.CoexistenceConstraint;
import org.cheetahplatform.core.declarative.constraint.IDeclarativeConstraint;
import org.cheetahplatform.core.declarative.constraint.InitConstraint;
import org.cheetahplatform.core.declarative.constraint.LastConstraint;
import org.cheetahplatform.core.declarative.constraint.MultiActivityConstraint;
import org.cheetahplatform.core.declarative.constraint.MutualExclusionConstraint;
import org.cheetahplatform.core.declarative.constraint.NegationResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.core.declarative.constraint.RespondedExistenceConstraint;
import org.cheetahplatform.core.declarative.constraint.ResponseConstraint;
import org.cheetahplatform.core.declarative.constraint.SelectionConstraint;
import org.cheetahplatform.core.declarative.constraint.SuccessionConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.service.ICheetahObjectLookup;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.engine.TDMProcess;

public class TDMDeclarativeModelerModel implements ILogListener {

	private static class RedirectListener implements PropertyChangeListener {

		private final Object coreObject;

		public RedirectListener(Object coreObject) {
			this.coreObject = coreObject;
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Services.getModificationService().broadcastChange(coreObject, event.getPropertyName());
		}
	}

	private static Set<String> TWO_ACTIVITIES_CONSTRAINTS;
	private static Set<String> SINGLE_ACTIVITY_CONSTRAINTS;
	private static Set<String> MULTI_ACTIVITY_CONSTRAINTS;

	static {
		TWO_ACTIVITIES_CONSTRAINTS = new HashSet<String>();
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_CHAINED_RESPONSE);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_CHAINED_SUCCESSION);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_COEXISTENCE);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_MUTUAL_EXCLUSION);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_PRECEDENCE);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_RESPONDED_EXISTENCE);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_RESPONSE);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_SUCCESSION);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_NEGATION_RESPONSE);
		TWO_ACTIVITIES_CONSTRAINTS.add(DECSERFLOW_CHAINED_PRECEDENCE);

		SINGLE_ACTIVITY_CONSTRAINTS = new HashSet<String>();
		SINGLE_ACTIVITY_CONSTRAINTS.add(DECSERFLOW_INIT);
		SINGLE_ACTIVITY_CONSTRAINTS.add(DECSERFLOW_SELECTION);
		SINGLE_ACTIVITY_CONSTRAINTS.add(DECSERFLOW_LAST);

		MULTI_ACTIVITY_CONSTRAINTS = new HashSet<String>();
		MULTI_ACTIVITY_CONSTRAINTS.add(DECSERFLOW_MULTI_PRECENDENCE);
		MULTI_ACTIVITY_CONSTRAINTS.add(DECSERFLOW_MULTI_SUCCESSION);
		MULTI_ACTIVITY_CONSTRAINTS.add(DECSERFLOW_MULTI_RESPONSE);
	}

	private Graph graph;
	private TDMProcess process;
	/**
	 * We need to keep track of the created multi activity constraints as multiple events are needed for creating it.
	 */
	private MultiActivityConstraint lastCreatedMultiActivityConstraint;

	public TDMDeclarativeModelerModel() {
		this(null, null);
	}

	public TDMDeclarativeModelerModel(Graph graph, TDMProcess process) {
		this.graph = graph;
		this.process = process;
	}

	public void dispose() {
		if (graph != null) {
			graph.removeLogListener(this);
		}
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	private DeclarativeProcessSchema getSchema() {
		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();
		List<IIdentifiableObject> schemata = lookup.getObjectsFromNamespace(NAMESPACE_DECLARATIVE_PROCESS_SCHEMA);
		Assert.isTrue(schemata.size() == 1, "Unexpected amount of process schemata: " + schemata.size());

		DeclarativeProcessSchema schema = (DeclarativeProcessSchema) schemata.get(0);
		return schema;
	}

	private void handleConstraintCreation(AuditTrailEntry entry) {
		DeclarativeProcessSchema schema = getSchema();
		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();
		long sourceId = entry.getLongAttribute(SOURCE_NODE_ID);
		DeclarativeActivity source = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, sourceId);
		long targetId = entry.getLongAttribute(TARGET_NODE_ID);
		DeclarativeActivity target = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, targetId);
		long id = entry.getLongAttribute(ID);
		IDeclarativeConstraint constraint = null;

		String descriptorId = entry.getAttribute(DESCRIPTOR);
		if (DECSERFLOW_SELECTION.equals(descriptorId)) {
			int minimum = entry.getIntegerAttribute(MINIMUM);
			int maximum = entry.getIntegerAttribute(MAXIMUM);

			constraint = new SelectionConstraint(source, minimum, maximum);
		} else if (DECSERFLOW_CHAINED_RESPONSE.equals(descriptorId)) {
			constraint = new ChainedResponseConstraint(source, target);
		} else if (DECSERFLOW_CHAINED_SUCCESSION.equals(descriptorId)) {
			constraint = new ChainedSuccessionConstraint(source, target);
		} else if (DECSERFLOW_COEXISTENCE.equals(descriptorId)) {
			constraint = new CoexistenceConstraint(source, target);
		} else if (DECSERFLOW_INIT.equals(descriptorId)) {
			constraint = new InitConstraint(source);
		} else if (DECSERFLOW_MUTUAL_EXCLUSION.equals(descriptorId)) {
			constraint = new MutualExclusionConstraint(source, target);
		} else if (DECSERFLOW_PRECEDENCE.equals(descriptorId)) {
			constraint = new PrecedenceConstraint(source, target);
		} else if (DECSERFLOW_RESPONDED_EXISTENCE.equals(descriptorId)) {
			constraint = new RespondedExistenceConstraint(source, target);
		} else if (DECSERFLOW_RESPONSE.equals(descriptorId)) {
			constraint = new ResponseConstraint(source, target);
		} else if (DECSERFLOW_SUCCESSION.equals(descriptorId)) {
			constraint = new SuccessionConstraint(source, target);
		} else if (DECSERFLOW_NEGATION_RESPONSE.equals(descriptorId)) {
			constraint = new NegationResponseConstraint(source, target);
		} else if (DECSERFLOW_LAST.equals(descriptorId)) {
			constraint = new LastConstraint(source);
		} else if (DECSERFLOW_CHAINED_PRECEDENCE.equals(descriptorId)) {
			constraint = new ChainedPrecedenceConstraint(source, target);
		} else if (MULTI_ACTIVITY_CONSTRAINTS.contains(descriptorId)) {
			DeclarativeActivity toAdd = null;
			if (source != null) {
				toAdd = source;
			} else {
				toAdd = target;
			}

			GraphElement edge = graph.getGraphElement(id);
			MultiActivityConstraintDescriptor descriptor = (MultiActivityConstraintDescriptor) EditorRegistry.getDescriptor(descriptorId);

			// either create the constraint or update it accordingly
			if (lastCreatedMultiActivityConstraint == null) {
				lastCreatedMultiActivityConstraint = descriptor.createConstraint();
				constraint = lastCreatedMultiActivityConstraint;
				edge.setProperty(ModelerConstants.ATTRIBUTE_CONSTRAINT_ID, id);

				List<DeclarativeActivity> activities = new ArrayList<DeclarativeActivity>();
				activities.add(toAdd);
				if (source != null) {
					lastCreatedMultiActivityConstraint.setStartActivities(activities);
				} else {
					lastCreatedMultiActivityConstraint.setEndActivities(activities);
				}
			} else {
				if (toAdd.equals(target)) {
					lastCreatedMultiActivityConstraint.addEndActivity(toAdd);
				} else {
					lastCreatedMultiActivityConstraint.addStartActivity(toAdd);
				}
				edge.setProperty(ModelerConstants.ATTRIBUTE_CONSTRAINT_ID, lastCreatedMultiActivityConstraint.getCheetahId());

				return;
			}
		} else {
			throw new IllegalArgumentException("Unsupported constraint type: " + descriptorId);
		}

		constraint.setCheetahId(id);
		schema.addConstraint(constraint);
		lookup.registerObject(NAMESPACE_CONSTRAINTS, constraint);
	}

	private void handleConstraintReconnect(AuditTrailEntry entry) {
		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();
		long sourceId = entry.getLongAttribute(SOURCE_NODE_ID);
		DeclarativeActivity source = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, sourceId);
		long targetId = entry.getLongAttribute(TARGET_NODE_ID);
		DeclarativeActivity target = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, targetId);
		long id = entry.getLongAttribute(ID);
		IDeclarativeConstraint constraint = (IDeclarativeConstraint) lookup.getObject(NAMESPACE_CONSTRAINTS, id);
		String descriptor = entry.getAttribute(DESCRIPTOR);

		if (SINGLE_ACTIVITY_CONSTRAINTS.contains(descriptor)) {
			AbstractDeclarativeConstraintWithOneActivity castedConstraint = (AbstractDeclarativeConstraintWithOneActivity) constraint;
			castedConstraint.setActivity(source);
		} else if (TWO_ACTIVITIES_CONSTRAINTS.contains(descriptor)) {
			AbstractDeclarativeConstraintWithTwoActivities castedConstraint = (AbstractDeclarativeConstraintWithTwoActivities) constraint;
			castedConstraint.setActivity1(source);
			castedConstraint.setActivity2(target);
		} else if (MULTI_ACTIVITY_CONSTRAINTS.contains(descriptor)) {
			// rebuild the constraint
			Edge edge = (Edge) graph.getGraphElement(id);
			long constraintId = (Long) edge.getProperty(ModelerConstants.ATTRIBUTE_CONSTRAINT_ID);
			MultiActivityConstraint casted = (MultiActivityConstraint) lookup.getObject(NAMESPACE_CONSTRAINTS, constraintId);
			casted.clearStartActivities();
			casted.clearEndActivities();
			Node sourceNode = (Node) graph.getGraphElement(sourceId);
			Node auxiliaryNode = null;

			if (sourceNode.getDescriptor() instanceof AuxiliaryNodeDescriptor) {
				auxiliaryNode = sourceNode;
			} else {
				auxiliaryNode = (Node) graph.getGraphElement(targetId);
			}

			for (Edge incomingEdge : auxiliaryNode.getTargetConnections()) {
				DeclarativeActivity startActivity = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, incomingEdge
						.getSource().getId());
				Assert.isNotNull(startActivity);
				casted.addStartActivity(startActivity);
			}
			for (Edge outgoingEdge : auxiliaryNode.getSourceConnections()) {
				DeclarativeActivity endActivity = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, outgoingEdge
						.getTarget().getId());
				Assert.isNotNull(endActivity);
				casted.addEndActivity(endActivity);
			}
		} else {
			throw new IllegalArgumentException("Unsupported constraint: " + descriptor);
		}
	}

	@Override
	public void log(AuditTrailEntry entry) {
		String type = entry.getEventType();
		boolean isGroupStartEvent = GROUP_EVENT_START.equals(type);
		boolean isGroupEndEvent = GROUP_EVENT_END.equals(type);
		if (isGroupStartEvent || isGroupEndEvent) {
			lastCreatedMultiActivityConstraint = null;
			// multi activity constraints are build in several steps, run the tests after all modifications have been performed
			process.setTestsEnabled(isGroupEndEvent);
			process.runAllTests();

			return;
		}

		ICheetahObjectLookup lookup = Services.getCheetahObjectLookup();
		long id = entry.getLongAttribute(ID);

		if (CREATE_NODE.equals(type)) {
			String descriptorId = entry.getAttribute(AbstractGraphCommand.DESCRIPTOR);
			if (descriptorId.equals(EditorRegistry.DECSERFLOW_AUXILIARY_NODE)
					|| descriptorId.equals(EditorRegistry.DECSERFLOW_AUXILIARY_NODE_FOR_MULTI_EXCLUSIVE_CHOICE)) {
				return; // ignore the creation of auxiliary nodes, they are just for visualization purpose
			}

			String name = entry.getAttribute(NAME);
			DeclarativeProcessSchema schema = getSchema();
			DeclarativeActivity activity = schema.createActivity(name);
			activity.setCheetahId(id);
			lookup.registerObject(NAMESPACE_DECLARATIVE_ACTIVITIES, activity);

			GraphElement graphElement = graph.getGraphElement(id);
			Services.getModificationService().addListener(new RedirectListener(activity), graphElement);
		} else if (CREATE_EDGE.equals(type)) {
			handleConstraintCreation(entry);
		} else if (RENAME.equals(type)) {
			String name = entry.getAttribute(NEW_NAME);
			DeclarativeActivity activity = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, id);
			activity.setName(name);
		} else if (EDIT_SELECTION_CONSTRAINT.equals(type)) {
			int minimum = entry.getIntegerAttribute(MINIMUM);
			int maximum = entry.getIntegerAttribute(MAXIMUM);

			SelectionConstraint constraint = (SelectionConstraint) lookup.getObject(NAMESPACE_CONSTRAINTS, id);
			constraint.setMinimum(minimum);
			constraint.setMaximum(maximum);
		} else if (DELETE_EDGE.equals(type)) {
			IDeclarativeConstraint constraint = (IDeclarativeConstraint) lookup.getObject(NAMESPACE_CONSTRAINTS, id);
			lookup.unregisterObject(NAMESPACE_CONSTRAINTS, constraint);
			process.getProcess().removeConstraint(constraint);
		} else if (DELETE_NODE.equals(type)) {
			DeclarativeActivity activity = (DeclarativeActivity) lookup.getObject(NAMESPACE_DECLARATIVE_ACTIVITIES, id);
			lookup.unregisterObject(NAMESPACE_DECLARATIVE_ACTIVITIES, activity);
			process.getProcess().removeActivity(activity);
		} else if (RECONNECT_EDGE.equals(type)) {
			handleConstraintReconnect(entry);
		} else {
			return; // do not run the tests if not necessary
		}

		process.runAllTests();
	}

	/**
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(Graph graph) {
		if (this.graph != null) {
			this.graph.removeLogListener(this);
		}

		this.graph = graph;
		this.graph.addLogListener(this);
	}

	public void setProcess(TDMProcess process) {
		this.process = process;
	}
}
