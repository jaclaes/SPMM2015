package org.cheetahplatform.test.core.imperative.modeling;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.imperative.modeling.ImperativeActivity;
import org.cheetahplatform.core.imperative.modeling.ImperativeProcessSchema;
import org.junit.Test;

public class ImperativeProcessSchemaTest {

	@Test
	public void createTask() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		ImperativeActivity task = schema.createActivity("the name");
		assertEquals("the name", task.getName());

		assertEquals("Expected start node, end node and the activity", 3, schema.getNodes().size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getTask() throws Exception {
		ImperativeProcessSchema schema = new ImperativeProcessSchema();
		schema.createActivity("");

		List<INode> tasks = schema.getNodes();
		tasks.remove(0);
	}
}
