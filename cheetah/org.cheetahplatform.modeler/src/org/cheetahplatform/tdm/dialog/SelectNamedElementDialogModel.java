package org.cheetahplatform.tdm.dialog;

import java.util.List;

import org.cheetahplatform.common.INamed;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class SelectNamedElementDialogModel {

	private static class SelectActivityDialogLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			INamed activity = (INamed) element;
			return activity.getName();
		}

	}

	private final List<INamed> elements;
	private INamed selection;

	public SelectNamedElementDialogModel(List<INamed> elements) {
		this.elements = elements;
	}

	public IBaseLabelProvider createLabelProvider() {
		return new SelectActivityDialogLabelProvider();
	}

	public Object getElements() {
		return elements;
	}

	/**
	 * @return the selection
	 */
	public INamed getSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            the selection to set
	 */
	public void setSelection(INamed selection) {
		this.selection = selection;
	}

}
