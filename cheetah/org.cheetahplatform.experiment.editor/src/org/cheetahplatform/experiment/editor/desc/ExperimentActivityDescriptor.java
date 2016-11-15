package org.cheetahplatform.experiment.editor.desc;

import org.cheetahplatform.experiment.editor.prop.NameHelper;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public abstract class ExperimentActivityDescriptor extends NodeDescriptor {

	private NameHelper nameHelper;
	
	public ExperimentActivityDescriptor(String imagePath, String name, String id) {
		super(imagePath, name, id);
		nameHelper = new NameHelper();
	}

	@Override
	public Point getInitialSize() {
		return (Point) CheetahPlatformConfigurator
				.getObject(IConfiguration.INITIAL_ACTIVITIY_SIZE);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public void updateName(IFigure figure, String name) {
		if (name == null) {
			name = "";
		}

		((GenericActivityFigure) figure).setName(name);
	}

	public NameHelper getNameHelper() {
		return nameHelper;
	}
	
	

}
