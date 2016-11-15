package org.cheetahplatform.common.ui.dialog;

import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_HOST;
import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_TIMESTAMP;

import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabaseUtil;

public class ProcessInstanceDatabaseHandle extends AbstractDatabaseHandle {

	private static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	public static final String UNKNOWN_HOST = "Unknown host";

	private final String id;
	private final String processId;
	private ProcessInstance instance;
	private List<AbstractDatabaseHandle> children;

	public ProcessInstanceDatabaseHandle(ExperimentalWorkflowElementDatabaseHandle child) {
		this(child.getDatabaseId(), child.getAttribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE), child.getAttributes(), null);
	}

	public ProcessInstanceDatabaseHandle(long dataBaseId, String id, List<Attribute> data, String processId) {
		super(dataBaseId);

		this.id = id;
		this.processId = processId;
		this.children = new ArrayList<AbstractDatabaseHandle>();
		addAttributes(data);
	}

	public ProcessInstanceDatabaseHandle(long dataBaseId, String id, String data, String processId) {
		this(dataBaseId, id, DatabaseUtil.fromDataBaseRepresentation(data), processId);
	}

	public void addChild(AbstractDatabaseHandle handle) {
		children.add(handle);
	}

	public int compareCreationDate(ProcessInstanceDatabaseHandle other) {
		Date creationDate = getTimeAsDate();

		if (creationDate == null || other.getTimeAsDate() == null) {
			return 0; // not comparable
		}

		return creationDate.compareTo(other.getTimeAsDate());
	}

	public int compareHost(ProcessInstanceDatabaseHandle other) {
		String host = getHost();
		String otherHost = other.getHost();

		if (host == null || otherHost == null) {
			return 0;
		}

		return Collator.getInstance().compare(host, otherHost);
	}

	@Override
	public String getAttribute(String name) {
		return getAttributeSafely(name);
	}

	public List<AbstractDatabaseHandle> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public String getHost() {
		if (isAttributeDefined(ATTRIBUTE_HOST)) {
			return getAttribute(ATTRIBUTE_HOST);
		}

		return UNKNOWN_HOST;
	}

	@Override
	public String getId() {
		return id;
	}

	public ProcessInstance getInstance() {
		return instance;
	}

	public String getProcessId() {
		return processId;
	}

	public String getTime() {
		if (isAttributeDefined(ATTRIBUTE_TIMESTAMP)) {
			long time = getLongAttribute(ATTRIBUTE_TIMESTAMP);
			return FORMAT.format(new Date(time));
		}

		return "";
	}

	public Date getTimeAsDate() {
		if (isAttributeDefined(ATTRIBUTE_TIMESTAMP)) {
			long time = getLongAttribute(ATTRIBUTE_TIMESTAMP);
			return new Date(time);
		}

		return null;
	}

	public boolean hasChildWithId(String id) {
		for (AbstractDatabaseHandle child : children) {
			if (child.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	public void setInstance(ProcessInstance instance) {
		this.instance = instance;
	}
}
