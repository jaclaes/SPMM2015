/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm;

import org.cheetahplatform.modeler.decserflow.figure.ActivityFigure;
import org.eclipse.swt.graphics.RGB;

public class TDMConstants {
	public static final String PROPERTY_ACTIVITIES = "PROPERTY_ACTIVITIES";
	public static final String PROPERTY_LAYOUT = "PROPERTY_LAYOUT";
	public static final String PROPERTY_SELECTION = "PROPERTY_SELECTION";
	public static final String PROPERTY_MOVE_TO_TOP = "PROPERTY_MOVE_TO_TOP";
	public static final String PROPERTY_STATE = "PROPERTY_STATE";
	public static final String PROPERTY_START_WEEK = "PROPERTY_START_WEEK";
	public static final String PROPERTY_LENGTH = "PROPERTY_LENGTH";
	public static final String PROPERTY_CONSTRAINTS = "PROPERTY_CONSTRAINTS";
	public static final String PROPERTY_MESSAGE = "PROPERTY_MESSAGE";
	public static final String PROPERTY_BACKGROUND_COLOR = "PROPERTY_BACKGROUND_COLOR";
	public static final String PROPERTY_TOOLTIP = "PROPERTY_TOOLTIP";
	public static final String PROPERTY_TIMESLOT_SELECTION = "PROPERTY_TIMESLOT_SELECTION";
	public static final String PROPERTY_EXECUTION_ASSERTIONS = "PROPERTY_EXECUTION_ASSERTIONS";
	public static final String PROPERTY_TERMINATION_ASSERTIONS = "PROPERTY_TERMINATION_ASSERTIONS";

	public static final String KEY_SHOW_COMPLETED_ACTIVITIES = "SHOW_COMPLETED_ACTIVITIES";
	public static final String KEY_WEEKS_PER_ROW = "KEY_WEEKS_PER_ROW";
	public static final String KEY_WEEK_ROWS = "KEY_COLUMNS";

	public static final RGB COLOR_BACKGROUND = new RGB(227, 239, 255);
	public static final RGB COLOR_LINE = new RGB(101, 147, 207);
	public static final RGB COLOR_PLANNING_AREA_PRIMARY_LINE = new RGB(165, 191, 225);
	public static final RGB COLOR_PLANNING_AREA_SECONDARY_LINE = new RGB(213, 225, 241);
	public static final RGB COLOR_ACTIVITY_TOP = ActivityFigure.COLOR_ACTIVITY_TOP;
	public static final RGB COLOR_ACTIVITY_BOTTOM = ActivityFigure.COLOR_ACTIVITY_BOTTOM;
	public static final RGB COLOR_PAGE_UP_DOWN_LEFT = new RGB(241, 241, 241);
	public static final RGB COLOR_PAGE_UP_DOWN_RIGHT = new RGB(252, 252, 252);
	public static final RGB COLOR_PAGE_UP_DOWN_BORDER = new RGB(235, 235, 235);
	public static final RGB COLOR_THUMB_BORDER = new RGB(155, 155, 155);
	public static final RGB COLOR_THUMB_HORIZONTAL_OUTER = new RGB(151, 151, 151);
	public static final RGB COLOR_THUMB_HORIZONTAL_MIDDLE = new RGB(189, 189, 189);
	public static final RGB COLOR_THUMB_EDGE = new RGB(253, 253, 253);
	public static final RGB COLOR_THUMB_LEFT = new RGB(255, 255, 255);
	public static final RGB COLOR_THUMB_RIGHT = new RGB(212, 212, 212);
	public static final RGB COLOR_WEEKLY_HEADER_BORDER = new RGB(141, 174, 217);
	public static final RGB COLOR_WEEKLY_HEADER_BORDER_CURRENT_WEEK = new RGB(238, 147, 17);
	public static final RGB COLOR_WEEKLY_HEADER_TOP_CURRENT_WEEK = new RGB(247, 207, 113);
	public static final RGB COLOR_WEEKLY_HEADER_TOP = new RGB(228, 236, 246);
	public static final RGB COLOR_WEEKLY_HEADER_BOTTOM = new RGB(199, 215, 236);
	public static final RGB COLOR_WEEKLY_HEADER_BOTTOM_CURRENT_WEEK = new RGB(240, 159, 37);
	public static final RGB COLOR_WEEKLY_PLANNING_AREA_BORDER = new RGB(93, 140, 201);
	public static final RGB COLOR_MILESTONE_BACKGROUND = new RGB(249, 157, 57);
	public static final RGB COLOR_MILESTONE_BORDER = new RGB(252, 97, 4);
	public static final RGB COLOR_TEST_FAILED = new RGB(232, 109, 119);
	public static final RGB COLOR_PLANNING_AREA_SELECTON = new RGB(41, 76, 122);

	public static final String IMAGE_BUTTON_UP = "img/tdm/up.jpg";
	public static final String IMAGE_BUTTON_DOWN = "img/tdm/down.jpg";
	public static final String IMAGE_BUTTON_DOWN_PRESSED = "img/tdm/down_pressed.jpg";
	public static final String IMAGE_BUTTON_UP_PRESSED = "img/tdm/up_pressed.jpg";
	public static final String IMAGE_BUTTON_UP_HOVERED = "img/tdm/up_hovered.jpg";
	public static final String IMAGE_BUTTON_DOWN_HOVERED = "img/tdm/down_hovered.jpg";
	public static final String IMAGE_BUTTON_LEFT = "img/tdm/left.jpg";
	public static final String IMAGE_BUTTON_RIGHT = "img/tdm/right.jpg";
	public static final String IMAGE_BUTTON_RIGHT_PRESSED = "img/tdm/right_pressed.jpg";
	public static final String IMAGE_BUTTON_LEFT_PRESSED = "img/tdm/left_pressed.jpg";
	public static final String IMAGE_BUTTON_LEFT_HOVERED = "img/tdm/left_hovered.jpg";
	public static final String IMAGE_BUTTON_RIGHT_HOVERED = "img/tdm/right_hovered.jpg";

	public static final String REQUEST_ADD_ACTIVITY = "REQUEST_ADD_ACTIVITY";
	public static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
	public static final String KEY_DROP_LOCATION = "DROP_LOCATION";
	public static final String KEY_ACTIVITIES = "KEY_ACTIVITIES";
	public static final String KEY_EDIT_POLICY_HANDLDER = "KEY_EDIT_POLICY_HANDLDER";
	public static final String KEY_NEW_TIME_SLOT = "KEY_NEW_TIME_SLOT";

	public static final int LOCKING_INTERVAL = 15;
	public static final String RESPONSE_CONSTRAINT = "Response";

	public static final String LOG_EVENT_TYPE_MOVE_WARNING = "move_warning";
	public static final String LOG_EVENT_TYPE_CONSTRAINT_VIOLATION = "constraint_violation";
	public static final String LOG_EVENT_MILESTONE_MOVE = "milestone_move";
	public static final String LOG_EVENT_ACTIVITY_MOVE = "activity_move";

	public static final String ATTRIBUTE_ACTIVITY_NAME = "activitiy_name";
	public static final String ATTRIBUTE_CONSTRAINT = "constraint";
	public static final String ATTRIBUTE_MILESTONE_MOVE_DIRECTION = "milestone_move_direction";
	public static final String MILESTONE_MOVE_FORWARD = "forward";
	public static final String MILESTONE_MOVE_BACKWARD = "backward";
	public static final String ATTRIBUTE_ACTIVITY_MOVE_OFFSET_IN_WEEKS = "activity_move_offset_in_weeks";
	public static final String ATTRIBUTE_TDM_PROCESS_ID = "tdm_process_id";

}
