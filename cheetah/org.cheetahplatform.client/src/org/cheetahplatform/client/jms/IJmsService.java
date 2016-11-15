package org.cheetahplatform.client.jms;

import javax.jms.JMSException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.part.WorkbenchPart;

public interface IJmsService {

	public abstract void asynchronousRequest(IServiceCallback callback, final WorkbenchPart workbenchPart) throws JMSException;

	public abstract IStatus synchronousRequest() throws JMSException;

}