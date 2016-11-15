package org.cheetahplatform.modeler.dialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class PpmNoteCategorySelectionDialog extends TitleAreaDialog {
	private class CategoryContentProvider extends ArrayContentProvider implements ITreeContentProvider {
		@Override
		public Object[] getChildren(Object parentElement) {
			IPpmNoteCategory category = (IPpmNoteCategory) parentElement;
			return category.getSubCategories().toArray();
		}

		@Override
		public Object[] getElements(Object inputElement) {
			IPpmNoteCategory category = (IPpmNoteCategory) inputElement;
			return category.getSubCategories().toArray();
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			IPpmNoteCategory category = (IPpmNoteCategory) element;
			return category.hasSubCategories();
		}

	}

	private class CategoryLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			IPpmNoteCategory category = (IPpmNoteCategory) element;
			return category.getName();
		}

	}

	private class CreateCategoryAction extends Action {

		public static final String ID = "org.cheetahplatform.action.createCategoryAction";

		public CreateCategoryAction() {
			setText("Create new Category");
			setId(ID);
		}

		@Override
		public void run() {
			IPpmNoteCategory parent = PpmNoteCategoryProvider.getInstance().getRootCategory();

			InputDialog dialog = new InputDialog(getShell(), "Category Name", "Please enter the category's name.", "",
					new NameInputValidator());

			if (dialog.open() == Window.OK) {
				String name = dialog.getValue();
				PpmNoteCategoryProvider.getInstance().addCategory(name, parent);
				categoryViewer.refresh();
			}
		}
	}

	private class CreateSubCategoryAction extends Action {
		public static final String ID = "org.cheetahplatform.action.createSubCategory";

		public CreateSubCategoryAction() {
			setText("Create Subcategory");
			setId(ID);
		}

		@Override
		public void run() {
			IPpmNoteCategory parent = getSelectedCategory();
			if (parent == null) {
				return;
			}

			InputDialog dialog = new InputDialog(getShell(), "Category Name", "Please enter the category's name.", "",
					new NameInputValidator());

			if (dialog.open() == Window.OK) {
				String name = dialog.getValue();
				PpmNoteCategoryProvider.getInstance().addCategory(name, parent);
				categoryViewer.refresh();
			}
		}
	}

	private final class NameInputValidator implements IInputValidator {
		@Override
		public String isValid(String newText) {
			if (newText.trim().isEmpty()) {
				return "Please enter a name.";
			}
			return null;
		}
	}

	private class RenameCategoryAction extends Action {
		public static final String ID = "org.cheetahplatform.action.RenameCategoryAction";

		public RenameCategoryAction() {
			setText("Rename");
			setId(ID);
		}

		@Override
		public void run() {
			IPpmNoteCategory selectedCategory = getSelectedCategory();
			if (selectedCategory == null) {
				return;
			}

			InputDialog dialog = new InputDialog(getShell(), "Category Name", "Please enter the category's name.",
					selectedCategory.getName(), new NameInputValidator());

			if (dialog.open() == Window.OK) {
				String name = dialog.getValue();
				selectedCategory.setName(name);
				PpmNoteCategoryProvider.getInstance().updateCategory(selectedCategory);
				categoryViewer.refresh();
			}
		}
	}

	private IPpmNoteCategory category;
	private TreeViewer categoryViewer;

	public PpmNoteCategorySelectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		setTitle("Select Category");
		setMessage("Please select a category below.");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		categoryViewer = new TreeViewer(composite, SWT.FULL_SELECTION);
		Tree tree = categoryViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		categoryViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TreeColumn column = new TreeColumn(categoryViewer.getTree(), SWT.NONE);
		column.setText("Category");
		column.setWidth(400);

		categoryViewer.setContentProvider(new CategoryContentProvider());
		categoryViewer.setLabelProvider(new CategoryLabelProvider());
		categoryViewer.setInput(PpmNoteCategoryProvider.getInstance().getRootCategory());
		categoryViewer.expandAll();

		MenuManager menuManager = new MenuManager();
		menuManager.add(new CreateCategoryAction());
		menuManager.add(new CreateSubCategoryAction());
		menuManager.add(new RenameCategoryAction());
		categoryViewer.getTree().setMenu(menuManager.createContextMenu(categoryViewer.getTree()));

		categoryViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});

		return container;
	}

	public IPpmNoteCategory getCategory() {
		return category;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 700);
	}

	public IPpmNoteCategory getSelectedCategory() {
		IStructuredSelection selection = (IStructuredSelection) categoryViewer.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(getShell(), "Select a category", "Please select a category.");
			return null;
		}

		return (IPpmNoteCategory) selection.getFirstElement();
	}

	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) categoryViewer.getSelection();
		if (!selection.isEmpty()) {
			category = (IPpmNoteCategory) selection.getFirstElement();
		}

		super.okPressed();
	}
}
