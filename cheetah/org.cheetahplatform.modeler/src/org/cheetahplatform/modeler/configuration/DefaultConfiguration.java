package org.cheetahplatform.modeler.configuration;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.export.IExportComputation;
import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;

public class DefaultConfiguration implements IConfiguration {

	private static Map<String, Object> NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS;
	private static Map<String, Object> NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS;
	private static Set<String> MANDATORY_SETTINGS;

	static {
		// By default, all admin configuration options are initialized to "true". Here all non-standard options are listed.
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS = new HashMap<String, Object>();
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(START_EXPERIMENTAL_WORKFLOW_ENGINE_AUTOMATICALLY, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(SHELL_STYLE, SWT.SHELL_TRIM);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(ASK_WHEN_CLOSING_MODELING_EDITOR, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(INITIAL_ACTIVITIY_SIZE, new Point(120, 50));
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(INITIAL_GATEWAY_SIZE, new Point(30, 30));
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(INITIAL_EVENT_SIZE, new Point(24, 24));
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(EXIT_MODELER_AFTER_WORKFLOW_FINISHED, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(ASK_FOR_WRITING_DOWN_ID, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(SHOW_START_MODELING_DIALOG, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(CLOSE_CHEETAH_WHEN_NO_WORKFLOW_CODE_IS_ENTERED, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(SHOW_LAYOUT_DIALOG, false);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(EXPORT_COMPUTATIONS, new ArrayList<IExportComputation>());
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(DECSERFLOW_CONFIGURATOR, null);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(GRAPH_EDITOR_TOOL_CONTRIBUTOR, null);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(SHOW_TOOL_BAR, true);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(SHELL_TITLE, "Cheetah Experimental Platform");
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(TDM_SHOW_EXECUTION_ASSERTION_AREA, true);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(TDM_SHOW_TERMINATION_ASSERTION_AREA, true);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(TDM_END_HOUR, 24);
		NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.put(TDM_PLANNING_AREA_WIDTH, 150);

		// By default, all admin configuration options are initialized to "false". Here all non-standard options are listed.
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS = new HashMap<String, Object>();
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(START_EXPERIMENTAL_WORKFLOW_ENGINE_AUTOMATICALLY, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(SHELL_STYLE, ~SWT.CLOSE & SWT.SHELL_TRIM & SWT.TOOL);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(ASK_WHEN_CLOSING_MODELING_EDITOR, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(INITIAL_ACTIVITIY_SIZE, new Point(120, 50));
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(INITIAL_GATEWAY_SIZE, new Point(30, 30));
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(INITIAL_EVENT_SIZE, new Point(24, 24));
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(EXIT_MODELER_AFTER_WORKFLOW_FINISHED, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(ASK_FOR_WRITING_DOWN_ID, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(SHOW_START_MODELING_DIALOG, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(CLOSE_CHEETAH_WHEN_NO_WORKFLOW_CODE_IS_ENTERED, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(EXPORT_COMPUTATIONS, new ArrayList<IExportComputation>());
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(DECSERFLOW_CONFIGURATOR, null);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(GRAPH_EDITOR_TOOL_CONTRIBUTOR, null);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(ALLOW_DELETION_OF_NODES, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(ALLOW_RENAMING_OF_NODES, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(ALLOW_CHANGING_OF_DECSERFLOW_ACTIVITY_DESCRIPTION, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(SHOW_TOOL_BAR, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(SHELL_TITLE, "Cheetah Experimental Platform");
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(TDM_SHOW_EXECUTION_ASSERTION_AREA, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(TDM_SHOW_TERMINATION_ASSERTION_AREA, true);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(TDM_END_HOUR, 24);
		NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.put(TDM_PLANNING_AREA_WIDTH, 150);

		MANDATORY_SETTINGS = new HashSet<String>();
		MANDATORY_SETTINGS.add(SHELL_STYLE);
		MANDATORY_SETTINGS.add(INITIAL_ACTIVITIY_SIZE);
		MANDATORY_SETTINGS.add(INITIAL_EVENT_SIZE);
		MANDATORY_SETTINGS.add(INITIAL_GATEWAY_SIZE);
	}

	private Map<String, Object> settings;

	public DefaultConfiguration() {
		settings = new HashMap<String, Object>();
	}

	@Override
	public void enableFullAdminMode() {
		Field[] fields = IConfiguration.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				String name = (String) field.get(null);
				Object value = Boolean.TRUE;
				if (NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.containsKey(name)) {
					value = NON_DEFAULT_ADMIN_CONFIGURATION_OPTIONS.get(name);
				}

				set(name, value);
			} catch (Exception e) {
				Activator.logError("Could not initialize the cheetah platform.", e);
			}
		}

		for (IEdgeDescriptor descriptor : EditorRegistry.getEdgeDescriptors(EditorRegistry.DECSERFLOW)) {
			set(descriptor.getId(), true);
		}
	}

	@Override
	public void enableModelerMode() {
		Field[] fields = IConfiguration.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				String name = (String) field.get(null);
				Object value = Boolean.FALSE;
				if (NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.containsKey(name)) {
					value = NON_DEFAULT_MODELER_CONFIGURATION_OPTIONS.get(name);
				}

				set(name, value);
			} catch (Exception e) {
				Activator.logError("Could not initialize the cheetah platform.", e);
			}
		}
	}

	@Override
	public boolean getBoolean(String key) {
		Object value = settings.get(key);
		if (value == null) {
			return false;
		}

		if (value instanceof String) {
			return Boolean.parseBoolean((String) value);
		}

		return (Boolean) value;
	}

	@Override
	public Object getObject(String key) {
		return settings.get(key);
	}

	@Override
	public boolean isDefined(String key) {
		return settings.containsKey(key);
	}

	@Override
	public void set(String key, Object value) {
		settings.put(key, value);
	}

	@Override
	public void validate() {
		for (String mandatorySetting : MANDATORY_SETTINGS) {
			String message = "Undefined mandatory setting: {0}\nYou may use enableFullAdminMode() or enableModelerMode() to define the basic configuration and then refine it.";
			Assert.isTrue(isDefined(mandatorySetting), MessageFormat.format(message, mandatorySetting));
		}
	}

}
