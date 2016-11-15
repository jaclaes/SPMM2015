/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.alaskasimulator.test.fitnesse.core;

import java.util.ArrayList;
import java.util.List;

import org.alaskasimulator.core.buildtime.ActionConfig;
import org.alaskasimulator.core.buildtime.LateBindingBoxConfig;
import org.alaskasimulator.core.buildtime.PlanItemConfig;
import org.alaskasimulator.core.runtime.PlanItem;
import org.alaskasimulator.core.runtime.action.Action;
import org.alaskasimulator.core.runtime.box.Box;
import org.alaskasimulator.core.runtime.box.LateBindingBox;
import org.alaskasimulator.core.runtime.box.LateModelingBox;

import fit.ActionFixture;

public class GameActionFixture extends ActionFixture {
	private String actionID;
	private String actionConfigID;
	private String boxID;
	private int sequenceIndex;

	public void createAction() {
		ActionConfig actionConfig = (ActionConfig) AlaskaFitnesseTestHelper.getObject(actionConfigID);
		Action action = AlaskaFitnesseTestHelper.GAME.getConfig().findActionConfigProxy(actionConfig).createAction();
		AlaskaFitnesseTestHelper.setObject(actionID, action);
		AlaskaFitnesseTestHelper.GAME.getPlan().insertPlanItem(action);
	}

	public int day() {
		return AlaskaFitnesseTestHelper.GAME.getCurrentTime().getDay();
	}

	public void executeAction() {
		Action action = (Action) AlaskaFitnesseTestHelper.getObject(actionID);
		action.execute();
	}

	public void executeBox() {
		Box box = (Box) AlaskaFitnesseTestHelper.getObject(boxID);
		for (PlanItem planItem : box.getSequence().getPlanItems())
			planItem.toAction().execute();

	}

	public int minutesOfDay() {
		return AlaskaFitnesseTestHelper.GAME.getCurrentTime().getMinute();
	}

	public void modelPlanItemSequence(List<PlanItemConfig> modelingList) {
		LateModelingBox box = (LateModelingBox) AlaskaFitnesseTestHelper.getObject(boxID);
		box.select(modelingList);
	}

	public int numOfPlannedActions() {
		return AlaskaFitnesseTestHelper.GAME.getPlan().getActionsInPlan().size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object parse(String string, Class type) throws Exception {
		if (type.equals(List.class)) {
			List<PlanItemConfig> list = new ArrayList<PlanItemConfig>();

			for (String s : string.split(",")) {
				PlanItemConfig boxConfigContent = (PlanItemConfig) AlaskaFitnesseTestHelper.getObject(s);
				list.add(boxConfigContent);
			}

			return list;
		}

		return super.parse(string, type);
	}

	public boolean selectSequence() {
		try {
			LateBindingBox box = (LateBindingBox) AlaskaFitnesseTestHelper.getObject(boxID);
			LateBindingBoxConfig boxConfig = (LateBindingBoxConfig) box.getBoxConfig();

			box.select(boxConfig.getSequences().get(sequenceIndex));
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public void setActionConfigID(String actionConfigID) {
		this.actionConfigID = actionConfigID;
	}

	public void setActionID(String actionID) {
		this.actionID = actionID;
	}

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	public void setSequenceIndex(int sequenceIndex) {
		this.sequenceIndex = sequenceIndex;
	}
}
