package org.cheetahplatform.experiment.editor.prop;

import org.cheetahplatform.experiment.editor.Activator;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class CheckBoxPropertyDescriptor extends PropertyDescriptor {

	public CheckBoxPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return new CheckboxCellEditor(parent);
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return new CheckboxLabelProvider();
	}

	public static class CheckboxCellEditor extends CellEditor {
		private boolean value = false;
		private static final int defaultStyle = SWT.NONE;

		public CheckboxCellEditor() {
			setStyle(defaultStyle);
		}
		
		public CheckboxCellEditor(Composite parent) {
			this(parent, defaultStyle);
		}

		public CheckboxCellEditor(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		protected Control createControl(final Composite parent) {
			Canvas canvas = new Canvas(parent, SWT.NO_BACKGROUND); // transparent
			canvas.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					toggle();
				}
			});
			return canvas;
		}

		@Override
		public void activate() {
			toggle();
		}

		protected void toggle() {
			value = !value;
			markDirty();
			fireApplyEditorValue();
		}


		@Override
		protected Object doGetValue() {
			return value ? Boolean.TRUE : Boolean.FALSE;
		}

		@Override
		protected void doSetFocus() {
			// nothing to do
		}

		@Override
		protected void doSetValue(Object val) {
			this.value = Boolean.TRUE.equals(val);
		}
	}

	public static class CheckboxLabelProvider extends BaseLabelProvider
			implements ILabelProvider {
		public static final Image TRUE_IMAGE = Activator.getImageDescriptor(
				"/icons/checked.png").createImage();
		public static final Image FALSE_IMAGE = Activator.getImageDescriptor(
				"/icons/unchecked.png").createImage();

		public Image getImage(Object element) {
			return Boolean.TRUE.equals(element) ? TRUE_IMAGE : FALSE_IMAGE;
		}

		public String getText(Object element) {
			return null;
		}
	}
}
