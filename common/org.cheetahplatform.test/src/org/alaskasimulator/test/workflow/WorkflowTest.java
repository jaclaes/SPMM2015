/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.alaskasimulator.test.workflow;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.alaskasimulator.config.model.WorkflowConfiguration;
import org.alaskasimulator.workflow.ui.actions.WorkflowExecutionAction;
import org.junit.Test;

public class WorkflowTest {

	private static final String FILE_PATH = "testdata/junit/testWorkflow.zip";

	@Test
	public void loadWorkflowConfigurationFileTest() throws ZipException, IOException {
		WorkflowExecutionAction action = new WorkflowExecutionAction(null);
		WorkflowConfiguration workflowConfig = action.loadWorkflowConfigurationFile(new ZipFile(new File(FILE_PATH)));
		assertEquals(workflowConfig.getName(), "testWorkflow");
		assertEquals(workflowConfig.getWorkflowElements().size(), 5);
	}
}
