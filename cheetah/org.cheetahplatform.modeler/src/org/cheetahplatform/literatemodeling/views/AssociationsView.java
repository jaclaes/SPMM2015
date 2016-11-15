package org.cheetahplatform.literatemodeling.views;

import java.net.URL;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.literatemodeling.LiterateModelingEditor;
import org.cheetahplatform.literatemodeling.model.IAssociationChangedListener;
import org.cheetahplatform.literatemodeling.model.ILiterateModelingAssociation;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.literatemodeling.model.NodeLiterateModelingAssociation;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class AssociationsView extends ViewPart implements IAssociationChangedListener {

	class ViewContentProvider implements IStructuredContentProvider {
		@Override
		public void dispose() {
			// nothing to do
		}

		@Override
		public Object[] getElements(Object parent) {
			List<ILiterateModelingAssociation> assocs = ((LiterateModel) parent).getAssociations();
			return assocs.toArray();
		}

		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// nothing to do
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object obj, int index) {
			if (index == 0) {
				ILiterateModelingAssociation assoc = (ILiterateModelingAssociation) obj;
				if (!assoc.isSingleLiterateModelingAssociation()) {
					return comment_icon;
				} else if (assoc instanceof NodeLiterateModelingAssociation) {
					return activity_icon;
				} else {
					return sequence_icon;
				}
			}
			return null;
		}

		@Override
		public String getColumnText(Object obj, int index) {
			ILiterateModelingAssociation assoc = (ILiterateModelingAssociation) obj;

			switch (index) {
			case 0:
				return "";
			case 1:
				return getModelNames(assoc.getGraphElements());
			case 2:
				try {
					String text = literateModel.getDocument().get(assoc.getOffset(), assoc.getLength());
					text = text.replaceAll("(\r\n|\n)", " ");
					return text;
				} catch (BadLocationException e) {
					Activator.getDefault().getLog()
							.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error accessing association text", e));
				}
			}

			return getText(obj);
		}
	}

	private LiterateModel literateModel;
	private static Image activity_icon;
	private static Image sequence_icon;
	private static Image comment_icon;
	private static ImageDescriptor delete_icon;

	static {
		activity_icon = loadImage("img/bpmn/activity.png").createImage();
		sequence_icon = loadImage("img/edit_condition.gif").createImage();
		comment_icon = loadImage("img/add_comment_16.png").createImage();
		delete_icon = loadImage("img/literatemodeling/delete_icon.gif");
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.cheetahplatform.literatemodeling.views.AssociationsView";

	private static String getModelNames(List<GraphElement> elements) {
		StringBuffer sb = new StringBuffer();
		Iterator<GraphElement> it = elements.iterator();
		while (it.hasNext()) {
			GraphElement elem = it.next();
			if (elem.getName() == null || elem.getName().equals("")) {
				sb.append(elem.getDescriptor().getName());
			} else {
				sb.append(elem.getName());
			}

			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	private static ImageDescriptor loadImage(String path) {
		URL url = Activator.getDefault().getBundle().getEntry(path);
		return ImageDescriptor.createFromURL(url);
	}

	private TableViewer viewer;

	private Action deleteAction;

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public void associationChanged(ILiterateModelingAssociation association) {
		viewer.refresh();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		// Create Colums
		TableViewerColumn typeCol = createTableViewerColumn("Type", 80);
		TableViewerColumn modelCol = createTableViewerColumn("Model", 250);
		TableViewerColumn textCol = createTableViewerColumn("Text", 300);

		ColumnViewerSorter typeSorter = new ColumnViewerSorter(viewer, typeCol) {
			@Override
			protected int doCompare(Viewer viewer, Object obj1, Object obj2) {
				ILiterateModelingAssociation a1 = (ILiterateModelingAssociation) obj1;
				ILiterateModelingAssociation a2 = (ILiterateModelingAssociation) obj2;
				return getVal(a1).compareTo(getVal(a2));
			}

			protected Integer getVal(ILiterateModelingAssociation assoc) {
				if (!assoc.isSingleLiterateModelingAssociation()) {
					return 3;
				} else if (assoc instanceof NodeLiterateModelingAssociation) {
					return 1;
				} else {
					return 0;
				}
			}

		};
		typeSorter.setSorter(typeSorter, ColumnViewerSorter.ASC);

		ColumnViewerSorter modelSorter = new ColumnViewerSorter(viewer, modelCol) {
			@Override
			protected int doCompare(Viewer viewer, Object obj1, Object obj2) {
				ILiterateModelingAssociation a1 = (ILiterateModelingAssociation) obj1;
				ILiterateModelingAssociation a2 = (ILiterateModelingAssociation) obj2;
				return getModelNames(a1.getGraphElements()).compareTo(getModelNames(a2.getGraphElements()));
			}

		};
		modelSorter.setSorter(modelSorter, ColumnViewerSorter.ASC);

		ColumnViewerSorter textSorter = new ColumnViewerSorter(viewer, textCol) {
			@Override
			protected int doCompare(Viewer viewer, Object obj1, Object obj2) {
				ILiterateModelingAssociation a1 = (ILiterateModelingAssociation) obj1;
				ILiterateModelingAssociation a2 = (ILiterateModelingAssociation) obj2;

				String text1 = "";
				String text2 = "";
				try {
					text1 = literateModel.getDocument().get(a1.getOffset(), a1.getLength());
					text2 = literateModel.getDocument().get(a2.getOffset(), a2.getLength());
				} catch (BadLocationException e) {
					Activator.getDefault().getLog()
							.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error accessing association text", e));
				}

				return text1.compareTo(text2);
			}

		};
		textSorter.setSorter(textSorter, ColumnViewerSorter.ASC);

		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		LiterateModelingEditor editor = LiterateModelingEditor.getActiveEditor();
		if (editor != null) {
			editor.addAssociationViewListener(this);

		}

		makeActions();
		hookContextMenu();
		contributeToActionBars();

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(deleteAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(deleteAction);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				AssociationsView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void makeActions() {
		deleteAction = new Action() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				literateModel.removeAllAssociations(selection.toList());
				viewer.refresh();
			}
		};
		deleteAction.setText("Delete");
		deleteAction.setToolTipText("Delete the selected associations.");
		deleteAction.setImageDescriptor(delete_icon);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.removeSelectionChangedListener(listener);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void setInput(LiterateModel literateModel) {
		this.literateModel = literateModel;
		viewer.setInput(literateModel);
		literateModel.addAssociationChangedListener(this);

		LiterateModelingEditor liMoEditor = (LiterateModelingEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

		if (liMoEditor != null) {
			liMoEditor.getCommandStack().addCommandStackListener(new CommandStackListener() {
				@Override
				public void commandStackChanged(EventObject event) {
					viewer.refresh();
				}
			});
		}
	}
}