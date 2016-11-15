package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class ClusteringDataExporter extends AbstractModelingPhaseExporter {
	class ClusteringAttributeRegistry {
		private Map<String, ContinuousAttribute> attributes;

		public ClusteringAttributeRegistry() {
			attributes = new HashMap<String, ContinuousAttribute>();
		}

		public Attribute getAttribute(String name, Integer index) {
			return attributes.get(name).getAttribute(index);
		}

		public void register(String name, Attribute attribute, int index) {
			if (!attributes.containsKey(name)) {
				attributes.put(name, new ContinuousAttribute());
			}

			ContinuousAttribute continuousAttribute = attributes.get(name);
			continuousAttribute.register(attribute, index);
		}
	}

	class ContinuousAttribute {
		private Map<Integer, Attribute> attributes;

		public ContinuousAttribute() {
			attributes = new HashMap<Integer, Attribute>();
		}

		public Attribute getAttribute(int index) {
			return attributes.get(index);
		}

		public void register(Attribute attribute, int index) {
			if (attributes.containsKey(index)) {
				throw new IllegalArgumentException("Attribute already registered");
			}
			attributes.put(index, attribute);
		}
	}

	private static final String ADD_ATTRIBUTE = "Added";
	private static final String REMOVE_ATTRIBUTE = "Deleted";
	private static final String LAYOUT_ATTRIBUTE = "Layout";
	private static final String COMP_TIME_ATTRIBUTE = "CompTime";

	private File target;

	private Map<String, List<SlidingWindow>> slidingWindows;

	public void createAttribute(ClusteringAttributeRegistry registry, FastVector attributes, int i, String name) {
		Attribute removeAttribute = new Attribute(name + "_" + i);
		registry.register(name, removeAttribute, i);
		attributes.addElement(removeAttribute);
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle modelingInstance, AuditTrailEntry entry) {
		// if (!modelingInstance.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS).equals("modeling_task1_without_layout_1.0"))
		// {
		// return;
		// }
		if (!modelingInstance.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS).equals("nve_modeling_task2_1.0")) {
			return;
		}
		// if (!modelingInstance.getInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS).equals("equipment_transport_1.0")) {
		// return;
		// }

		super.doExportModelingProcessInstance(modelingInstance, entry);

		PhaseSimilarityCalculator calculator = new PhaseSimilarityCalculator(new FixedDurationsPhaseSimilarityCalculationStrategy(10000,
				10000));
		slidingWindows.put(modelingInstance.getId(), calculator.calculate(modelingInstance.getInstance(), getChunks(), getIterations()));
	}

	@Override
	public void exportFinished() {
		// if (getMaximumSlidingWindowSize() > length) {
		// throw new IllegalStateException("Too long!");
		// }

		// int length = getMaximumSlidingWindowSize();
		// int length = 298;
		int length = 265;

		ClusteringAttributeRegistry registry = new ClusteringAttributeRegistry();

		FastVector attributes = new FastVector();

		Attribute modelIdAttribute = new Attribute("ModelId");
		attributes.addElement(modelIdAttribute);

		for (int i = 0; i < length; i++) {
			createAttribute(registry, attributes, i, ADD_ATTRIBUTE);
		}
		for (int i = 0; i < length; i++) {
			createAttribute(registry, attributes, i, REMOVE_ATTRIBUTE);
		}
		for (int i = 0; i < length; i++) {
			createAttribute(registry, attributes, i, LAYOUT_ATTRIBUTE);
		}
		for (int i = 0; i < length; i++) {
			createAttribute(registry, attributes, i, COMP_TIME_ATTRIBUTE);
		}

		Instances instances = new Instances("ModelingProcesses", attributes, slidingWindows.size());

		for (Entry<String, List<SlidingWindow>> entry : slidingWindows.entrySet()) {
			Instance instance = new Instance(instances.numAttributes());
			instance.setValue(modelIdAttribute, Long.parseLong(entry.getKey()));

			List<SlidingWindow> value = entry.getValue();
			for (int i = 0; i < length; i++) {
				if (i >= value.size()) {
					instance.setValue(registry.getAttribute(ADD_ATTRIBUTE, i), 0);
					instance.setValue(registry.getAttribute(REMOVE_ATTRIBUTE, i), 0);
					instance.setValue(registry.getAttribute(LAYOUT_ATTRIBUTE, i), 0);

					if (value.isEmpty()) {
						instance.setValue(registry.getAttribute(COMP_TIME_ATTRIBUTE, i), -1);
					} else {
						instance.setValue(registry.getAttribute(COMP_TIME_ATTRIBUTE, i), value.get(0).getDuration());
					}
					continue;
				}
				instance.setValue(registry.getAttribute(ADD_ATTRIBUTE, i), value.get(i).getNumberOfAddEventsForClustering());
				instance.setValue(registry.getAttribute(REMOVE_ATTRIBUTE, i), value.get(i).getNumberOfDeleteEventsForClustering());
				instance.setValue(registry.getAttribute(LAYOUT_ATTRIBUTE, i), value.get(i).getNumberOfLayoutEventsForClustering());
				instance.setValue(registry.getAttribute(COMP_TIME_ATTRIBUTE, i), value.get(i).getTimeOfComprehension());
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

	private int getMaximumSlidingWindowSize() {
		int size = 0;
		for (List<SlidingWindow> window : slidingWindows.values()) {
			if (window.size() > size) {
				size = window.size();
			}
		}
		return size;
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
		slidingWindows = new HashMap<String, List<SlidingWindow>>();
	};
}
