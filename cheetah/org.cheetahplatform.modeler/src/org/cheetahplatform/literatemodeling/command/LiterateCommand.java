package org.cheetahplatform.literatemodeling.command;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public abstract class LiterateCommand extends AbstractGraphCommand {
	private static final NodeDescriptor dummyDescriptor = new NodeDescriptor("img/bpmn/activity.png", "Activity",
			EditorRegistry.BPMN_ACTIVITY) {

		@Override
		public IGraphElementFigure createFigure(GraphElement element) {
			return null;
		}

		@Override
		public Point getInitialSize() {
			return new Point(10, 10);
		}

		@Override
		public boolean hasCustomName() {
			return false;
		}

		@Override
		public void updateName(IFigure figure, String name) {
			// nothing to do
		}
	};

	protected boolean preExecuted;

	public LiterateCommand(Graph graph, boolean preExecuted) {
		super(graph, dummyDescriptor.createModel(graph));
		this.preExecuted = preExecuted;
	}

	public boolean isPreExecuted() {
		return preExecuted;
	}

}
