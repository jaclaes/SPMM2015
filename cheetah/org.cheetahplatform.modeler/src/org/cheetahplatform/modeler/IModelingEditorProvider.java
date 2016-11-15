package org.cheetahplatform.modeler;

public interface IModelingEditorProvider {
	/**
	 * Determine whether the given id represents a modeling <b>activity</b>, i.e., is the id of an experimental workflow activity that
	 * offers a modeler.
	 * 
	 * @param id
	 *            the id
	 * @return <code>true</code> if it is a modeling id, <code>false</code> otherwise
	 */
	boolean isModelingEditorActivityId(String id);

	/**
	 * Translates the given editor id to an Eclipse Editor id.
	 * 
	 * @param id
	 *            the id to be translated
	 * @return the translated id, <code>null</code> if could not be translated
	 */
	String translateToEclipseEditorId(String id);
}
