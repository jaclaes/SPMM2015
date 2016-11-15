package org.cheetahplatform.conformance.core;

import java.io.IOException;
import java.io.InputStream;

public interface ITraceValidatorFactory {

	/**
	 * Create a validator for the given model.
	 * 
	 * @param modelName
	 *            the name of the model, to be used for status messages
	 * @param model
	 *            the model to be checked
	 * @throws Exception
	 */
	ITraceValidator createValidator(String modelName, InputStream model) throws Exception;

	/**
	 * Determine whether the validator supports the model contained in the given stream. The validator does not need to close the given
	 * stream.
	 * 
	 * @param model
	 *            the model
	 * @return <code>true</code> if the model is supported by this validator, <code>false</code> otherwise
	 */
	boolean understands(InputStream model) throws IOException;
}
