package org.cheetahplatform.modeler.graph.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.modeler.dialog.StatisticsSelectionModel;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.semanticalcorrectness.SemanticalCorrectnessComputation;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PpmMetricModel {
	private class ExtractAttributeMetric implements IPpmMetric {
		private String name;
		private String attributeId;

		public ExtractAttributeMetric(String name, String attributeId) {
			this.name = name;
			this.attributeId = attributeId;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			ProcessInstance processInstance = replayModel.getProcessInstance();
			if (processInstance.isAttributeDefined(attributeId)) {
				return processInstance.getAttribute(attributeId);
			}
			return IPpmStatistic.N_A;
		}

	}

	private interface IPpmMetric {
		String getName();

		String getValue();
	}

	private class SemanticalCorrectnessEvaluationMetric implements IPpmMetric {
		private String value;

		private String calculate() {
			SemanticalCorrectnessComputation computation = new SemanticalCorrectnessComputation();
			List<Attribute> attributes = computation.computeForModelingProcessInstance(replayModel.getProcessInstanceDatabaseHandle());
			for (Attribute attribute : attributes) {
				if (attribute.getName().equals(SemanticalCorrectnessComputation.SEMANTICAL_CORRECTNESS)) {
					return attribute.getContent();
				}
			}
			return IPpmStatistic.N_A;
		}

		@Override
		public String getName() {
			return SemanticalCorrectnessComputation.SEMANTICAL_CORRECTNESS;
		}

		@Override
		public String getValue() {
			if (value == null) {
				value = calculate();
			}
			return value;
		}
	}

	private class StatisticLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			IPpmMetric metric = (IPpmMetric) element;
			if (columnIndex == 0) {
				return metric.getName();
			}
			return metric.getValue();
		}
	}

	private class StatisticPpmMetric implements IPpmMetric {
		private final IPpmStatistic statistic;

		public StatisticPpmMetric(IPpmStatistic statistic) {
			this.statistic = statistic;
		}

		@Override
		public String getName() {
			return statistic.getHeader();
		}

		@Override
		public String getValue() {
			return statistic.getValue(
					replayModel.getProcessInstance(),
					modelingPhaseModel.getChunks(modelingPhaseModel.getDefaultDetectionStrategy(),
							modelingPhaseModel.getDefaultComprehensionThreshold(),
							modelingPhaseModel.getDefaultComprehensionAggregationThreshold()), modelingPhaseModel.getIterations());
		}
	}

	private ModelingPhaseModel modelingPhaseModel;

	private final ReplayModel replayModel;
	private StatisticsSelectionModel statisticsSelectionModel;

	public PpmMetricModel(ReplayModel replayModel) {
		Assert.isNotNull(replayModel);
		this.replayModel = replayModel;
		this.modelingPhaseModel = new ModelingPhaseModel(replayModel);
		this.statisticsSelectionModel = new StatisticsSelectionModel();
	}

	public LabelProvider createLabelProvider() {
		return new StatisticLabelProvider();
	}

	public String getModelId() {
		return replayModel.getProcessInstance().getId();
	}

	public List<IPpmMetric> getPpmMetrics() {
		List<IPpmMetric> metrics = new ArrayList<IPpmMetric>();
		List<IPpmStatistic> availableStatistics = statisticsSelectionModel.getAvailableStatistics();
		for (IPpmStatistic iPpmStatistic : availableStatistics) {
			if (iPpmStatistic.isActive()) {
				metrics.add(new StatisticPpmMetric(iPpmStatistic));
			}
		}
		metrics.add(new SemanticalCorrectnessEvaluationMetric());
		metrics.add(new ExtractAttributeMetric("GED Similarity", CommonConstants.ATTRIBUTE_GRAPH_EDIT_DISTANCE));
		metrics.add(new ExtractAttributeMetric("Behav. Profile Sim All", CommonConstants.ATTRIBUTE_BEHAVORIAL_PROFILES_SIMILARITY_ALL));
		metrics.add(new ExtractAttributeMetric("Behav. Profile Sim Base", CommonConstants.ATTRIBUTE_BEHAVIORAL_PROFILES_SIMILARITY_BASE));

		return metrics;
	}

	public String getProcess() {
		if (replayModel.getProcessInstance().isAttributeDefined(CommonConstants.ATTRIBUTE_PROCESS)) {
			return replayModel.getProcessInstance().getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		}
		return "";
	}

	public String getWorkflowId() {
		String id = replayModel.getProcessInstance().getId();
		return new ExperimentalWorkflowIdentifier().getWorkflowId(id);
	}
}
