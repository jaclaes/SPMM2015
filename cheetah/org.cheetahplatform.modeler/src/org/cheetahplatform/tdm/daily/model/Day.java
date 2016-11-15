/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.daily.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.tdm.GenericTDMModel;
import org.cheetahplatform.tdm.TDMConstants;
import org.cheetahplatform.tdm.daily.editpart.DayEditPart;
import org.eclipse.gef.EditPart;

public class Day extends GenericTDMModel {
	private final Date date;

	private final DayTimeLine timeline;

	private final PlanningArea planningArea;
	private final ExecutionAssertionArea executionAssertionArea;
	private final TerminationAssertionArea terminationAssertionArea;
	private final DayHeader header;

	public Day(Workspace workspace, Date date) {
		super(workspace);

		this.date = date;
		this.header = new DayHeader(this);
		this.timeline = new DayTimeLine(this);
		this.planningArea = new PlanningArea(this);
		this.executionAssertionArea = new ExecutionAssertionArea(this);
		this.terminationAssertionArea = new TerminationAssertionArea(this);
	}

	public Activity addActivityInstance(DeclarativeActivity activity, DateTime start) {
		Activity instance = getWorkspace().addActivityInstance(activity, start);
		firePropertyChanged(Workspace.class, TDMConstants.PROPERTY_ACTIVITIES);

		return instance;
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return new DayEditPart(this);
	}

	public List<Activity> getActivities() {
		return planningArea.getActivities();
	}

	@Override
	public List<? extends Object> getChildren() {
		List<Object> children = new ArrayList<Object>();
		children.add(header);
		children.add(timeline);
		children.add(planningArea);
		boolean showExecutionAssertions = CheetahPlatformConfigurator.getBoolean(IConfiguration.TDM_SHOW_EXECUTION_ASSERTION_AREA);
		if (showExecutionAssertions) {
			children.add(executionAssertionArea);
		}

		boolean showTerminationAssertions = CheetahPlatformConfigurator.getBoolean(IConfiguration.TDM_SHOW_TERMINATION_ASSERTION_AREA);
		if (showTerminationAssertions) {
			children.add(terminationAssertionArea);
		}

		return children;
	}

	/**
	 * Return the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the executionAssertionArea
	 */
	public ExecutionAssertionArea getExecutionAssertionArea() {
		return executionAssertionArea;
	}

	/**
	 * Return the planningArea.
	 * 
	 * @return the planningArea
	 */
	public PlanningArea getPlanningArea() {
		return planningArea;
	}

	/**
	 * @return the terminationAssertionArea
	 */
	public TerminationAssertionArea getTerminationAssertionArea() {
		return terminationAssertionArea;
	}

	public int getWidth() {
		return planningArea.getWidth();
	}

}
