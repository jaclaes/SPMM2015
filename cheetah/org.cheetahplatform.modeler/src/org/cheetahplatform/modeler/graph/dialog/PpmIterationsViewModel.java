package org.cheetahplatform.modeler.graph.dialog;

import java.util.List;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.Chunk;
import org.cheetahplatform.modeler.graph.model.ModelingPhaseModel;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

public class PpmIterationsViewModel {

	private class PpmIterationContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Chunk) {
				Chunk chunk = (Chunk) parentElement;
				return chunk.getEntries().toArray();
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof Chunk) {
				Chunk chunk = (Chunk) element;
				return !chunk.getEntries().isEmpty();
			}
			return false;
		}
	}

	private class PpmIterationLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Chunk) {
				return ((Chunk) element).getType();
			}

			return AbstractGraphCommand.getCommandLabel((AuditTrailEntry) element);
		}
	}

	private ModelingPhaseModel modelingPhaseModel;
	private ReplayModel replaymodel;

	public PpmIterationsViewModel() {
		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		IAdaptable adaptable = (IAdaptable) selection.getFirstElement();
		modelingPhaseModel = (ModelingPhaseModel) adaptable.getAdapter(ModelingPhaseModel.class);
		replaymodel = (ReplayModel) adaptable.getAdapter(ReplayModel.class);
	}

	public ITreeContentProvider createContentProvider() {
		return new PpmIterationContentProvider();
	}

	public ITableLabelProvider createLabelProvider() {
		return new PpmIterationLabelProvider();
	}

	public List<Chunk> getChunks() {
		return modelingPhaseModel.getChunks(modelingPhaseModel.getDefaultDetectionStrategy(),
				modelingPhaseModel.getDefaultComprehensionThreshold(), modelingPhaseModel.getDefaultComprehensionAggregationThreshold());
	}

	public void jumpTo(AuditTrailEntry auditTrailEntry) throws Exception {
		CommandDelegate commandDelegate = replaymodel.findCommandDelegate(auditTrailEntry);
		replaymodel.executeTo(new StructuredSelection(commandDelegate));
	}
}
