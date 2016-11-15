package org.cheetahplatform.testarossa.dialog;

import java.util.Comparator;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.core.common.NamedComparator;
import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class SelectRoleDialogModel {

	private static class RoleLabelProvider extends LabelProvider implements ITableColorProvider, ITableLabelProvider {

		public Color getBackground(Object element, int columnIndex) {
			Role role = (Role) element;
			return SWTResourceManager.getColor(role.getColor());
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			Role role = (Role) element;
			return role.getName();
		}

		public Color getForeground(Object element, int columnIndex) {
			return null;
		}

	}

	private static class RoleSorter extends ViewerSorter {
		private static final Comparator<INamed> COMPARATOR = new NamedComparator();

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Role role1 = (Role) e1;
			Role role2 = (Role) e2;

			return COMPARATOR.compare(role1, role2);
		}
	}

	private Role selection;

	public SelectRoleDialogModel(Role selection) {
		this.selection = selection;
	}

	public IBaseLabelProvider createLabelProvider() {
		return new RoleLabelProvider();
	}

	public ViewerSorter createSorter() {
		return new RoleSorter();
	}

	public Object getInput() {
		return RoleLookup.getInstance().getAllRoles();
	}

	/**
	 * @return the selection
	 */
	public Role getSelection() {
		return selection;
	}

	public ISelection getSelectionAsViewerSelection() {
		if (selection == null) {
			return new StructuredSelection();
		}

		return new StructuredSelection(selection);
	}

	public void setSelection(Role selection) {
		this.selection = selection;
	}

}
