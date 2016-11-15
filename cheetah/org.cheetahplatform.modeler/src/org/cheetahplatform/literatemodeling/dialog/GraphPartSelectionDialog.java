package org.cheetahplatform.literatemodeling.dialog;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.generic.editpart.IGenericEditPart;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.editpart.GraphEditPart;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         21.04.2010
 */
public class GraphPartSelectionDialog extends TitleAreaDialog {

	private static final Color BACKRGOUND_COLOR = SWTResourceManager.getColor(255, 255, 255);
	private final Graph graph;
	private final List<Class<? extends IGraphElementDescriptor>> allowedTypes;
	private final List<GraphElement> selectedElements;
	private GraphicalGraphViewerWithFlyoutPalette viewer;
	private ISelectionChangedListener selectionChangedListener;

	public GraphPartSelectionDialog(Shell parentShell, Graph graph, List<Class<? extends IGraphElementDescriptor>> allowedTypes) {
		super(parentShell);
		this.graph = graph;
		this.allowedTypes = allowedTypes;
		selectedElements = new ArrayList<GraphElement>();
	}

	@Override
	public boolean close() {
		boolean result = super.close();
		viewer.getViewer().removeSelectionChangedListener(selectionChangedListener);
		return result;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Composite buttonBar = (Composite) super.createButtonBar(parent);
		buttonBar.setBackgroundMode(SWT.INHERIT_FORCE);
		buttonBar.setBackground(BACKRGOUND_COLOR);
		return buttonBar;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(BACKRGOUND_COLOR);
		Composite container = (Composite) super.createDialogArea(parent);
		container.setBackground(BACKRGOUND_COLOR);
		setTitle("Select a sequence flow.");
		setMessage("Please select a sequence flow in the graph below.");
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		GraphPartSelectionViewerAdvisor advisor = new GraphPartSelectionViewerAdvisor(graph);
		viewer = new GraphicalGraphViewerWithFlyoutPalette(container, advisor);
		GraphEditDomain editDomain = (GraphEditDomain) viewer.getViewer().getEditDomain();
		editDomain.setEditable(false);

		selectionChangedListener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged(event.getSelection());
			}
		};

		viewer.getViewer().addSelectionChangedListener(selectionChangedListener);

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	/**
	 * Returns the selectedElements.
	 * 
	 * @return the selectedElements
	 */
	public List<GraphElement> getSelectedElements() {
		return selectedElements;
	}

	protected void handleSelectionChanged(ISelection selection) {
		if (selection.isEmpty()) {
			setError("Please select an element form the graph below.");
			return;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		if (structuredSelection.getFirstElement() instanceof GraphEditPart) {
			setError("Please select an element form the graph below.");
			return;
		}

		for (Object object : structuredSelection.toList()) {
			if (!isAllowedDescriptorType((IGenericEditPart) object)) {
				setError("At least one selected element does not match the required type");
				return;
			}
		}

		setError(null);
		for (Object object : structuredSelection.toList()) {
			IGenericEditPart editPart = (IGenericEditPart) object;
			selectedElements.add((GraphElement) editPart.getModel());
		}
	}

	/**
	 * Checks if the selected element is of the expected type.
	 * 
	 * @param editPart
	 *            the {@link IGenericEditPart} to check
	 * @return <code>true</code> if the {@link IGenericEditPart} is of the correct type, <code>false</code> otherwise
	 * 
	 */
	private boolean isAllowedDescriptorType(IGenericEditPart editPart) {
		if (allowedTypes == null) {
			return true;
		}

		GraphElement model = (GraphElement) editPart.getModel();
		IGraphElementDescriptor descriptor = model.getDescriptor();
		boolean found = false;
		for (Class<? extends IGraphElementDescriptor> descriptorClass : allowedTypes) {
			if (descriptorClass.isInstance(descriptor)) {
				found = true;
				break;
			}
		}
		return found;
	}

	protected void setError(String message) {
		setErrorMessage(message);
		if (message == null) {
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		} else {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}
}
