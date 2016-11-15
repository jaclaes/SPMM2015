package org.cheetahplatform.tdm.modeler.test;

import static org.cheetahplatform.modeler.ModelerConstants.PROPERTY_BACKGROUND_COLOR;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.core.common.IIdentifiableObject;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.core.service.SimpleCheetahServiceLookup;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.daily.model.Activity;
import org.cheetahplatform.tdm.daily.model.ExecutionAssertion;
import org.cheetahplatform.tdm.daily.model.TerminationAssertion;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.engine.ITDMStep;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.cheetahplatform.tdm.modeler.declarative.IdentifiableGraph;

public class TDMTestEditorModel implements ILogListener {

	public static Graph getGraph() {
		List<IIdentifiableObject> graphs = Services.getCheetahObjectLookup().getObjectsFromNamespace(
				SimpleCheetahServiceLookup.NAMESPACE_GRAPH);
		Assert.isTrue(graphs.size() == 1);
		IdentifiableGraph wrappedGraph = (IdentifiableGraph) graphs.get(0);
		Graph graph = wrappedGraph.getGraph();
		return graph;
	}

	private TDMTest test;
	private Workspace workspace;
	private final IPromLogger logger;

	private String currentTitleImageUrl;

	public TDMTestEditorModel(TDMTest test, Workspace workspace, IPromLogger logger) {
		Assert.isNotNull(logger);

		this.test = test;
		this.logger = logger;
		if (workspace != null) {
			this.workspace = workspace;
		} else {
			DeclarativeProcessSchema dummySchema = new DeclarativeProcessSchema();
			DeclarativeProcessInstance dummyInstance = dummySchema.instantiate();
			dummyInstance.setStartTime(new DateTime(0, 0, true));
			this.workspace = new Workspace(dummyInstance);
		}

		this.workspace.setTest(test);
		this.workspace.addLogListener(this);
		this.currentTitleImageUrl = "img/tdm/test_passed.gif";
	}

	public void clearConstraintHighlighting() {
		List<Activity> toClear = new ArrayList<Activity>();
		toClear.addAll(workspace.getActivities());
		toClear.addAll(workspace.getExecutionAssertions());
		toClear.addAll(workspace.getTerminationAssertions());

		for (Activity activity : toClear) {
			ITDMStep step = test.getFailure(activity);
			if (step != null) {
				// a constraint may consist of several graph elements (e.g., multi precedence)
				List<GraphElement> constraint = step.getFailure().getModelElementCausingFailure();
				for (GraphElement element : constraint) {
					element.setProperty(PROPERTY_BACKGROUND_COLOR, null);
				}
			}
		}
	}

	public void clearModelHighlighting() {
		Graph graph = getGraph();
		for (Edge edge : graph.getEdges()) {
			edge.setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, null);
		}
	}

	public void dispose() {
		workspace.removeLoggListener(this);
	}

	protected String getCurrentTitleImageUrl() {
		return currentTitleImageUrl;
	}

	public TDMProcess getTDMProcess() {
		return test.getProcess();
	}

	public TDMTest getTest() {
		return test;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public boolean isTestDeleted() {
		return !getTDMProcess().contains(test);
	}

	@Override
	public void log(AuditTrailEntry entry) {
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, test.getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, test.getName());
		logger.log(entry);
	}

	protected void setCurrentTitleImageUrl(String currentTitleImageUrl) {
		this.currentTitleImageUrl = currentTitleImageUrl;
	}

	public void updateActivityColoring() {
		for (Activity activity : workspace.getActivities()) {
			activity.setBackgroundColor(null);
			activity.firePropertyChanged(TDMConstants.PROPERTY_SELECTION);
			activity.setTooltip(activity.getName());
		}
		for (ExecutionAssertion assertion : workspace.getExecutionAssertions()) {
			assertion.setBackgroundColor(null);
			assertion.firePropertyChanged(TDMConstants.PROPERTY_SELECTION);
			assertion.setTooltip(assertion.getName());
		}
		for (TerminationAssertion assertion : workspace.getTerminationAssertions()) {
			assertion.setBackgroundColor(null);
			assertion.firePropertyChanged(TDMConstants.PROPERTY_SELECTION);
			assertion.setTooltip(assertion.getName());
		}

		for (ITDMStep step : test.getFailures()) {
			step.getFailure().visualizeFailureInTest();
		}
	}

	public void updateTestFromWorkspace() {
		test.reset();

		for (Activity activity : workspace.getActivities()) {
			test.addActivity(activity);
		}
		for (ExecutionAssertion assertion : workspace.getExecutionAssertions()) {
			test.addExecutionAssertion(assertion);
		}
		for (TerminationAssertion assertion : workspace.getTerminationAssertions()) {
			test.addTerminationAssertion(assertion);
		}

		test.run();
	}

}
