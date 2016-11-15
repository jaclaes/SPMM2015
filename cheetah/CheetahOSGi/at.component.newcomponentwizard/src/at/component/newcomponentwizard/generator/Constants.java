package at.component.newcomponentwizard.generator;

/**
 * These constants are needed by the framework to check if the started bundle is a simple component, a top-level-component or neither of
 * them
 * 
 * @author Felix Schöpf
 */
public class Constants {
	/**
	 * The component-identifier String which is used as Manifest-Header-key
	 */
	public static final String COMPONENT_IDENTIFIER = "Component-Identifier";
	/**
	 * The simple-component-identifier which is used as Manifest-Header-value
	 */
	public static final String SIMPLE_COMPONENT = "component";
	/**
	 * The top-level-component-identifier which is used as Manifest-Header-value
	 */
	public static final String PROJECT_COMPONENT = "project-component";
}
