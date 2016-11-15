package org.cheetahplatform.modeler.graph.export;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.OperationSpanActivity;

public class OperationSpanComputation extends AbstractExportComputation {
	public static final String OPERATION_SPAN = "Operation Span";

	@Override
	public List<Attribute> computeForModelingProcessInstance(ProcessInstanceDatabaseHandle handle) {
		ProcessInstance instance = handle.getInstance();
		String type = instance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		if (!type.equals(OperationSpanActivity.TYPE_OPERATION_SPAN)) {
			return Collections.emptyList();
		}

		OperationSpanIncrementalComputation comp = new OperationSpanIncrementalComputation();

		comp.add(instance.getEntries().iterator());

		DecimalFormat decimalFormat = new DecimalFormat("##.00");
		List<Attribute> attributes = new ArrayList<Attribute>();
		double result = comp.getRSP();
		attributes.add(new Attribute(OPERATION_SPAN, decimalFormat.format(result)));
		return attributes;
	}
}
