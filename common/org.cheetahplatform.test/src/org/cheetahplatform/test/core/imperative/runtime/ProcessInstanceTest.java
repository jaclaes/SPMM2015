/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.test.core.imperative.runtime;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.test.modeler.TestLogListener;
import org.junit.Test;

public class ProcessInstanceTest {

	private DeclarativeProcessInstance serializeAndDeserialize(TestLogListener listener) throws IOException, ClassNotFoundException {
		DeclarativeProcessSchema schema = new DeclarativeProcessSchema();
		schema.createActivity("A");
		DeclarativeProcessInstance instance = new DeclarativeProcessInstance(schema);
		instance.addLogListener(listener);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
		objectOutputStream.writeObject(instance);

		DeclarativeProcessInstance deserialized = (DeclarativeProcessInstance) new ObjectInputStream(new ByteArrayInputStream(out
				.toByteArray())).readObject();
		return deserialized;
	}

	@Test
	public void transientEvents() throws Exception {
		TestLogListener listener = new TestLogListener();
		DeclarativeProcessInstance deserialized = serializeAndDeserialize(listener);

		assertEquals(1, listener.getEntries().size());
		deserialized.getSchema().getNodes().get(0).instantiate(deserialized);
		assertEquals("Listener should have been cleared.", 1, listener.getEntries().size());
	}

	@Test
	public void transientListener() throws Exception {
		TestLogListener listener = new TestLogListener();
		DeclarativeProcessInstance deserialized = serializeAndDeserialize(listener);

		TestLogListener secondListener = new TestLogListener();
		deserialized.addLogListener(secondListener, true);
		assertEquals(0, secondListener.getEntries().size());
	}

}
