package org.cheetahplatform.modeler.hierarchical;

import java.util.Iterator;

import org.cheetahplatform.common.ui.gef.CustomEditDomain;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.part.ViewPart;

public class HierarchicalComboView extends ViewPart {

	public static final String ID = "org.cheetahplatform.modeler.hierarchical.HierarchicalComboView";
	private Composite graphComposite = null;
	private HierarchicalOutlineComposite hierarchicalOutline;
	private Composite container;
	private Sash sash;

	@Override
	public void createPartControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		sash = new Sash(container, SWT.VERTICAL);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(20, 0);
		sash.setLayoutData(data);

		sash.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				sash.setBounds(e.x, e.y, e.width, e.height);

				FormData formData = new FormData();
				formData.top = new FormAttachment(0, 0);
				formData.left = new FormAttachment(0, e.x);
				formData.bottom = new FormAttachment(100, 0);
				sash.setLayoutData(formData);
				container.layout();
			}
		});

		hierarchicalOutline = new HierarchicalOutlineComposite(container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sash, 0);
		hierarchicalOutline.setLayoutData(data);

		graphComposite = new Composite(container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(sash, 0);
		data.right = new FormAttachment(100, 0);
		graphComposite.setLayoutData(data);

		hierarchicalOutline.addSelectionChangeListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Iterator it = selection.iterator();
					if (it.hasNext()) {
						@SuppressWarnings("unchecked")
						OutlineViewNode<Graph> node = (OutlineViewNode<Graph>) it.next();
						setGraphInput(node);
					}
				}
			}
		});

	}

	public OutlineViewNode<Graph> getSelection() {
		return hierarchicalOutline.getSelection();
	}

	@Override
	public void setFocus() {
		// nothing to do
	}

	protected void setGraphInput(OutlineViewNode<Graph> mainNode) {
		if (graphComposite != null) {
			graphComposite.dispose();
		}
		graphComposite = new Composite(container, SWT.NONE);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		data.left = new FormAttachment(sash, 0);
		data.right = new FormAttachment(100, 0);
		graphComposite.setLayoutData(data);

		Graph graph = mainNode.getData();
		DefaultGraphicalGraphViewerAdvisor advisor = new DefaultGraphicalGraphViewerAdvisor(
				EditorRegistry.getNodeDescriptors(EditorRegistry.BPMN), EditorRegistry.getEdgeDescriptors(EditorRegistry.BPMN), graph) {

			@Override
			public int getInitialFlyoutPaletteState() {
				return HIDE_PALETTE;
			}
		};

		GraphicalGraphViewerWithFlyoutPalette viewer = new GraphicalGraphViewerWithFlyoutPalette(graphComposite, advisor);
		((CustomEditDomain) viewer.getViewer().getEditDomain()).setEditable(false);
		container.layout(true, true);

	}

	public void setInput(OutlineViewNode<Graph> mainNode) {
		hierarchicalOutline.setInput(mainNode);
		setGraphInput(mainNode);
	}

	public void setSelection(OutlineViewNode<Graph> node) {
		hierarchicalOutline.setSelection(node);

	}

}
