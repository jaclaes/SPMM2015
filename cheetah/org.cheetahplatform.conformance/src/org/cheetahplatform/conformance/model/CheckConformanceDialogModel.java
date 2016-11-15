package org.cheetahplatform.conformance.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.conformance.core.BPMNTraceValidatorFactory;
import org.cheetahplatform.conformance.core.CheckConformanceRunnable;
import org.cheetahplatform.conformance.core.DeclarativeTraceValidatorFactory;
import org.cheetahplatform.conformance.core.ITraceValidator;
import org.cheetahplatform.conformance.core.ITraceValidatorFactory;

public class CheckConformanceDialogModel {
	private List<ITraceValidatorFactory> validators;
	private ITraceValidator firstValidator;
	private ITraceValidator secondValidator;

	public CheckConformanceDialogModel() {
		validators = new ArrayList<ITraceValidatorFactory>();
		validators.add(new DeclarativeTraceValidatorFactory());
		validators.add(new BPMNTraceValidatorFactory());
	}

	public CheckConformanceRunnable createCheckConformanceRunnable() {
		return new CheckConformanceRunnable(firstValidator, secondValidator);
	}

	private ITraceValidator getValidator(File inputFile) throws Exception {
		for (ITraceValidatorFactory validator : validators) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(inputFile);
				if (validator.understands(input)) {
					return validator.createValidator(inputFile.getName(), new FileInputStream(inputFile));
				}

			} catch (IOException e) {
				// ignore
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}

		throw new IllegalArgumentException("No validator found for: " + inputFile);
	}

	public void setFirstFile(String file) throws Exception {
		firstValidator = getValidator(new File(file));
	}

	public void setSecondFile(String file) throws Exception {
		secondValidator = getValidator(new File(file));
	}

}
