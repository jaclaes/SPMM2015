package at.component.newcomponentwizard.generator;

/**
 * This class generates the Activator-class and all the other classes that are needed for a component-project
 * 
 * @author Felix Schöpf
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.internal.build.IPDEBuildConstants;
import org.eclipse.pde.internal.core.util.CoreUtility;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.eclipse.pde.ui.templates.PluginReference;

@SuppressWarnings("restriction")
public class ClassGenerator {
	private PluginFieldData fPluginData;
	private IProject fProject;
	private String fQualifiedClassName;

	public ClassGenerator(IProject project, String qualifiedClassName, PluginFieldData data) {
		fProject = project;
		fQualifiedClassName = qualifiedClassName;
		fPluginData = data;
	}

	/**
	 * Generates all the classes
	 * 
	 * @param monitor
	 *            The ProgressMonitor for displaying the progress in a progressbar
	 * @throws CoreException
	 *             if there occur problems while writing the files to the file-system
	 */
	public void generate(IProgressMonitor monitor) throws CoreException {
		int nameloc = fQualifiedClassName.lastIndexOf('.');
		String packageName = (nameloc == -1) ? "" : fQualifiedClassName.substring(0, nameloc); //$NON-NLS-1$
		String className = fQualifiedClassName.substring(nameloc + 1);

		IPath path = new Path(packageName.replace('.', '/'));
		if (fPluginData.getSourceFolderName().trim().length() > 0)
			path = new Path(fPluginData.getSourceFolderName()).append(path);

		CoreUtility.createFolder(fProject.getFolder(path));

		generateActivator(monitor, packageName, className, path);
		generateComponent(monitor, packageName, "Component", path);
		generateComponentStarterService(monitor, packageName, "ComponentStarterService", path);
		generateComponentUiController(monitor, packageName, "ComponentUiController", path);
	}

	/**
	 * @param monitor
	 *            The ProgressMonitor for displaying the progress in a progressbar
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param path
	 *            The filePath
	 * @throws CoreException
	 *             if there occur problems while writing the files to the file-system
	 */
	private void generateActivator(IProgressMonitor monitor, String packageName, String className, IPath path) throws CoreException {
		IFile file = fProject.getFile(path.append(className + ".java")); //$NON-NLS-1$
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		if (fPluginData.getOSGiFramework() != null) {
			generateActivatorClass(packageName, className, writer);
		}
		writer.flush();
		try {
			swriter.close();
			ByteArrayInputStream stream = new ByteArrayInputStream(swriter.toString().getBytes(fProject.getDefaultCharset()));
			if (file.exists())
				file.setContents(stream, false, true, monitor);
			else
				file.create(stream, false, monitor);
			stream.close();
		} catch (IOException e) {

		}
	}

	/**
	 * Generates the Activator java file.
	 * 
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param writer
	 *            The writer that writes the String-file
	 */
	private void generateActivatorClass(String packageName, String className, PrintWriter writer) {
		if (!packageName.equals("")) { //$NON-NLS-1$
			writer.println("package " + packageName + ";");
			writer.println();
		}
		writer.println("import org.osgi.framework.BundleActivator;"); //$NON-NLS-1$
		writer.println("import org.osgi.framework.BundleContext;");
		writer.println("import org.osgi.framework.ServiceRegistration;");
		writer.println("import org.osgi.util.tracker.ServiceTracker;");
		writer.println();
		writer.println("import at.component.IComponentStarterService;");
		writer.println("import at.component.framework.services.componentwireadmin.IComponentWireAdmin;");
		writer.println("import at.component.framework.services.log.IComponentLogService;");
		writer.println();
		writer.println("public class " + className + " implements BundleActivator {");
		writer.println();
		writer.println("\tprivate ServiceRegistration componentStarterServiceRegistration;");
		writer.println("\tprivate static ServiceTracker componentLogServiceTracker;");
		writer.println("\tprivate static ServiceTracker componentWireAmdinServiceTracker;");
		writer.println("\tprivate static BundleContext bundleContext;");
		writer.println();
		writer.println("\tpublic static BundleContext getBundleContext() {");
		writer.println("\t\treturn bundleContext;");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic static IComponentLogService getComponentLogService() {");
		writer.println("\t\treturn (IComponentLogService) componentLogServiceTracker.getService();");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic static IComponentWireAdmin getComponentWireAdmin() {");
		writer.println("\t\treturn (IComponentWireAdmin) componentWireAmdinServiceTracker.getService();");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic void start(BundleContext context) throws Exception {");
		writer.println("\t\tbundleContext = context;");
		writer
				.println("\t\tcomponentStarterServiceRegistration = context.registerService(IComponentStarterService.class.getName(), new ComponentStarterService(), null);");
		writer.println("\t\t");
		writer
				.println("\t\tcomponentWireAmdinServiceTracker = new ServiceTracker(bundleContext, IComponentWireAdmin.class.getName(), null);");
		writer.println("\t\tcomponentWireAmdinServiceTracker.open();");
		writer.println("\t\t");
		writer.println("\t\tcomponentLogServiceTracker = new ServiceTracker(bundleContext, IComponentLogService.class.getName(), null);");
		writer.println("\t\tcomponentLogServiceTracker.open();");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic void stop(BundleContext context) throws Exception {");
		writer.println("\t\tcomponentLogServiceTracker.close();");
		writer.println("\t\tcomponentWireAmdinServiceTracker.close();");
		writer.println("\t\tcomponentStarterServiceRegistration.unregister();");
		writer.println("\t}");
		writer.println("}");
	}

	/**
	 * @param monitor
	 *            The ProgressMonitor for displaying the progress in a progressbar
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param path
	 *            The filePath
	 * @throws CoreException
	 *             if there occur problems while writing the files to the file-system
	 */
	private void generateComponent(IProgressMonitor monitor, String packageName, String className, IPath path) throws CoreException {
		IFile file = fProject.getFile(path.append(className + ".java")); //$NON-NLS-1$
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		if (fPluginData.getOSGiFramework() != null) {
			generateComponentClass(packageName, className, writer);
		}
		writer.flush();
		try {
			swriter.close();
			ByteArrayInputStream stream = new ByteArrayInputStream(swriter.toString().getBytes(fProject.getDefaultCharset()));
			if (file.exists())
				file.setContents(stream, false, true, monitor);
			else
				file.create(stream, false, monitor);
			stream.close();
		} catch (IOException e) {

		}
	}

	/**
	 * Generates the Component java file.
	 * 
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param writer
	 *            The writer that writes the String-file
	 */
	private void generateComponentClass(String packageName, String className, PrintWriter writer) {
		if (!packageName.equals("")) { //$NON-NLS-1$
			writer.println("package " + packageName + ";");
			writer.println();
		}

		writer.println("import java.util.Dictionary;");
		writer.println("import java.util.HashMap;");
		writer.println("import java.util.Hashtable;");
		writer.println();
		writer.println("import org.eclipse.swt.widgets.Composite;");
		writer.println("import org.osgi.framework.Constants;");
		writer.println("import org.osgi.framework.ServiceRegistration;");
		writer.println("import org.osgi.service.event.EventConstants;");
		writer.println("import org.osgi.service.event.EventHandler;");
		writer.println("import org.osgi.service.wireadmin.Consumer;");
		writer.println("import org.osgi.service.wireadmin.Producer;");
		writer.println("import org.osgi.service.wireadmin.WireConstants;");
		writer.println();
		writer.println("import at.component.AbstractComponent;");
		writer.println();
		writer.println("public class " + className + " extends AbstractComponent {");
		writer.println();
		writer.println("\tprivate ComponentUiController uiController;");
		writer.println("\tprivate ServiceRegistration serviceRegistration;");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void addUI(Composite composite) {");
		writer.println("\t\tuiController = new ComponentUiController(composite, this);");
		writer.println("\t\t");

		writer.println("\t\tDictionary<String, Object> properties = new Hashtable<String, Object>();");
		writer.println("\t\tproperties.put(Constants.SERVICE_PID, Activator.getComponentWireAdmin().getPersistentIdOfComponent(this));");
		writer.println("\t\tproperties.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS, new Class[] { Object.class });");
		writer.println("\t\tproperties.put(EventConstants.EVENT_TOPIC, \"at/component/event/*\");");
		writer.println("\t\t");
		writer.println("\t\tserviceRegistration = Activator.getBundleContext()");
		writer
				.println("\t\t\t.registerService(new String[] { Consumer.class.getName(), Producer.class.getName(), EventHandler.class.getName() },");
		writer.println("\t\t\t\tuiController, properties);");

		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic String getName() {");
		writer.println("\t\tif (name == null) {");
		writer.println("\t\t\tObject bundleName = Activator.getBundleContext().getBundle().getHeaders().get(Constants.BUNDLE_NAME);");
		writer.println("\t\t\t");
		writer.println("\t\t\tif (bundleName != null && !bundleName.toString().trim().equals(\"\"))");
		writer.println("\t\t\t\tname = bundleName.toString().trim();");
		writer.println("\t\t\telse");
		writer.println("\t\t\t\tname = Activator.getBundleContext().getBundle().getSymbolicName();");
		writer.println("\t\t}");
		writer.println("\t\t");
		writer.println("\t\treturn name;");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic HashMap<String, String> getData() {");
		writer.println("\t\tif (uiController != null)");
		writer.println("\t\t\treturn uiController.getData();");
		writer.println("\t\t");
		writer.println("\t\treturn null;");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void initialize(HashMap<String, String> data) {");
		writer.println("\t\tif (uiController != null)");
		writer.println("\t\t\tuiController.initialize(data);");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void unregister() {");
		writer.println("\t\tif (serviceRegistration != null)");
		writer.println("\t\t\tserviceRegistration.unregister();");
		writer.println("\t\tsuper.unregister();");
		writer.println("\t}");
		writer.println("}");
	}

	/**
	 * @param monitor
	 *            The ProgressMonitor for displaying the progress in a progressbar
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param path
	 *            The filePath
	 * @throws CoreException
	 *             if there occur problems while writing the files to the file-system
	 */
	private void generateComponentStarterService(IProgressMonitor monitor, String packageName, String className, IPath path)
			throws CoreException {
		IFile file = fProject.getFile(path.append(className + ".java")); //$NON-NLS-1$
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		if (fPluginData.getOSGiFramework() != null) {
			generateComponentStarterServiceClass(packageName, className, writer);
		}
		writer.flush();
		try {
			swriter.close();
			ByteArrayInputStream stream = new ByteArrayInputStream(swriter.toString().getBytes(fProject.getDefaultCharset()));
			if (file.exists())
				file.setContents(stream, false, true, monitor);
			else
				file.create(stream, false, monitor);
			stream.close();
		} catch (IOException e) {

		}
	}

	/**
	 * Generates the ComponentStarterService java file.
	 * 
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param writer
	 *            The writer that writes the String-file
	 */
	private void generateComponentStarterServiceClass(String packageName, String className, PrintWriter writer) {
		if (!packageName.equals("")) { //$NON-NLS-1$
			writer.println("package " + packageName + ";");
			writer.println();
		}

		writer.println("import org.osgi.framework.ServiceRegistration;");
		writer.println();
		writer.println("import at.component.IComponent;");
		writer.println("import at.component.IComponentStarterService;");
		writer.println();
		writer.println("public class " + className + " implements IComponentStarterService {");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic IComponent startNewComponentInstance() {");
		writer.println("\t\tComponent component = new Component();");
		writer.println("\t\t");
		writer
				.println("\t\tServiceRegistration componentRegistration = Activator.getBundleContext().registerService(IComponent.class.getName(), component, null);");
		writer.println("\t\tcomponent.setComponentRegistration(componentRegistration);");
		writer.println("\t\t");
		writer.println("\t\treturn (IComponent) Activator.getBundleContext().getService(componentRegistration.getReference());");
		writer.println("\t}");
		writer.println("}");
	}

	/**
	 * @param monitor
	 *            The ProgressMonitor for displaying the progress in a progressbar
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param path
	 *            The filePath
	 * @throws CoreException
	 *             if there occur problems while writing the files to the file-system
	 */
	private void generateComponentUiController(IProgressMonitor monitor, String packageName, String className, IPath path)
			throws CoreException {
		IFile file = fProject.getFile(path.append(className + ".java")); //$NON-NLS-1$
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		if (fPluginData.getOSGiFramework() != null) {
			generateComponentUiControllerClass(packageName, className, writer);
		}
		writer.flush();
		try {
			swriter.close();
			ByteArrayInputStream stream = new ByteArrayInputStream(swriter.toString().getBytes(fProject.getDefaultCharset()));
			if (file.exists())
				file.setContents(stream, false, true, monitor);
			else
				file.create(stream, false, monitor);
			stream.close();
		} catch (IOException e) {

		}
	}

	/**
	 * Generates the ComponentUiController java file.
	 * 
	 * @param packageName
	 *            The name of the package where the file should be stored
	 * @param className
	 *            The name of the class
	 * @param writer
	 *            The writer that writes the String-file
	 */
	private void generateComponentUiControllerClass(String packageName, String className, PrintWriter writer) {
		if (!packageName.equals("")) { //$NON-NLS-1$
			writer.println("package " + packageName + ";");
			writer.println();
		}

		writer.println("import java.util.HashMap;");
		writer.println();
		writer.println("import org.eclipse.swt.widgets.Composite;");
		writer.println("import org.osgi.service.event.Event;");
		writer.println("import org.osgi.service.wireadmin.Wire;");
		writer.println();
		writer.println("import at.component.IComponent;");
		writer.println("import at.component.IComponentUiController;");
		writer.println();
		writer.println("public class " + className + " implements IComponentUiController {");
		writer.println();
		writer.println("\tpublic ComponentUiController(Composite parent, IComponent component) {");
		writer.println("\t\t// TODO: REQUIRED Create Component UI");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void consumersConnected(Wire[] wires) {");
		writer.println("\t\t// TODO: OPTIONAL Implement this method if you want to send data to another component");
		writer.println("\t\t// Knowledge about the OSGi WireAdmin Service is required");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic HashMap<String, String> getData() {");
		writer.println("\t\t// TODO: OPTIONAL Return the data which is needed to initialize the component at start-up");
		writer.println("\t\treturn null;");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void handleEvent(Event event) {");
		writer.println("\t\t// TODO: OPTIONAL Implement this method if you want to handle events");
		writer.println("\t\t// Knowledge about the OSGi EventAdmin Service is required");
		writer.println("\t}");
		writer.println();
		writer.println("\tpublic void initialize(HashMap<String, String> data) {");
		writer
				.println("\t\t// TODO: OPTIONAL Initialize the component with the given data (that was returned by the \"getData()\"-method)");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic Object polled(Wire wire) {");
		writer.println("\t\t// TODO: OPTIONAL Implement this method if you want to send data to another component");
		writer.println("\t\t// Knowledge about the OSGi WireAdmin Service is required");
		writer.println("\t\treturn null;");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void producersConnected(Wire[] wires) {");
		writer.println("\t\t// TODO: OPTIONAL Implement this method if you want to receive data from data producers");
		writer.println("\t\t// Knowledge about the OSGi WireAdmin Service is required");
		writer.println("\t}");
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void updated(Wire wire, Object value) {");
		writer.println("\t\t// TODO: OPTIONAL Implement this method if you want to receive data from data producers");
		writer.println("\t\t// Knowledge about the OSGi WireAdmin Service is required");
		writer.println("\t}");
		writer.println("}");
	}

	/**
	 * Returns the necessary Plug-In Dependencies
	 * 
	 * @return All necessary Plug-In Dependencies
	 */
	public IPluginReference[] getDependencies() {
		ArrayList<PluginReference> result = new ArrayList<PluginReference>();
		if (fPluginData.isUIPlugin())
			result.add(new PluginReference("org.eclipse.ui", null, 0)); //$NON-NLS-1$
		if (!fPluginData.isLegacy() && fPluginData.getOSGiFramework() == null)
			result.add(new PluginReference(IPDEBuildConstants.BUNDLE_CORE_RUNTIME, null, 0));
		result.add(new PluginReference("org.eclipse.swt", null, 0));
		return (IPluginReference[]) result.toArray(new IPluginReference[result.size()]);
	}

	/**
	 * Returns all necessary packages
	 * 
	 * @return All necessary packages
	 */
	public String[] getImportPackages() {
		return fPluginData.getOSGiFramework() != null ? new String[] {
				"org.osgi.framework;version=\"1.3.0\"", "at.component;version=\"1.0.0\"", "org.osgi.service.event;version=\"1.2.0\"", "org.osgi.service.wireadmin", "org.osgi.util.tracker", "at.component.framework.services.componentwireadmin", "at.component.framework.services.log" } //$NON-NLS-1$
				: new String[0];
	}

}
