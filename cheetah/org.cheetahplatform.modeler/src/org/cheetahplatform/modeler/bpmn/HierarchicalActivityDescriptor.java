package org.cheetahplatform.modeler.bpmn;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.generic.figure.GenericActivityFigure;
import org.cheetahplatform.modeler.generic.figure.IGraphElementFigure;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.HierarchicalNode;
import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.swtdesigner.SWTResourceManager;

public class HierarchicalActivityDescriptor extends ActivityDescriptor {
	private static final String SUB_PROCESS = "sub_process";

	public HierarchicalActivityDescriptor() {
		super("img/bpmn/activity.png", "Hierarchical\nActivity", EditorRegistry.BPMN_ACTIVITY_HIERARCHICAL);
	}

	@Override
	public boolean canExecuteCreationCommand(AbstractGraphCommand command, GraphElement element) {
		if (!super.canExecuteCreationCommand(command, element)) {
			return false;
		}
		//
		// Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		//		InputDialog dialog = new InputDialog(shell, "Subprocess Id", "Please enter the subprocess id", "", null); //$NON-NLS-3$
		// if (dialog.open() != Window.OK) {
		// return false;
		// }
		//
		// ((CreateNodeCommand) command).addAttribute(new Attribute(SUB_PROCESS, dialog.getValue().trim()));

		return true;
	}

	@Override
	public IGraphElementFigure createFigure(GraphElement element) {
		String name = element.getName();
		if (name == null) {
			name = ""; //$NON-NLS-1$
		}
		GenericActivityFigure figure = new GenericActivityFigure(name) {
			@Override
			protected Color getBorderColor() {
				return SWTResourceManager.getColor(0, 0, 0);
			}

			@Override
			protected void paintFigure(Graphics graphics) {
				graphics.pushState();

				super.paintFigure(graphics);

				int margin = 3; // margin in pixels of the "plus" inside the box
				int pWidth = 8; // length in pixels of the plus arms
				int boxWidth = margin * 2 + pWidth; // width and height of the square to draw

				Point p1 = new Point(bounds.x + bounds.width / 2 - boxWidth / 2, bounds.y + bounds.height - boxWidth);
				Point p2 = new Point(p1).translate(boxWidth - 1, boxWidth - 1);
				graphics.drawRectangle(new Rectangle(p1, p2));

				p1 = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height - margin - pWidth);
				p2 = new Point(p1);
				p2.translate(0, pWidth);
				graphics.drawLine(p1, p2);

				p1 = new Point(bounds.x + bounds.width / 2 - pWidth / 2, bounds.y + bounds.height - margin - pWidth / 2);
				p2 = new Point(p1);
				p2.translate(pWidth, 0);
				graphics.drawLine(p1, p2);

				graphics.popState();
			}

		};

		figure.setSize(new Dimension(getInitialSize().getSWTPoint()));
		figure.setBackgroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setForegroundColor(SWTResourceManager.getColor(255, 255, 255));
		figure.setLabelColor(SWTResourceManager.getColor(0, 0, 0));

		return figure;
	}

	@Override
	public Node createModel(Graph graph) {
		return new HierarchicalNode(graph, this);
	}

	@Override
	public Node createModel(Graph graph, long id, AuditTrailEntry entry) {
		HierarchicalNode node = new HierarchicalNode(graph, this, id);
		node.setSubProcess(entry.getAttributeSafely(SUB_PROCESS));
		return node;

	}
}
