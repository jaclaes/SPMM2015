package org.cheetahplatform.j2ee;

import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.shared.ActivityInstanceHandle;
import org.cheetahplatform.shared.DeclarativeProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessInstanceHandle;
import org.cheetahplatform.shared.ProcessSchemaHandle;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;

import com.thoughtworks.xstream.XStream;

public class XStreamProvider {
	public static XStream createConfiguredXStream() {
		XStream xStream = new XStream();
		xStream.setClassLoader(INode.class.getClassLoader());

		xStream.alias("ProcessSchema", ProcessSchemaHandle.class);
		xStream.alias("ProcessInstance", ProcessInstanceHandle.class);
		xStream.alias("DeclarativeProcessInstance", DeclarativeProcessInstanceHandle.class);
		xStream.alias("Activity", ActivityInstanceHandle.class);

		try {
			xStream.alias("PersistentList", PersistentList.class); //$NON-NLS-1$
			xStream.registerConverter(new PersistentListConverter(xStream.getMapper()));
			xStream.alias("PersistentSet", PersistentSet.class); //$NON-NLS-1$
			xStream.registerConverter(new PersistentSetConverter(xStream.getMapper()));
			xStream.alias("PersistentMap", PersistentMap.class);//$NON-NLS-1$
			xStream.registerConverter(new PersistentMapConverter(xStream.getMapper()));
		} catch (NoClassDefFoundError e) {
			// ignore, hibernate libs may not be persistens
		}

		return xStream;
	}
}
