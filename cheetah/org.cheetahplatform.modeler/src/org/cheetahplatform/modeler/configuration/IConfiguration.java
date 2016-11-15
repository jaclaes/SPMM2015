package org.cheetahplatform.modeler.configuration;

public interface IConfiguration {
	// boolean configuration options
	String NEW_BPMN_MODEL = "new_bpmn_model";
	String NEW_BPMN_CHANGE_TASK_MODEL = "new_bpmn_change_task_model";
	String REPLAY = "replay";
	String DEMO_REPLAY = "demo_replay";
	String EXPORT_MODELING_PROCESS = "export_modeling_process";
	String EXPORT_CSV = "export_csv";
	String PREFERENCES_DIALOG = "preferences_dialog";
	String NEW_DECSERFLOW_MODEL = "new_decserflow_model";
	String NEW_CHANGE_PATTERN_MODEL = "new_change_pattern_model";
	String NEW_CHANGE_PATTERN_CHANGE_TASK = "new_change_pattern_change_task";
	String MAP_PARAGRAPH = "map_paragraph";
	String EXPORT_MODEL_TO_IMAGE = "export_model_to_image";
	String SHOW_ID_NOT_TASK_NAME = "show_id_not_task_name";
	String SHOW_ID_AND_TASK_NAME = "show_id_and_task_name";
	String CONFIGURE_PARAGRAPH_MAPPING = "configure_paragraph_mapping";
	String SHOW_TUTORIAL = "show_tutorial";
	String EXPORT_PARAGRAPH_MAPPING = "export_paragraph_mapping";
	String EXPORT_CUSTOM_GRAPH_NOTATION = "export_custom_graph_notation";
	String EXPORT_MODELING_DIAGRAM = "export_modeling_diagram";
	String EXPORT_MODELING_PHASES_DIAGRAM = "export_modeling_phases_diagram";
	String EXPORT_STATISTICS_ACTION = "export_statistics_action";
	String EXPORT_PPM_STATISTICS = "export_ppm_statistics";
	String EXPORT_EXPERIMENTAL_PPM_STATISTICS = "export_exp_ppm_statistics";
	String EXPORT_SYNTAX_STATISTIC = "export_syntax_statistic";
	String EXPORT_RENAMING_STATISTIC = "export_renaming_statistic";
	String EXPORT_LAYOUT_STATISTICS = "export_layout_statistics";
	String CONTINUE_MODELING_PROCESS = "continue_modeling_process";
	String NEW_TDM_MODEL = "new_tdm_model";
	String EXPORT_EXPERIMENTAL_WORKFLOW = "export_experimental_workflow";
	String EXPORT_CHUNKS = "export_chunks";
	String EXPORT_DURATION_TO_LAYOUT = "export_duration_to_layout";
	String EXPORT_ITERATIONS = "export_iterations";
	String EXPORT_AUDIT_TRAIL_ENTRIES_TO_CSV = "export_audit_trail_entries";
	String EXPORT_CHUNK_OVERVIEW = "export_chunk_overview";
	String EXPORT_MODELING_PAUSES = "export_modeling_pauses";
	String COMPUTE_SIZE = "compute_size";
	String COMPUTE_CONSTRAINTS = "compute_constraints";
	String RECTANGULAR_EDGES = "rectangular_edges";
	String ALIGN_HORIZONTALLY = "align_horizontally";
	String ALIGN_VERTICALLY = "align_vertically";
	String LAYOUT = "layout";
	String START_EXPERIMENTAL_WORKFLOW_ENGINE_AUTOMATICALLY = "start_experimental_workflow_engine_automatically";
	String ASK_WHEN_CLOSING_MODELING_EDITOR = "ask_when_closing_modeling_editor";
	String NODES_RESIZABLE = "nodes_resizable";
	String EXIT_MODELER_AFTER_WORKFLOW_FINISHED = "exit_modeler_after_workflow_finished";
	String CONVERT_XML_LOG_FILES = "convert_xml_log_files";
	String ASK_FOR_WRITING_DOWN_ID = "ask_for_writing_down_id";
	String CLOSE_CHEETAH_WHEN_NO_WORKFLOW_CODE_IS_ENTERED = "close_cheetah_when_no_workflow_code_is_entered";
	String SHOW_LAYOUT_DIALOG = "show_layout_dialog";
	String TDM_CASE_STUDY_QUESTIONNAIRE = "tdm_case_study_questionnaire";
	String DUPLICATE_PROCESS = "duplicate_process";
	String ALLOW_MODIFICATION_OF_TESTS = "allow_modification_of_tests";
	String SHOW_MENU_BAR = "show_menu_bar";
	String SHOW_TOOL_BAR = "show_tool_bar";
	String DELETE_EDGES_WHEN_DELETING_NODE = "delete_edges_when_deleting_nodes";
	String ALLOW_DELETION_OF_NODES = "allow_deletion_of_nodes";
	String ALLOW_RENAMING_OF_NODES = "allow_renaming_of_nodes";
	String ALLOW_CHANGING_OF_DECSERFLOW_ACTIVITY_DESCRIPTION = "allow_changing_of_decserflow_activity_description";
	String FIND_RESTORED_WORKFLOWS = "find_restored_workflows";
	String DELETE_BEND_POINTS = "delete_bend_points";
	String SHOW_LAYOUT_TUTORIAL = "show_layout_tutorial";
	String SHOW_CHANGE_PATTERN_TUTORIAL = "show_change_pattern_tutorial";
	String MAP_PARAGRAPH_BATCH_VERSION = "map_paragraph_batch_version";
	String EXPORT_DECLARE_MODEL = "export_declare_model";
	String IMPORT_MODELING_TRANSCRIPTS = "import_modeling_transcripts";
	String IMPORT_ATTRIBUTE = "import_attribute";
	String EDIT_MODELING_TRANSCRIPTS = "edit_modeling_transcripts";
	String HIERARCHICAL_ACTIVITIES = "hierarchial_activites";
	String SHOW_TASK_DESCRIPTION = "show_task_description";
	String EXPORT_PHASE_SIMILARITY = "export_phase_similarity";
	String EXPORT_CLUSTERING_DATA = "export_clustering_data";
	String TDM_SHOW_EXECUTION_ASSERTION_AREA = "tdm_show_execution_assertion_area";
	String TDM_SHOW_TERMINATION_ASSERTION_AREA = "tdm_show_termination_assertion_area";
	String TDM_ASK_FOR_CONSISTENCY = "tdm_ask_for_consistency";
	String EXPORT_ACTIVITY_NAMES = "export_activity_names";
	String EXPORT_MODELING_STEPS = "export_modeling_steps";
	String NEW_LIPROMO_MODEL = "new_lipromo_model_action";
	String SHOW_MODELING_INSTANCE_MAPPING = "show_modeling_instance_mapping";
	String EXPORT_STEP_CATEGORIZATION = "export_step_categorization";
	String IMPORT_BPMN_XML = "import_bpmn_xml";
	String SHOW_TUTORIAL_SKIP_STEPS = "show_tutorial_skip_steps";

	// non-boolean configuration options
	String EXPERIMENT = "experiment";
	String SHELL_STYLE = "shell_style";
	String INITIAL_ACTIVITIY_SIZE = "initial_activity_size";
	String INITIAL_GATEWAY_SIZE = "initial_gateway_size";
	String INITIAL_EVENT_SIZE = "initial_event_size";
	String EXPORT_COMPUTATIONS = "export_computations";
	String DECSERFLOW_CONFIGURATOR = "decserflow_configurator";
	String SHELL_TITLE = "shell_title";
	String CHANGE_PATTERN_SERIAL_INSERT = "change_pattern_serial_insert";
	String CHANGE_PATTERN_PARALLEL_INSERT = "change_pattern_parallel_insert";
	String CHANGE_PATTERN_CONDITIONAL_INSERT = "change_pattern_conditional_insert";
	String CHANGE_PATTERN_UPDATE_CONDITION = "change_pattern_update_condition";
	String CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT = "change_pattern_delete_process_fragment";
	String CHANGE_PATTERN_RENAME_ACTIVITY = "change_pattern_rename_activity";
	String CHANGE_PATTERN_EMBED_IN_LOOP = "change_pattern_embed_in_loop";
	String CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH = "change_pattern_embed_conditional_branch";
	String CHANGE_PATTERN_UNDO = "change_pattern_undo";
	String CHANGE_PATTERN_LAYOUT = "change_pattern_layout";
	String SHOW_START_MODELING_DIALOG = "show_start_modeling_dialog";
	String PPM_DASHBOARD_REPLAY = "ppm_dashboard_replay";
	String TDM_END_HOUR = "tdm_end_hour";
	String TDM_PLANNING_AREA_WIDTH = "planning_area_width";
	String GRAPH_EDITOR_TOOL_CONTRIBUTOR = "graph_editor_tool_contributor";
	String ASK_FOR_UNDERSTANDABILITY_EXPLANATION = "ask_for_understandability_explanation";

	// set this to false if checkboxes are used in surveys. Set it to true otherwise to aggregate the results in one column when exporting
	// to CSV
	String AGGREGATE_CHOICE_VALUES = "aggregate_choice_values";

	/**
	 * Enable all options to provide admin behavior.
	 */
	void enableFullAdminMode();

	/**
	 * Configure for use as modeler, i.e., disable everything that is not needed.
	 */
	void enableModelerMode();

	/**
	 * Get a boolean property.
	 *
	 * @param key
	 *            the key for which the property should be retrieved
	 * @return the associated boolean value
	 */
	boolean getBoolean(String key);

	/**
	 * Get an object property.
	 *
	 * @param key
	 *            the key for which the property should be retrieved
	 * @return the associated object, may be <code>null</code>
	 */
	Object getObject(String key);

	/**
	 * Determine whether the given property has been set.
	 *
	 * @param key
	 *            the property
	 * @return <code>true</code> if the property has been defined, <code>false</code> if not
	 */
	boolean isDefined(String key);

	/**
	 * Set the given property.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the property to set
	 */
	void set(String key, Object value);

	/**
	 * Validate the configuration, e.g., if all required options are set. Throws an unchecked exception if the configuration is not valid.
	 */
	void validate();
}
