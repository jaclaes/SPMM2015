package org.cheetahplatform.testarossa.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cheetahplatform.core.common.NamedComparator;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.testarossa.TestaRossaModel;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class FilterRoleAction extends ProcessInstanceSpecificAction {
	public static final String ID = "org.cheetahplatform.testarossa.action.FilterRoleAction";

	public FilterRoleAction() {
		super(ID, "img/role.png", "img/role_disabled.png", AS_DROP_DOWN_MENU);
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return new IMenuCreator() {
			private Menu menu;
			private List<Image> images = new ArrayList<Image>();

			private Image createImage(Role role) {
				Image image = new Image(Display.getDefault(), 16, 16);
				GC gc = new GC(image);
				gc.setBackground(SWTResourceManager.getColor(role.getColor()));
				gc.fillRectangle(0, 0, 16, 16);
				gc.dispose();

				images.add(image);
				return image;
			}

			public void dispose() {
				if (menu != null) {
					menu.dispose();
				}

				for (Image image : images) {
					image.dispose();
				}
			}

			public Menu getMenu(Control parent) {
				if (menu != null) {
					menu.dispose();
				}

				menu = new Menu(parent);
				Workspace workspace = TestaRossaModel.getInstance().getCurrentWorkspace();
				List<Role> roles = workspace.getRoles();
				Collections.sort(roles, new NamedComparator());

				for (Role role : roles) {
					final Role finalRole = role;
					MenuItem item = new MenuItem(menu, SWT.CHECK);
					item.setText(role.getName());
					item.setSelection(workspace.isShowRole(role));
					item.setImage(createImage(role));
					item.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							toggleRoleVisibility(finalRole);
						}

					});
				}

				return menu;
			}

			public Menu getMenu(Menu parent) {
				return null; // ignore
			}

			private void toggleRoleVisibility(Role role) {
				Workspace workspace = TestaRossaModel.getInstance().getCurrentWorkspace();
				boolean showRole = workspace.isShowRole(role);
				workspace.setShowRole(role, !showRole);
			}
		};
	}
}
