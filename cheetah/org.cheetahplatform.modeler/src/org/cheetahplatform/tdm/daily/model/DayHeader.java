package org.cheetahplatform.tdm.daily.model;

import org.cheetahplatform.tdm.GenericTDMModel;
import org.cheetahplatform.tdm.daily.editpart.DayHeaderEditPart;
import org.eclipse.gef.EditPart;

public class DayHeader extends GenericTDMModel {

	public DayHeader(Day parent) {
		super(parent);
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new DayHeaderEditPart(this);
	}

}
