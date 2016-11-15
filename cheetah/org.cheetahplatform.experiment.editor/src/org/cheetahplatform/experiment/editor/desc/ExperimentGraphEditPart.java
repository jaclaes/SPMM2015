package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.experiment.editor.prop.ExperimentGraphPropertySource;
import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.eclipse.ui.views.properties.IPropertySource;

public class ExperimentGraphEditPart extends GraphEditPart {

	public ExperimentGraphEditPart(IGenericModel model) {
		super(model);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			return new ExperimentGraphPropertySource((ExperimentGraph)getModel());
		}
		return super.getAdapter(adapter);
	}

}
