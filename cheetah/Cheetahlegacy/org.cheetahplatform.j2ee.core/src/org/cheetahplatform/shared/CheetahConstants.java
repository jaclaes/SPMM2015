package org.cheetahplatform.shared;

public class CheetahConstants {
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_PASSWORD_HASH = "KEY_PASSWORD";
	public static final String KEY_SERVICE_VERSION = "KEY_SERVICE_VERSION";
	public static final String KEY_SERVICE_NAME = "KEY_SERVICE_NAME";
	public static final String KEY_MESSAGE_TYPE = "KEY_MESSAGE_TYPE";
	public static final String KEY_SESSION_ID = "SESSION_ID";
	public static final String KEY_MESSAGE_ID = "MESSAGE_ID";
	public static final String KEY_STATUS = "STATUS";
	public static final String KEY_ACTIVE_ACTIVITIES = "ACTIVE_ACTIVITIES";
	public static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
	public static final String KEY_PROCESS_INSTANCE_ID = "KEY_PROCESS_INSTANCE_ID";
	public static final String KEY_PROCESS_SCHEMA_ID = "KEY_PROCESS_SCHEMA";
	public static final String KEY_EXCEPTION = "KEY_EXCEPTION";
	public static final String KEY_LATE_BINDING_SEQUENCES = "KEY_LATE_BINDING_SEQUENCES";
	public static final String KEY_LATE_BINDING_SEQUENCE = "KEY_LATE_BINDING_SEQUENCE";
	public static final String KEY_LATE_BINDING_BOX_ID = "KEY_LATE_BINDING_BOX_ID";
	public static final String KEY_LATE_MODELING_ACTIVITIES = "KEY_LATE_MODELING_ACTIVITIES";
	public static final String KEY_LATE_MODELING_BOX_CONTENT = "KEY_LATE_MODELING_BOX_CONTENT";
	public static final String KEY_RECOMMENDATIONS = "KEY_RECOMMENDATIONS";

	public static final String STATUS_OK = "OK";
	public static final String ERROR_LOGIN_INCORRECT = "Login Incorrect";
	public static final String ERROR_PROTOCOL_VERSION_MISSING = "No Message Protocol Version";
	public static final String ERROR_SERVICE_MISSING = "No Service Specified";
	public static final String ERROR_MESSAGE_ID_MISSING = "No Message Id";
	public static final String ERROR_INVALID_SESSION_ID = "Invalid Session Id";
	public static final String ERROR_INVALID_ACTIVITY_ID = "Invalid id for Activity";
	public static final String ERROR_UNKNOWN_PROCESS_INSTANCE_ID = "Unknown Process Instance";
	public static final String ERROR_UNKNOWN_PROCESS_SCHEMA_ID = "Unknown Process Schema";
	public static final String ERROR_UNKNOWN_SERVICE_REQUESTED = "Unknown Service Requested";
	public static final String ERROR_ILLEGAL_ACTIVITY_STATE = "Illegal State of Activity";
	public static final String ERROR_NO_BOX_FOUND = "Invalid id for Activity";
	public static final String ERROR_UNKNOWN_LATE_BINDING_SEQUENCE = "Invalid id for Late Binding Box Sequence";
	public static final String ERROR_SUBPROCESS_ALREADY_SELECTED = "The subprocess has already been selected";
	public static final String ERROR_UNKNOWN_LATE_MODELING_BOX = "Invalid id for Late Modeling Box";
	public static final String ERROR_UNKNOWN = "An unknown error occurred";

	public static final String SERVICE_LOGIN = "login";
	public static final String SERVICE_SESSION_VERIFIER = "sessionVerifier";
	public static final String SERVICE_ACTIVE_TASK_RETRIEVAL = "getActiveTasks";
	public static final String SERVICE_GET_RECOMMENDATION = "getRecommendation";
	public static final String SERVICE_LAUNCH_ACTIVITY = "launchActivity";
	public static final String SERVICE_CANCEL_ACTIVITY = "cancelActivity";
	public static final String SERVICE_COMPLETE_ACTIVITY = "completeActivity";
	public static final String SERVICE_DISPATCHER = "dispatcher";
	public static final String SERVICE_INSTANTIATE_SCHEMA = "instantiateSchema";
	public static final String SERVICE_GENERATE_TEST_DATA = "generateTestData";
	public static final String SERVICE_GENERATE_SCHEMA = "generateSchema";
	public static final String SERVICE_RETRIEVE_LATE_BINDING_BOX_SUB_PROCESSES = "retrieveLateBindingBoxSubProcesses";
	public static final String SERVICE_SELECT_LATE_BINDING_SEQUENCE = "selectLateBindingSequence";
	public static final String SERVICE_RETRIEVE_LATE_MODELING_BOX = "retrieveLateModelingBox";
	public static final String SERVICE_SELECT_LATE_MODELING_BOX_CONTENT = "selectLateModelingBoxContentService";
	public static final String SERVICE_TERMINATE_DECLARATIVE_PROCESS_INSTANCE = "terminateDeclarativeProcessInstanceService";

	public static final String POJO_CACHE_SERVICE = "jboss.cache:service=PojoCache";
	public static final String NAMESPACE_USER_CACHE = "pojo/user/";
	public static final String NAMESPACE_INSTANCE_CACHE = "pojo/instance/";
	public static final String NAMESPACE_SCHEMA_CACHE = "pojo/schema/";
	public static final String RELATIVE_NAMESPACE_SCHEMA_CACHE = "schema";
	public static final String RELATIVE_NAMESPACE_POJO_CACHE = "pojo";
	public static final String NAMESPACE_QUEUE = "jms/queue/";

	// logging constants
	public static final String SCHEMA = "schema";
	public static final String NODE = "node";
	public static final String PROCESS_INSTANCE = "process_instance";

	public static final String DESTROY_CACHE = "Jakob";
}
