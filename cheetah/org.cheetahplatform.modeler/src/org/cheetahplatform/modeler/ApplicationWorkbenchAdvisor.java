package org.cheetahplatform.modeler;

import java.sql.Connection;
import java.sql.SQLException;

import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.common.logging.db.StaticPromWriteProvider;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public void eventLoopException(Throwable exception) {
		super.eventLoopException(exception);

		exception.printStackTrace();
		logToCentralDatabase(exception);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		Platform.addLogListener(new ILogListener() {

			@Override
			public void logging(IStatus status, String plugin) {
				logToCentralDatabase(status.getException());
			}
		});

		try {
			CheetahPlatformConfigurator.getInstance().readConfiguration();
		} catch (CoreException e) {
			Activator.logError("Could not configure cheetah.", e);
		}

		try {
			Bundle adminBundle = Platform.getBundle("org.cheetahplatform.modeler.admin");
			if (adminBundle != null) {
				adminBundle.start();
			}
		} catch (BundleException e) {
			Activator.logError("Could not start the admin bundle.", e);
		}
	}

	private void logToCentralDatabase(Throwable exception) {
		Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An error occurred.", exception);

		if (org.cheetahplatform.common.Activator.getDatabaseConnector().checkConnection()) {
			new PromLogger(new StaticPromWriteProvider(null)).logError(status);
		} else if (XMLLogHandler.getInstance().isEnabled()) {
			Connection connection = null;

			try {
				connection = XMLLogHandler.getInstance().openConnection();
				new PromLogger(new StaticPromWriteProvider(null)).logError(status, connection);
			} catch (SQLException e) {
				// ignore
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						// ignore
					}
				}
			}
		}
	}

	@Override
	public boolean preShutdown() {
		org.cheetahplatform.common.Activator.getDatabaseConnector().shutDown();

		return super.preShutdown();
	}
}
