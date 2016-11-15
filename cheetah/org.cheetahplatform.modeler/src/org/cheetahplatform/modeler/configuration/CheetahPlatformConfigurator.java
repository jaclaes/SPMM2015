package org.cheetahplatform.modeler.configuration;

import static org.cheetahplatform.common.Activator.logError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.cheetahplatform.common.logging.db.ConnectionSetting;
import org.cheetahplatform.common.logging.db.IDatabaseConnector;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.export.IExportComputation;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

public class CheetahPlatformConfigurator {

	public static void enableFullAdminMode() {
		getInstance().getConfiguration().enableFullAdminMode();
	}

	public static void enableModelerMode() {
		getInstance().getConfiguration().enableModelerMode();
	}

	public static boolean getBoolean(String key) {
		return getInstance().getConfiguration().getBoolean(key);
	}

	public static CheetahPlatformConfigurator getInstance() {
		return INSTANCE;
	}

	public static Object getObject(String key) {
		return getInstance().getConfiguration().getObject(key);
	}

	public static void setBoolean(String key, boolean value) {
		getInstance().getConfiguration().set(key, String.valueOf(value));
	}

	private static final String EXPORT_COMPUTATION = "exportComputation";
	private static final String EMAIL = "email";
	private static final String XML_LOG_HANDLER = "xmlLogHandler";
	private static final String ADMIN_PASSWORD = "adminPassword";
	private static final String ADMIN_USER_NAME = "adminUserName";
	private static final String CLASS = "class";
	private static final String CONFIGURATOR = "configurator";
	private static final String PASSWORD = "password";
	private static final String USER = "user";

	private static final String URL = "url";

	private static final String TOZIP = "to_zip";

	private static final String DATABASE_ACCESS = "databaseAccess";

	private static final String CONFIGURATION_EXTENSION_POINT = "org.cheetahplatform.configuration";

	private static final String SCRIPT = "cheetah.script"; //$NON-NLS-1$

	private static final String PROPERTIES = "cheetah.properties"; //$NON-NLS-1$

	private static final CheetahPlatformConfigurator INSTANCE = new CheetahPlatformConfigurator();

	private IConfiguration configuration;

	private CheetahPlatformConfigurator() {
		configuration = new DefaultConfiguration();
	}

	private void configureDatabaseAccess(IConfigurationElement element) {
		String url = element.getAttribute(URL);
		String user = element.getAttribute(USER);
		String password = element.getAttribute(PASSWORD);
		String adminUser = element.getAttribute(ADMIN_USER_NAME);
		String adminPassword = element.getAttribute(ADMIN_PASSWORD);

		IDatabaseConnector databaseConnector = org.cheetahplatform.common.Activator.getDatabaseConnector();
		ConnectionSetting setting = new ConnectionSetting(user, password, adminUser, adminPassword, url);
		databaseConnector.addPreconfiguredSetting(setting);

		if (!databaseConnector.isConnectionCustomized()) {
			databaseConnector.setDatabaseURL(url);
			databaseConnector.setDefaultCredentials(user, password);
			databaseConnector.setAdminCredentials(adminUser, adminPassword);
		}

		if (url.equals("hsqldb")) {
			configureHSQLDB();
		}
	}

	@SuppressWarnings("unchecked")
	private void configureExportComputation(IConfigurationElement child) throws CoreException {
		IExportComputation computation = (IExportComputation) child.createExecutableExtension(CLASS);
		List<IExportComputation> computations = (List<IExportComputation>) configuration.getObject(IConfiguration.EXPORT_COMPUTATIONS);
		if (computations == null) {
			computations = new ArrayList<IExportComputation>();
			configuration.set(IConfiguration.EXPORT_COMPUTATIONS, computations);
		}

		computations.add(computation);
	}

	private void configureHSQLDB() {
		Location workspaceLocation = Platform.getInstanceLocation();
		String workspace = workspaceLocation.getURL().toString();
		String databaseUrl = "jdbc:hsqldb:" + workspace + "/cheetah"; //$NON-NLS-1$ //$NON-NLS-2$
		IDatabaseConnector databaseConnector = org.cheetahplatform.common.Activator.getDatabaseConnector();
		databaseConnector.setDatabaseURL(databaseUrl);
		databaseConnector.setDefaultCredentials("sa", "");
		databaseConnector.setAdminCredentials("sa", "");

		copyDatabaseFiles();
	}

	private void configureXMLStorage(IConfigurationElement element) {
		String email = element.getAttribute(EMAIL);
		String url = element.getAttribute(URL);
		String user = element.getAttribute(USER);
		String password = element.getAttribute(PASSWORD);
		boolean toZip = element.getAttribute(TOZIP).equals("true") ? true : false;

		XMLLogHandler storage = XMLLogHandler.getInstance();
		storage.setEmail(email);
		storage.setUrl(url);
		storage.setUser(user);
		storage.setPassword(password);
		storage.setToZip(toZip);
	}

	private void copy(File workspace, String file) throws IOException, FileNotFoundException {
		URL toCopy = FileLocator.find(Activator.getDefault().getBundle(), new Path("resource/" + file), new HashMap<Object, Object>()); //$NON-NLS-1$
		toCopy = FileLocator.toFileURL(toCopy);

		FileOutputStream out = new FileOutputStream(new File(workspace, file));
		FileInputStream in = new FileInputStream(new File(toCopy.getFile()));
		out.getChannel().transferFrom(in.getChannel(), 0, in.getChannel().size());
		out.close();
		in.close();
	}

	private void copyDatabaseFiles() {
		URL workspaceUrl = Platform.getInstanceLocation().getURL();
		try {
			File workspace = new File(workspaceUrl.getFile());
			if (!workspace.exists()) {
				workspace.mkdirs();
			}

			if (!new File(workspace, SCRIPT).exists()) {
				copy(workspace, SCRIPT);
				copy(workspace, PROPERTIES);
			}
		} catch (Exception e) {
			logError("Could not copy the database files.", e); //$NON-NLS-1$
		}
	}

	private void delegateConfiguration(IConfigurationElement element) throws CoreException {
		IConfigurator configurator = (IConfigurator) element.createExecutableExtension(CLASS);
		configurator.configure(configuration);
		configuration.validate();
	}

	public IConfiguration getConfiguration() {
		return configuration;
	}

	protected int getPriority(IConfigurationElement config) {
		Assert.isLegal(config.getName().equals("configuration"));
		String priorityString = config.getAttribute("priority");

		int priority = Integer.parseInt(priorityString);
		return priority;
	}

	public void readConfiguration() throws CoreException {
		IConfigurationElement[] configurations = Platform.getExtensionRegistry().getConfigurationElementsFor(CONFIGURATION_EXTENSION_POINT);
		Assert.isLegal(configurations.length > 0);

		List<IConfigurationElement> configurationList = new ArrayList<IConfigurationElement>(Arrays.asList(configurations));
		Collections.sort(configurationList, new Comparator<IConfigurationElement>() {
			@Override
			public int compare(IConfigurationElement o1, IConfigurationElement o2) {
				return getPriority(o2) - getPriority(o1);
			}
		});
		if (configurationList.size() > 1) {
			int highest = getPriority(configurationList.get(0));
			int secondHighest = getPriority(configurationList.get(1));
			Assert.isLegal(highest > secondHighest);
		}

		IConfigurationElement highestPriority = configurationList.get(0);
		for (IConfigurationElement child : highestPriority.getChildren()) {
			if (child.getName().equals(DATABASE_ACCESS)) {
				configureDatabaseAccess(child);
			} else if (child.getName().equals(XML_LOG_HANDLER)) {
				configureXMLStorage(child);
			} else if (child.getName().equals(CONFIGURATOR)) {
				delegateConfiguration(child);
			} else if (child.getName().equals(EXPORT_COMPUTATION)) {
				configureExportComputation(child);
			}
		}
	}

	public void set(String key, Object value) {
		getInstance().getConfiguration().set(key, value);
	}

}
