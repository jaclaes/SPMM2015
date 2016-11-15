package org.cheetahplatform.modeler.dialog;

import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_PROCESS;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.IExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ProcessInstanceDatabaseHandleLabelProvider extends LabelProvider implements ITableLabelProvider {
	private final IExtraColumnProvider extraColumnProvider;

	public ProcessInstanceDatabaseHandleLabelProvider(IExtraColumnProvider extraColumnProvider) {
		this.extraColumnProvider = extraColumnProvider;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null; // no images
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ExperimentalWorkflowElementDatabaseHandle) {
			ExperimentalWorkflowElementDatabaseHandle model = (ExperimentalWorkflowElementDatabaseHandle) element;

			if (columnIndex == 0) {
				return SelectProcessInstanceModel.FORMAT.format(model.getTimeAsDate());
			}
			if (columnIndex == 1) {
				if (model.isAttributeDefined(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE)) {
					return model.getAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE);
				}
			}
			if (columnIndex == 2) {
				return "";
			}
			if (columnIndex == 3) {
				return model.getType();
			}

			return "";
		}

		ProcessInstanceDatabaseHandle model = (ProcessInstanceDatabaseHandle) element;

		if (columnIndex == 0) {
			return SelectProcessInstanceModel.FORMAT.format(model.getTimeAsDate());
		}
		if (columnIndex == 1) {
			return model.getId();
		}
		if (columnIndex == 2) {
			return model.getHost();
		}
		if (columnIndex == 3) {
			String id = model.getAttribute(ATTRIBUTE_PROCESS);
			if (!id.isEmpty()) {
				return id;
			}

			return model.getProcessId();
		}

		return extraColumnProvider.getColumnText(model, columnIndex);
	}
}