package org.cheetahplatform.modeler.bpmn;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.bpmn.figure.EndEventFigure;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.geometry.Point;

import com.swtdesigner.SWTResourceManager;

public class EndEventDescriptor extends ConnectorDescriptor {

	public EndEventDescriptor() {
		super("img/bpmn/end_event.png", "End Event", EditorRegistry.BPMN_END_EVENT);
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String name = null;
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)
				|| CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
			name = Long.toString(element.getId());
		}

		EndEventFigure figure = new EndEventFigure(name);
		figure.setBackgroundColor(SWTResourceManager.getColor(255, 255, 255));
		return figure;
	}

	@Override
	public Point getInitialSize() {
		return (Point) CheetahPlatformConfigurator.getObject(IConfiguration.INITIAL_EVENT_SIZE);
	}
}
