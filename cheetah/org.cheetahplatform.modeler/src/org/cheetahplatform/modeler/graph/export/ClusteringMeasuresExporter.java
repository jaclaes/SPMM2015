package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ClusteringMeasuresExporter extends AbstractModelingPhaseExporter {
	private static final String NUMBER_OF_LAYOUT_EVENTS = "Number of Layout Events";
	private static final String NUMBER_OF_DELETE_EVENTS = "Number of Delete Events";
	private static final String NUMBER_OF_ADD_EVENTS = "Number of Add Events";
	private File target;
	private Map<String, Map<String, Double>> results;
	private List<IPpmStatistic> statistics;
	private HashMap<String, Attribute> attributeRegistry;

	public void createAttribute(FastVector attributes, String name) {
		Attribute attribute = new Attribute(name);
		attributes.addElement(attribute);
		attributeRegistry.put(name, attribute);
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		if (!modelingInstance.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS).equals("nve_modeling_task2_1.0")) {
			return;
		}

		super.doExportModelingProcessInstance(modelingInstance, entry);
		HashMap<String, Double> values = new HashMap<String, Double>();

		for (IPpmStatistic statistic : statistics) {
			String value = statistic.getValue(modelingInstance.getInstance(), getChunks(), getIterations());
			value = value.replaceAll(",", ".");
			results.put(modelingInstance.getId(), values);
			values.put(statistic.getHeader(), Double.parseDouble(value));
		}

		SlidingWindow window = new SlidingWindow(0, 100000);
		List<AuditTrailEntry> entries = modelingInstance.getInstance().getEntries();
		for (AuditTrailEntry auditTrailEntry : entries) {
			window.addAuditTrailEntry(auditTrailEntry);
		}

		// values.put(NUMBER_OF_ADD_EVENTS, (double) window.getNumberOfAddEventsForClustering());
		// values.put(NUMBER_OF_DELETE_EVENTS, (double) window.getNumberOfDeleteEventsForClustering());
		// values.put(NUMBER_OF_LAYOUT_EVENTS, (double) window.getNumberOfLayoutEventsForClustering());
	}

	@Override
	public void exportFinished() {
		FastVector attributes = new FastVector();

		Attribute modelIdAttribute = new Attribute("ModelId");
		attributes.addElement(modelIdAttribute);

		for (IPpmStatistic statistic : statistics) {
			createAttribute(attributes, statistic.getHeader());
		}

		// createAttribute(attributes, NUMBER_OF_ADD_EVENTS);
		// createAttribute(attributes, NUMBER_OF_DELETE_EVENTS);
		// createAttribute(attributes, NUMBER_OF_LAYOUT_EVENTS);

		Instances instances = new Instances("ProcessesOfProcessModeling", attributes, results.size());
		for (Entry<String, Map<String, Double>> process : results.entrySet()) {
			Instance instance = new Instance(instances.numAttributes());
			instance.setValue(modelIdAttribute, Long.parseLong(process.getKey()));

			for (Entry<String, Double> values : process.getValue().entrySet()) {
				instance.setValue(attributeRegistry.get(values.getKey()), values.getValue());
			}
			instances.add(instance);
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(target));
			writer.write(instances.toString());
			writer.close();
		} catch (IOException e) {
			Activator.logError("Unable to write file", e);
		}
		super.exportFinished();
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
		results = new HashMap<String, Map<String, Double>>();

		attributeRegistry = new HashMap<String, Attribute>();

		statistics = new ArrayList<IPpmStatistic>();
		statistics.add(new AverageIterationChunkSizeStatistic());
		statistics.add(new NumberOfIterationsStatistic());
		statistics.add(new ShareOfComprehensionStatistic());
		statistics.add(new ReconciliationBreakStatistic());
		statistics.add(new DeleteIterationsStatistic());
	}
}
