package org.cheetahplatform.experiment.editor.views;


import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.experiment.editor.model.ExperimentGraph;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ValidationView extends ViewPart implements IPropertyChangeListener{

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.cheetahplatform.experiment.editor.views.ValidationView";

	private TableViewer viewer;
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			ValidationViewModel vvm = (ValidationViewModel) parent;
			List<Message> msgs = new ArrayList<Message>();
			
			for (ExperimentGraph graph : vvm.getModels()){
				msgs.addAll(vvm.getMessages(graph));
			}
			
			return msgs.toArray();
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			Message msg = (Message) obj;
			
			switch (index){
			case 0:
				return getText(msg.getModelName());
			case 1:
				return getText(msg.getMessage());
			}
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			Message msg = (Message) obj;
			if (index == 0){
				switch (msg.getType()){
				case ERROR:
					return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
				case WARNING:
					return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
				case INFO:
					return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
				}
			}
			return null;
		}
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);				
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		//Create Colums
		createTableViewerColumn("Konfiguration", 100);
		createTableViewerColumn("Beschreibung", 300);	
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(ValidationViewModel.getInstance());
		
		ValidationViewModel.getInstance().addListener(this);
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.refresh();
	}	
}