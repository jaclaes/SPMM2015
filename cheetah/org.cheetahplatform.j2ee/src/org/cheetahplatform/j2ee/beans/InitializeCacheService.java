package org.cheetahplatform.j2ee.beans;

import java.lang.management.ManagementFactory;
import java.util.HashMap;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.jboss.cache.pojo.PojoCache;
import org.jboss.cache.pojo.PojoCacheFactory;
import org.jboss.cache.pojo.jmx.PojoCacheJmxWrapper;
import org.jboss.cache.pojo.jmx.PojoCacheJmxWrapperMBean;

import com.sun.appserv.server.ServerLifecycleException;

public class InitializeCacheService extends AbstractMessageDrivenBean implements MessageListener {

	public static final String CACHE_NAME = "jboss.cache:service=PojoCache";

	@SuppressWarnings("cast")
	@Override
	protected void doOnMessage(Message message) throws Exception {
		try {
			System.out.println("INIT_POJO_CACHE STARTED");
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			PojoCache cache = PojoCacheFactory.createCache("replSync-service.xml", false);
			PojoCacheJmxWrapperMBean wrapper = new PojoCacheJmxWrapper(cache);
			ObjectName name = new ObjectName(CACHE_NAME);
			server.registerMBean(wrapper, name);
			cache.create();
			cache.start();

			PojoCacheJmxWrapperMBean instance = (PojoCacheJmxWrapperMBean) MBeanServerInvocationHandler.newProxyInstance(server, name,
					PojoCacheJmxWrapperMBean.class, false);

			PojoCache pojoCache = instance.getPojoCache();
			if (pojoCache == null) {
				throw new IllegalStateException("PojoCache not initialized correctly!");
			}

			System.out.println("INIT_POJO_CACHE COMPLETED");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerLifecycleException(e);
		}

		sendReply(message, new HashMap<String, Object>());
	}

}
