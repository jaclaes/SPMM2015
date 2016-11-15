package org.cheetahplatform.modeler.experiment;

import java.util.HashMap;
import java.util.Map;

import org.cheetahplatform.modeler.configuration.IConfiguration;

public class ChangePatternRegistry {
	//key, display map
	private static Map<String, String> patterns = new HashMap<String, String>();

	static {
		patterns.put(IConfiguration.CHANGE_PATTERN_SERIAL_INSERT, "Serial Insert");
		patterns.put(IConfiguration.CHANGE_PATTERN_PARALLEL_INSERT, "Parallel Insert");
		patterns.put(IConfiguration.CHANGE_PATTERN_CONDITIONAL_INSERT, "Conditional Insert");
		patterns.put(IConfiguration.CHANGE_PATTERN_UPDATE_CONDITION, "Update Condition");
		patterns.put(IConfiguration.CHANGE_PATTERN_DELETE_PROCESS_FRAGMENT, "Delete Process Fragment");
		patterns.put(IConfiguration.CHANGE_PATTERN_RENAME_ACTIVITY, "Rename Activity");
		patterns.put(IConfiguration.CHANGE_PATTERN_EMBED_IN_LOOP, "Embed in Loop");
		patterns.put(IConfiguration.CHANGE_PATTERN_EMBED_IN_CONDITIONAL_BRANCH, "Embed in Conditional Branch");
	}
	
	public static Map<String, String> getAll(){
		return patterns;
	}
	
	public static String getDisplay(String key){
		return patterns.get(key);
	}
	
}
