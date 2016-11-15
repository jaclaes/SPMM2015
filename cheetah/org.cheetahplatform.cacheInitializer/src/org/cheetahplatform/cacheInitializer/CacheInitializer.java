package org.cheetahplatform.cacheInitializer;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.jboss.cache.pojo.PojoCache;
import org.jboss.cache.pojo.PojoCacheFactory;
import org.jboss.cache.pojo.jmx.PojoCacheJmxWrapper;
import org.jboss.cache.pojo.jmx.PojoCacheJmxWrapperMBean;

import com.sun.appserv.server.LifecycleEvent;
import com.sun.appserv.server.LifecycleListener;
import com.sun.appserv.server.ServerLifecycleException;

public class CacheInitializer implements LifecycleListener {
	public static final String CACHE_NAME = "jboss.cache:service=PojoCache";

	public void handleEvent(LifecycleEvent event) throws ServerLifecycleException {
		if (event.getEventType() == LifecycleEvent.STARTUP_EVENT) {
			handleReadyEvent();
		} else if (event.getEventType() == LifecycleEvent.SHUTDOWN_EVENT) {
			handleShutDownEvent();
		}
	}

	private void handleShutDownEvent() throws ServerLifecycleException {
		try {
			System.out.println("SHUTDOWN_POJO_CACHE STARTED");
			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = new ObjectName(CACHE_NAME);
			PojoCacheJmxWrapperMBean instance = (PojoCacheJmxWrapperMBean) MBeanServerInvocationHandler.newProxyInstance(server, name,
					PojoCacheJmxWrapperMBean.class, false);
			System.out.println("STOP_POJO_CACHE");
			instance.stop();
			System.out.println("DESTROY_POJO_CACHE");
			instance.destroy();
			System.out.println("SHUTDOWN_POJO_CACHE COMPLETED");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerLifecycleException(e);
		}
	}

	private void handleReadyEvent() throws ServerLifecycleException {
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
	}
}
