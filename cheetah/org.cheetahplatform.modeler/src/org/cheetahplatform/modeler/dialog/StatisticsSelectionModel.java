package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.modeler.graph.export.AddingRateStatistics;
import org.cheetahplatform.modeler.graph.export.AverageDurationBetweenCreationAndLastMoveStatistic;
import org.cheetahplatform.modeler.graph.export.AverageDurationToLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.AverageIterationChunkSizeStatistic;
import org.cheetahplatform.modeler.graph.export.AverageIterationDurationStatistic;
import org.cheetahplatform.modeler.graph.export.AverageModelingChunkSizeStatistic;
import org.cheetahplatform.modeler.graph.export.AverageNumberOfDeleteOperationsStatistic;
import org.cheetahplatform.modeler.graph.export.AverageNumberOfMovesPerNodeStatistic;
import org.cheetahplatform.modeler.graph.export.AverageNumberOfOperationsPerIteration;
import org.cheetahplatform.modeler.graph.export.AverageReconciliationPhaseSizeStatistic;
import org.cheetahplatform.modeler.graph.export.AverageTimeAfterLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.AvgNumberOfLayoutedEdgesInReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.AvgNumberOfMovedElementsPerReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.AvgNumberOfTouchedElementsInReconciliationPhaseStatistc;
import org.cheetahplatform.modeler.graph.export.AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic;
import org.cheetahplatform.modeler.graph.export.ContentAddingDistributionStatistic;
import org.cheetahplatform.modeler.graph.export.DeleteIterationsStatistic;
import org.cheetahplatform.modeler.graph.export.DurationBetweenLayoutAndUndoStatistic;
import org.cheetahplatform.modeler.graph.export.DurationToFirstInteractionStatistic;
import org.cheetahplatform.modeler.graph.export.IPpmStatistic;
import org.cheetahplatform.modeler.graph.export.InitialComprehensionPhaseDurationStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutContinuumDurationStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutContinuumModelingStepsStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutContinuumPpmStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutDurationStatistic;
import org.cheetahplatform.modeler.graph.export.LayoutIterationStatistic;
import org.cheetahplatform.modeler.graph.export.MaxDurationBetweenCreationAndLastMoveStatistic;
import org.cheetahplatform.modeler.graph.export.MaxIterationChunkSizeStatistic;
import org.cheetahplatform.modeler.graph.export.MaxModelingChunkSizeStatistic;
import org.cheetahplatform.modeler.graph.export.MaxNumberOfLayoutedEdgesInReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.MaxNumberOfMovedElementsPerReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.MaxNumberOfMovesStatistic;
import org.cheetahplatform.modeler.graph.export.MaxNumberOfTouchedElementsInReconciliationPhaseStatistc;
import org.cheetahplatform.modeler.graph.export.MaxReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.MaxShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic;
import org.cheetahplatform.modeler.graph.export.MinDurationBetweenCreationAndLastMoveStatistic;
import org.cheetahplatform.modeler.graph.export.MinNumberOfLayoutedEdgesInReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.MinNumberOfMovedElementsPerReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.MinNumberOfMovesStatistic;
import org.cheetahplatform.modeler.graph.export.MinNumberOfTouchedElementsInReconciliationPhaseStatistc;
import org.cheetahplatform.modeler.graph.export.MinReconciliationPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.NodeEdgeAlternationStatistic;
import org.cheetahplatform.modeler.graph.export.NumberOfCompoundCommandsStatistic;
import org.cheetahplatform.modeler.graph.export.NumberOfComprehensionPhasesStatistc;
import org.cheetahplatform.modeler.graph.export.NumberOfIterationsStatistic;
import org.cheetahplatform.modeler.graph.export.NumberOfModelingPhasesStatistic;
import org.cheetahplatform.modeler.graph.export.NumberOfReconciliationPhasesStatistic;
import org.cheetahplatform.modeler.graph.export.ProcessSteepnessStatistic;
import org.cheetahplatform.modeler.graph.export.ReconciliationBreakContinuumIterationStatistic;
import org.cheetahplatform.modeler.graph.export.ReconciliationBreakStatistic;
import org.cheetahplatform.modeler.graph.export.RenamingStatistics;
import org.cheetahplatform.modeler.graph.export.ShareOfComprehensionIncludingInitialPhaseStatistic;
import org.cheetahplatform.modeler.graph.export.ShareOfComprehensionStatistic;
import org.cheetahplatform.modeler.graph.export.StdDevOfDurationToLayout;
import org.cheetahplatform.modeler.graph.export.StdDevOfLayoutOperationsPerIteration;
import org.cheetahplatform.modeler.graph.export.SuccessfulLayoutCounterStatistic;
import org.cheetahplatform.modeler.graph.export.ThreePhaseIterationsStatistic;
import org.cheetahplatform.modeler.graph.export.TimesAfterLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.TotalComprehensionDurationStatistic;
import org.cheetahplatform.modeler.graph.export.TotalModelingDurationStatistic;
import org.cheetahplatform.modeler.graph.export.TotalReconciliationDurationStatistic;
import org.cheetahplatform.modeler.graph.export.UndoLayoutStatistic;
import org.cheetahplatform.modeler.graph.export.UnsuccesfulLayoutStatistic;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;

public class StatisticsSelectionModel {
	private class StatisticFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer arg0, Object arg1, Object arg2) {
			return ((IPpmStatistic) arg2).isActive();
		}
	}

	private class StatisticsLableProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object arg0, int arg1) {
			return null;
		}

		@Override
		public String getColumnText(Object arg0, int arg1) {
			return ((IPpmStatistic) arg0).getName();
		}
	}

	private class StatisticsSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			IPpmStatistic statitistic1 = (IPpmStatistic) e1;
			IPpmStatistic statitistic2 = (IPpmStatistic) e2;

			return statitistic1.getName().compareTo(statitistic2.getName());
		}
	}

	private static List<IPpmStatistic> AVAILABE_STATISTICS = new ArrayList<IPpmStatistic>();

	static {
		AVAILABE_STATISTICS.add(new ThreePhaseIterationsStatistic());
		AVAILABE_STATISTICS.add(new AverageIterationChunkSizeStatistic());
		AVAILABE_STATISTICS.add(new AverageModelingChunkSizeStatistic());
		AVAILABE_STATISTICS.add(new ReconciliationBreakStatistic());
		AVAILABE_STATISTICS.add(new NumberOfIterationsStatistic());
		AVAILABE_STATISTICS.add(new ShareOfComprehensionStatistic());
		AVAILABE_STATISTICS.add(new DeleteIterationsStatistic());
		AVAILABE_STATISTICS.add(new TotalComprehensionDurationStatistic());
		AVAILABE_STATISTICS.add(new TotalModelingDurationStatistic());
		AVAILABE_STATISTICS.add(new TotalReconciliationDurationStatistic());
		AVAILABE_STATISTICS.add(new SuccessfulLayoutCounterStatistic());
		AVAILABE_STATISTICS.add(new UnsuccesfulLayoutStatistic());
		AVAILABE_STATISTICS.add(new UndoLayoutStatistic());
		AVAILABE_STATISTICS.add(new DurationBetweenLayoutAndUndoStatistic());
		AVAILABE_STATISTICS.add(new LayoutDurationStatistic());
		AVAILABE_STATISTICS.add(new TimesAfterLayoutStatistic());
		AVAILABE_STATISTICS.add(new AverageTimeAfterLayoutStatistic());
		AVAILABE_STATISTICS.add(new LayoutContinuumDurationStatistic());
		AVAILABE_STATISTICS.add(new LayoutContinuumModelingStepsStatistic());
		AVAILABE_STATISTICS.add(new LayoutContinuumPpmStatistic());
		AVAILABE_STATISTICS.add(new RenamingStatistics());
		AVAILABE_STATISTICS.add(new SyntaxStatistic());
		AVAILABE_STATISTICS.add(new MaxIterationChunkSizeStatistic());
		AVAILABE_STATISTICS.add(new MaxModelingChunkSizeStatistic());
		AVAILABE_STATISTICS.add(new AverageNumberOfDeleteOperationsStatistic());
		AVAILABE_STATISTICS.add(new AverageNumberOfOperationsPerIteration());
		AVAILABE_STATISTICS.add(new ProcessSteepnessStatistic());
		AVAILABE_STATISTICS.add(new AverageIterationDurationStatistic());
		AVAILABE_STATISTICS.add(new ContentAddingDistributionStatistic());
		AVAILABE_STATISTICS.add(new AverageDurationToLayoutStatistic());
		AVAILABE_STATISTICS.add(new StdDevOfDurationToLayout());
		AVAILABE_STATISTICS.add(new LayoutIterationStatistic());
		AVAILABE_STATISTICS.add(new NodeEdgeAlternationStatistic());
		AVAILABE_STATISTICS.add(new StdDevOfLayoutOperationsPerIteration());
		AVAILABE_STATISTICS.add(new InitialComprehensionPhaseDurationStatistic());
		AVAILABE_STATISTICS.add(new ShareOfComprehensionIncludingInitialPhaseStatistic());
		AVAILABE_STATISTICS.add(new AddingRateStatistics());
		AVAILABE_STATISTICS.add(new MinReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new MaxReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new NumberOfComprehensionPhasesStatistc());
		AVAILABE_STATISTICS.add(new NumberOfModelingPhasesStatistic());
		AVAILABE_STATISTICS.add(new NumberOfReconciliationPhasesStatistic());
		AVAILABE_STATISTICS.add(new AverageReconciliationPhaseSizeStatistic());
		AVAILABE_STATISTICS.add(new MinNumberOfMovesStatistic());
		AVAILABE_STATISTICS.add(new MaxNumberOfMovesStatistic());
		AVAILABE_STATISTICS.add(new AverageNumberOfMovesPerNodeStatistic());
		AVAILABE_STATISTICS.add(new MinDurationBetweenCreationAndLastMoveStatistic());
		AVAILABE_STATISTICS.add(new MaxDurationBetweenCreationAndLastMoveStatistic());
		AVAILABE_STATISTICS.add(new AverageDurationBetweenCreationAndLastMoveStatistic());
		AVAILABE_STATISTICS.add(new NumberOfCompoundCommandsStatistic());
		AVAILABE_STATISTICS.add(new ReconciliationBreakContinuumIterationStatistic());
		AVAILABE_STATISTICS.add(new MinNumberOfMovedElementsPerReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new MaxNumberOfMovedElementsPerReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new AvgNumberOfMovedElementsPerReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new MinNumberOfLayoutedEdgesInReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new MaxNumberOfLayoutedEdgesInReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new AvgNumberOfLayoutedEdgesInReconciliationPhaseStatistic());
		AVAILABE_STATISTICS.add(new MinNumberOfTouchedElementsInReconciliationPhaseStatistc());
		AVAILABE_STATISTICS.add(new MaxNumberOfTouchedElementsInReconciliationPhaseStatistc());
		AVAILABE_STATISTICS.add(new AvgNumberOfTouchedElementsInReconciliationPhaseStatistc());
		AVAILABE_STATISTICS.add(new MaxShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic());
		AVAILABE_STATISTICS.add(new AvgShareOfCreatedElementsTouchedInUpcomingRecPhaseStatisic());
		AVAILABE_STATISTICS.add(new DurationToFirstInteractionStatistic());
	}

	public IContentProvider createContentProvider() {
		return new ArrayContentProvider();
	}

	public ViewerFilter createFilter() {
		return new StatisticFilter();
	}

	public IBaseLabelProvider createLabelProvider() {
		return new StatisticsLableProvider();
	}

	public ViewerSorter createSorter() {
		return new StatisticsSorter();
	}

	public List<IPpmStatistic> getAvailableStatistics() {
		return Collections.unmodifiableList(AVAILABE_STATISTICS);
	}
}
