package org.cheetahplatform.modeler.hierarchical;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class HierarchicalView extends ViewPart {

	public static final String ID = "org.cheetahplatform.modeler.hierarchical.HierarchicalView";
	public static final String INPUT_CHANGED = "INPUT_CHANGED";
	private Composite parent;
	private Composite composite;
	private List<ILogListener> logListeners;
	private OutlineViewNode<Graph> currInputNode;
	private Set<Long> backgroundColorsToClear;

	public HierarchicalView() {
		this.backgroundColorsToClear = new HashSet<Long>();
	}

	public void addLogListener(ILogListener listener) {
		logListeners.add(listener);
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		this.logListeners = new ArrayList<ILogListener>();
	}

	protected void logInputChanged(OutlineViewNode<Graph> oldInput, OutlineViewNode<Graph> newInput) {
		AuditTrailEntry entry = new AuditTrailEntry(INPUT_CHANGED);
		String oldModel = oldInput != null ? oldInput.getId() : "null";
		String newModel = newInput != null ? newInput.getId() : "null";
		entry.setAttribute("from", oldModel);
		entry.setAttribute("to", newModel);

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	protected void logScroll(String type, RangeModel rangeModel) {
		AuditTrailEntry entry = new AuditTrailEntry(type);
		entry.setAttribute(AbstractGraphCommand.SCROLL_MIN, rangeModel.getMinimum());
		entry.setAttribute(AbstractGraphCommand.SCROLL_MAX, rangeModel.getMaximum());
		entry.setAttribute(AbstractGraphCommand.SCROLL_EXTENT, rangeModel.getExtent());
		entry.setAttribute(AbstractGraphCommand.SCROLL_VALUE, rangeModel.getValue());

		for (ILogListener listener : logListeners) {
			listener.log(entry);
		}
	}

	public void removeListener(ILogListener listener) {
		logListeners.remove(listener);
	}

	@Override
	public void setFocus() {
		// nothing to do

	}

	public void setInput(OutlineViewNode<Graph> inputNode) {
		// first clean up old data
		if (currInputNode != null) {
			for (Long id : backgroundColorsToClear) {
				currInputNode.getData().getGraphElement(id).setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, null);
			}
			backgroundColorsToClear.clear();
		}

		logInputChanged(currInputNode, inputNode);
		currInputNode = inputNode;

		if (composite != null) {
			composite.dispose();
		}
		composite = new Composite(parent, SWT.NONE);

		Graph graph = inputNode.getData();
		DefaultGraphicalGraphViewerAdvisor advisor = new DefaultGraphicalGraphViewerAdvisor(
				EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN), EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN), graph) {

			@Override
			public int getInitialFlyoutPaletteState() {
				return HIDE_PALETTE;
			}
		};

		GraphicalGraphViewerWithFlyoutPalette viewer = new GraphicalGraphViewerWithFlyoutPalette(composite, advisor);
		((CustomEditDomain) viewer.getViewer().getEditDomain()).setEditable(false);
		parent.layout(true, true);

		Viewport viewport = ((FigureCanvas) viewer.getViewer().getControl()).getViewport();
		viewport.getHorizontalRangeModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				logScroll(AbstractGraphCommand.HSCROLL, (RangeModel) event.getSource());
			}

		});

		viewport.getVerticalRangeModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				logScroll(AbstractGraphCommand.VSCROLL, (RangeModel) event.getSource());
			}

		});

		for (Map.Entry<Long, RGB> entry : inputNode.getBackgrounds().entrySet()) {
			graph.getGraphElement(entry.getKey()).setProperty(ModelerConstants.PROPERTY_BACKGROUND_COLOR, entry.getValue());
		}

		backgroundColorsToClear.addAll(inputNode.getBackgrounds().keySet());
	}

}
