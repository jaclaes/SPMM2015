package at.component.tabfolder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import at.component.IComponent;
import at.component.util.ImagePathPool;
import at.component.util.ImageProvider;

public class TabFolderUi {

	private Composite parent;
	private CTabFolder tabFolder;
	private ToolItem connectItem;
	private ToolItem renameItem;

	public TabFolderUi(Composite parent) {
		this.parent = parent;
		createUi();
	}

	public void addNewComponentUI(IComponent component) {
		Composite control = new Composite(tabFolder, SWT.NONE);
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		control.setLayout(new GridLayout());

		component.addUI(control);

		control.layout(true, true);

		CTabItem tabItem = new CTabItem(tabFolder, SWT.CLOSE);
		tabItem.setText(component.getNameWithId());
		tabItem.setControl(control);
		tabItem.setData(component);

		tabFolder.setSelection(tabItem);
		renameItem.setEnabled(true);
		connectItem.setEnabled(true);
	}

	private void createUi() {
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		parent.setLayout(new GridLayout());
		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		tabFolder = new CTabFolder(parent, SWT.TOP);
		tabFolder.setBorderVisible(true);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		final ToolBar toolBar = new ToolBar(tabFolder, SWT.FLAT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		toolBar.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		renameItem = new ToolItem(toolBar, SWT.PUSH);
		renameItem.setImage(ImageProvider.getImage(ImagePathPool.RENAME_COMPONENT));
		renameItem.setToolTipText("Rename Component");
		renameItem.setEnabled(tabFolder.getSelection() != null);

		connectItem = new ToolItem(toolBar, SWT.PUSH);
		connectItem.setImage(ImageProvider.getImage(ImagePathPool.CONNECT_COMPONENT));
		connectItem.setToolTipText("Connect Component");
		connectItem.setEnabled(tabFolder.getSelection() != null);

		tabFolder.setTabHeight(22);
		tabFolder.setTopRight(toolBar);
	}

	public Shell getShell() {
		return parent.getShell();
	}

	public CTabFolder getTabFolder() {
		return tabFolder;
	}

	public ToolItem getRenameItem() {
		return renameItem;
	}

	public ToolItem getConnectItem() {
		return connectItem;
	}
}
