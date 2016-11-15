package org.cheetahplatform.modeler.graph.model;

import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.generic.model.GenericModel;
import org.cheetahplatform.modeler.generic.model.IGenericModel;
import org.cheetahplatform.modeler.graph.editpart.EdgeLabelEditPart;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;

public class EdgeLabel extends GenericModel implements ILocated {
	private Point offset;

	public EdgeLabel(IGenericModel parent) {
		super(parent);

		this.offset = new Point();
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new EdgeLabelEditPart(this);
	}

	@Override
	public Point getLocation() {
		return offset;
	}

	public Point getOffset() {
		return offset;
	}

	public String getText() {
		String name = ((Edge) parent).getName();
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
			String add = " (" + ((Edge) parent).getId() + ")";
			if (name == null || name.equals("null")) {
				name = "" + ((Edge) parent).getId();
			} else if (!name.equals("" + ((Edge) parent).getId()) && !name.contains(add)) {
				name += add;
			}
		} else if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)) {
			name = "" + ((Edge) parent).getId();
		}
		return name;
	}

	@Override
	public void move(Point moveDelta) {
		offset.translate(moveDelta);

		firePropertyChanged(ModelerConstants.PROPERTY_LAYOUT);
	}

	public void resetOffset() {
		setOffset(new Point());
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Point offset) {
		this.offset = offset;

		firePropertyChanged(ModelerConstants.PROPERTY_LAYOUT);
	}

	public void setText(String text) {
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
			try {
				int index = text.lastIndexOf(" (");
				if (index == -1 || index > text.length() - 3) {
					((Edge) parent).setName(text);
				} else {
					((Edge) parent).setName(text.substring(0, index));
					((Edge) parent).setId(Long.parseLong(text.substring(index + 2, text.length() - 1)));
				}
			} catch (NumberFormatException nfe) {
				((Edge) parent).setName(text);
			}
		} else if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)) {
			try {
				((Edge) parent).setId(Long.parseLong(text));
			} catch (NumberFormatException nfe) {
				((Edge) parent).setName(text);
			}
		} else {
			((Edge) parent).setName(text);
		}
		if (text == null) {
			offset = new Point();
		}

		firePropertyChanged(ModelerConstants.PROPERTY_NAME);
	}

}
