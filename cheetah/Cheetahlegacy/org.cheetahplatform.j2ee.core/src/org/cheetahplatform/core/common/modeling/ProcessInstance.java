/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/

package org.cheetahplatform.core.common.modeling;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.core.common.EventType;
import org.cheetahplatform.core.common.IdentifiableObject;
import org.cheetahplatform.core.common.NamedIdentifiableObject;
import org.cheetahplatform.shared.CheetahConstants;
import org.cheetahplatform.shared.ListenerList;

public abstract class ProcessInstance extends NamedIdentifiableObject implements IProcessInstance {
	private static final long serialVersionUID = 3816009423965770852L;

	protected final ProcessSchema schema;
	private final ListenerList terminationListener;
	private transient ListenerList logListeners;
	private transient List<AuditTrailEntry> logEntries;

	protected ProcessInstance(ProcessSchema schema) {
		this(schema, "");
	}

	protected ProcessInstance(ProcessSchema schema, String name) {
		super(name);

		this.schema = schema;
		terminationListener = new org.cheetahplatform.shared.ListenerList();
		logListeners = new ListenerList();
		logEntries = new ArrayList<AuditTrailEntry>();

		log(EventType.INSTANTIATE, this, CheetahConstants.SCHEMA, String.valueOf(schema.getCheetahId()));
	}

	public void addLogListener(ILogListener listener) {
		addLogListener(listener, true);
	}

	public void addLogListener(ILogListener listener, boolean obtainHistory) {
		getLogListeners().add(listener);

		if (obtainHistory) {
			for (AuditTrailEntry entry : getLogEntries()) {
				listener.log(entry);
			}
		}
	}

	public void addTerminationListener(ITerminationListener listener) {
		terminationListener.add(listener);
	}

	protected void fireInstanceTerminated() {
		for (Object listener : terminationListener.getListeners()) {
			((ITerminationListener) listener).terminated(this);
		}
	}

	private List<AuditTrailEntry> getLogEntries() {
		if (logEntries == null) {
			logEntries = new ArrayList<AuditTrailEntry>();
		}

		return logEntries;
	}

	private ListenerList getLogListeners() {
		if (logListeners == null) {
			logListeners = new ListenerList();
		}

		return logListeners;
	}

	public abstract INodeInstance getNodeInstance(long id);

	public ProcessSchema getSchema() {
		return schema;
	}

	public void log(AuditTrailEntry entry) {
		getLogEntries().add(entry);

		for (Object object : getLogListeners().getListeners()) {
			((ILogListener) object).log(entry);
		}
	}

	/**
	 * Log the given type of event for object and data.
	 * 
	 * @param type
	 *            the type
	 * @param object
	 *            the object which is affected by the event
	 * @param data
	 *            associated data, subsequently keys and values (i.e., key1, value1, key2, value2)
	 */
	public void log(EventType type, IdentifiableObject object, String... data) {
		DateTime currentTime = DateTimeProvider.getDateTimeSource().getCurrentTime(true);
		AuditTrailEntry entry = new AuditTrailEntry(currentTime.toJavaUtilDate(), type.name(), String.valueOf(object.getCheetahId()), null,
				new ArrayList<Attribute>());

		Assert.isTrue(data.length % 2 == 0);
		for (int i = 0; i < data.length; i += 2) {
			entry.setAttribute(data[i], data[i + 1]);
		}

		log(entry);
	}

	public void removeLogListener(ILogListener listener) {
		getLogListeners().remove(listener);
	}

}
