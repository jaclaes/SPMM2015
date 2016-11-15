package org.cheetahplatform.common;

import org.eclipse.swt.graphics.Color;

import com.swtdesigner.SWTResourceManager;

public class CommonConstants {
	public static final String ATTRIBUTE_TIMESTAMP = "timestamp";
	public static final String ATTRIBUTE_HOST = "host";
	public static final String ATTRIBUTE_GAME_TYPE = "type";
	public static final String ATTRIBUTE_PROCESS = "PROCESS";
	public static final String ATTRIBUTE_PROCESS_INSTANCE = "PROCESS_INSTANCE";
	public static final String ATTRIBUTE_DISPLAY_RESOLUTION_WIDTH = "display_resolution_width";
	public static final String ATTRIBUTE_DISPLAY_RESOLUTION_HEIGHT = "display_resolution_height";
	public static final String ATTRIBUTE_UNDO_EVENT = "undo_event";
	public static final String ATTRIBUTE_BEHAVIORAL_PROFILES_SIMILARITY_BASE = "BpSimBase";
	public static final String ATTRIBUTE_BEHAVORIAL_PROFILES_SIMILARITY_ALL = "BpSimAll";
	public static final String ATTRIBUTE_GRAPH_EDIT_DISTANCE = "ged";

	public static final String FILTER_PROCESS_INSTANCE_FROM = "filter_process_instance_from";
	public static final String FILTER_PROCESS_INSTANCE_TO = "filter_process_instance_to";
	public static final String FILTER_PROCESS_INSTANCE_ID = "filter_process_instance_id";
	public static final String FILTER_PROCESS_INSTANCE_HOST = "filter_process_instance_host";
	public static final String FILTER_PROCESS_INSTANCE_PROCESS = "filter_process_instance_process";

	public static final Color DIVIDER_COLOR = SWTResourceManager.getColor(180, 180, 180);
	public static final Color SELECTION_COLOR = SWTResourceManager.getColor(233, 233, 233);
	public static final Color BACKGROUND_COLOR = SWTResourceManager.getColor(255, 255, 255);

	public static final String PREFERENCE_DB_LOGGING_TYPE = "db_logging"; //$NON-NLS-1$
	public static final String PREFERENCE_XML_LOGGING_TYPE = "xml_logging"; //$NON-NLS-1$
	public static final String PREFERENCE_PASSWORD = "password"; //$NON-NLS-1$
	public static final String PREFERENCE_USER_DB_NAME = "userName"; //$NON-NLS-1$
	public static final String PREFERENCE_HOST = "dbURI"; //$NON-NLS-1$
	public static final String PREFERENCE_LOGGING_TYPE = "loggingType"; //$NON-NLS-1$
	public static final String PREFERENCE_PORT = "db_port"; //$NON-NLS-1$
	public static final String PREFERENCE_SCHEMA = "schema"; //$NON-NLS-1$
	public static final String PREFERENCE_USER = "user";

	public static final String KEY_MOUSE_LOCATION = "KEY_MOUSE_LOCATION";
	public static final String KEY_VIEWER = "KEY_VIEWER";
}
