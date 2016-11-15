/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.testarossa.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.declarative.constraint.EarliestStartTimeConstraint;
import org.cheetahplatform.core.declarative.constraint.PrecedenceConstraint;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.modeling.Milestone;
import org.cheetahplatform.core.declarative.modeling.MilestoneActivityReminder;
import org.cheetahplatform.core.declarative.runtime.DeclarativeActivityInstance;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.tdm.MessageLookup;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.tdm.daily.model.Workspace;
import org.cheetahplatform.tdm.modeler.test.model.WeeklyActivity;
import org.cheetahplatform.tdm.weekly.RelativeBounds;
import org.cheetahplatform.testarossa.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import com.thoughtworks.xstream.XStream;

public class PersistentModel {
	private class PersistentLogListener implements ILogListener {

		public void log(AuditTrailEntry entry) {
			logEntries.add(entry);
		}

	}

	private List<AuditTrailEntry> logEntries;

	private static final String FILE_NAME = "model.xml";
	private static final String PROJECT_NAME = "org.cheetahplatform.testarossa";

	private static IFile getModelFile(String fileName) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(PROJECT_NAME);
		if (!project.exists()) {
			project.create(null);
		}

		if (!project.isOpen()) {
			project.open(null);
		}

		return project.getFile(new Path(fileName));
	}

	private static XStream getXStream() {
		XStream xStream = new XStream();
		xStream.setClassLoader(Activator.class.getClassLoader());
		return xStream;
	}

	public static void save() {
		PersistentModel model = getInstance();
		model.updatePersistentModels();

		try {
			if (!Platform.isRunning()) {
				getXStream().toXML(model, new FileOutputStream(new File(FILE_NAME)));
			} else {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				getXStream().toXML(model, out);

				IFile file = getModelFile(FILE_NAME);
				if (!file.exists()) {
					file.create(new ByteArrayInputStream(out.toByteArray()), true, null);
				} else {
					if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
						file.refreshLocal(IResource.DEPTH_ZERO, null);
					}
					file.setContents(new ByteArrayInputStream(out.toByteArray()), IResource.NONE, null);
				}

				IFile backupFile = getModelFile(FILE_NAME + System.currentTimeMillis() + ".zip");
				ByteArrayOutputStream zipped = new ByteArrayOutputStream();
				ZipOutputStream zipStream = new ZipOutputStream(zipped);
				zipStream.putNextEntry(new ZipEntry(FILE_NAME));
				getXStream().toXML(model, zipStream);
				zipStream.close();
				backupFile.create(new ByteArrayInputStream(zipped.toByteArray()), IResource.NONE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long highestId;
	private Set<DeclarativeProcessSchema> schemata;
	private Set<DeclarativeProcessInstance> instances;
	private Set<PersistentWorkspace> persistentUiModels;
	private transient Set<Workspace> uiModels;
	private Map<DeclarativeActivity, Role> activityToRole;
	private Map<DeclarativeProcessInstance, Map<DeclarativeActivity, Role>> roleOverrides;
	private Map<DeclarativeActivity, RelativeBounds> initialBounds;
	private Map<DeclarativeActivity, Integer> weekOffset;
	private Set<Role> roles;
	private Map<DeclarativeActivityInstance, String> messages;

	private static PersistentModel MODEL;

	static PersistentModel createTestaRossaModel() {
		return new PersistentTestaRossaModel().create();
	}

	static PersistentModel createTestData() {
		PersistentModel model = new PersistentModel();

		DeclarativeProcessSchema schema = new DeclarativeProcessSchema("Testa Rossa 1.0");
		DeclarativeActivity buy = schema.createActivity("Buy Coffee");
		DeclarativeActivity brew = schema.createActivity("Brew Coffee");
		DeclarativeActivity drink = schema.createActivity("Drink Coffee");
		DeclarativeActivity chat = schema.createActivity("Chat");

		Milestone milestone = new Milestone("Milestone Week 1", new Duration(7, 0, 0));
		schema.addMilestone(milestone);
		milestone.addActivity(buy);
		milestone.addActivity(brew);
		milestone.addActivity(drink);

		Milestone milestone2 = new Milestone("Milestone Week 5", new Duration(7 * 5, 0, 0));
		schema.addMilestone(milestone2);
		milestone2.addActivity(chat);
		//
		// // start time constraints
		schema.addConstraint(new EarliestStartTimeConstraint(buy, new Duration(0, 0)));
		schema.addConstraint(new EarliestStartTimeConstraint(brew, new Duration(0, 0)));
		schema.addConstraint(new EarliestStartTimeConstraint(drink, new Duration(8, 0, 0)));
		schema.addConstraint(new EarliestStartTimeConstraint(chat, new Duration(0, 0)));
		//
		// // precedence constraints
		schema.addConstraint(new PrecedenceConstraint(buy, brew));
		// schema.addConstraint(new PrerequisiteConstraint(brew, drink));
		//
		MilestoneActivityReminder reminder1 = new MilestoneActivityReminder(buy, milestone, "Buy Coffee Reminder",
				"Man, u gotta buy some coffee before the milestone!!!!", new Duration(3, 0, 0));
		MilestoneActivityReminder reminder2 = new MilestoneActivityReminder(brew, milestone, "Brew the coffee",
				"The coffee has to be brewed to get it ready in time!", new Duration(2, 0, 0));
		MilestoneActivityReminder reminder3 = new MilestoneActivityReminder(drink, milestone, "Drink it baby!",
				"There should always be enough time for having a delicious cup of coffee! So better hurry!", new Duration(1, 0, 0));
		//
		schema.addReminder(reminder1);
		schema.addReminder(reminder2);
		schema.addReminder(reminder3);

		model.addSchema(schema);
		DeclarativeProcessInstance instance = schema.instantiate();
		instance.setName("Innsbruck");
		model.addInstance(instance);

		Workspace workspace = new Workspace(instance);
		model.addWorkspace(workspace);

		return model;
	}

	public static synchronized PersistentModel getInstance() {
		if (MODEL != null) {
			return MODEL;
		}

		InputStream in = null;
		try {
			if (!Platform.isRunning()) {
				in = new FileInputStream(new File(FILE_NAME));
			} else {
				IFile file = getModelFile(FILE_NAME);
				if (!file.exists()) {
					throw new Exception();
				}

				file.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
				in = file.getContents();
			}
		} catch (Exception e) {
			MODEL = createTestaRossaModel();
			MODEL.initialize();
			return MODEL;
		}

		MODEL = (PersistentModel) getXStream().fromXML(in);
		try {
			in.close();
		} catch (IOException e) {
			// ignore
		}

		Services.getIdGenerator().setMinimalId(MODEL.highestId);
		MODEL.initialize();
		return MODEL;
	}

	private transient PersistentLogListener logListener;

	public PersistentModel() {
		schemata = new HashSet<DeclarativeProcessSchema>();
		instances = new HashSet<DeclarativeProcessInstance>();
		persistentUiModels = new HashSet<PersistentWorkspace>();
		uiModels = new HashSet<Workspace>();
		activityToRole = new HashMap<DeclarativeActivity, Role>();
		initialBounds = new HashMap<DeclarativeActivity, RelativeBounds>();
		weekOffset = new HashMap<DeclarativeActivity, Integer>();
		logListener = new PersistentLogListener();
		logEntries = new ArrayList<AuditTrailEntry>();
		roles = new HashSet<Role>();
		roleOverrides = new HashMap<DeclarativeProcessInstance, Map<DeclarativeActivity, Role>>();
		messages = new HashMap<DeclarativeActivityInstance, String>();

		initialize();
	}

	public void addInstance(DeclarativeProcessInstance instance) {
		instances.add(instance);
	}

	public void addSchema(DeclarativeProcessSchema schema) {
		schemata.add(schema);
	}

	public void addWorkspace(Workspace workspace) {
		uiModels.add(workspace);
	}

	public void associateRole(DeclarativeActivity activity, Role role) {
		activityToRole.put(activity, role);
		roles.add(role);
	}

	public RelativeBounds getInitialBounds(DeclarativeActivity activity) {
		return initialBounds.get(activity);
	}

	public DeclarativeProcessInstance getInstanceByCheetahId(long id) {
		for (DeclarativeProcessInstance instance : instances) {
			if (instance.getCheetahId() == id) {
				return instance;
			}
		}

		return null;
	}

	public List<DeclarativeProcessInstance> getInstances(DeclarativeProcessSchema schema) {
		List<DeclarativeProcessInstance> matched = new ArrayList<DeclarativeProcessInstance>();

		for (DeclarativeProcessInstance instance : instances) {
			if (instance.getSchema().equals(schema)) {
				matched.add(instance);
			}
		}

		return matched;
	}

	public List<DeclarativeProcessSchema> getProcesses() {
		return new ArrayList<DeclarativeProcessSchema>(schemata);
	}

	/**
	 * Get the workspace for the given instance, create a new one, if there is no corresponding workspace.
	 * 
	 * @param instance
	 *            the instance
	 * @return the corresponding workspace
	 */
	public Workspace getWorkspace(DeclarativeProcessInstance instance) {
		for (Workspace workspace : uiModels) {
			if (workspace.getProcessInstance().equals(instance)) {
				return workspace;
			}
		}

		Workspace workspace = new Workspace(instance);
		uiModels.add(workspace);
		initialize(workspace);

		return workspace;
	}

	public void initialize() {
		uiModels = new HashSet<Workspace>();
		logListener = new PersistentLogListener();

		for (Map.Entry<DeclarativeActivity, Role> entry : activityToRole.entrySet()) {
			RoleLookup.getInstance().assignRole(entry.getKey(), entry.getValue());
		}

		for (PersistentWorkspace persistent : persistentUiModels) {
			Workspace workspace = persistent.toWorkspace();
			uiModels.add(workspace);
			workspace.getProcessInstance().addLogListener(logListener);
		}

		for (Role role : roles) {
			RoleLookup.getInstance().addRole(role);
		}

		for (Map.Entry<DeclarativeProcessInstance, Map<DeclarativeActivity, Role>> entry : roleOverrides.entrySet()) {
			for (Map.Entry<DeclarativeActivity, Role> mapping : entry.getValue().entrySet()) {
				RoleLookup.getInstance().assignRole(mapping.getKey(), mapping.getValue());
			}
		}

		for (Map.Entry<DeclarativeActivityInstance, String> message : messages.entrySet()) {
			MessageLookup.getInstance().assignMessage(message.getKey(), message.getValue());
		}
	}

	private void initialize(Workspace workspace) {
		for (INode node : workspace.getProcessInstance().getSchema().getNodes()) {
			RelativeBounds bounds = getInitialBounds((DeclarativeActivity) node);
			if (bounds == null) {
				continue;
			}

			WeeklyActivity activity = workspace.getWeekly().getActivity((DeclarativeActivity) node);
			activity.setBounds(bounds);
			activity.setCustomLayout(true);
		}

		for (WeeklyActivity activity : workspace.getWeekly().getActivityInstances()) {
			if (!weekOffset.containsKey(activity.getActivity().getNode())) {
				continue;
			}

			Integer offset = weekOffset.get(activity.getActivity().getNode());
			DateTime startTime = activity.getStartTime();
			startTime.plus(new Duration(7 * offset, 0, 0));
			activity.setStartTime(startTime);
		}

		workspace.getProcessInstance().addLogListener(logListener);
	}

	public void setInitialBounds(DeclarativeActivity activity, RelativeBounds bounds) {
		initialBounds.put(activity, bounds);
	}

	public void setWeekOffset(DeclarativeActivity activity, int offset) {
		weekOffset.put(activity, offset);
	}

	private void updatePersistentModels() {
		persistentUiModels.clear();
		highestId = Services.getIdGenerator().generateId();

		for (Workspace workspace : uiModels) {
			persistentUiModels.add(new PersistentWorkspace(workspace));
			DeclarativeProcessInstance instance = workspace.getProcessInstance();
			schemata.add(instance.getSchema());
			addInstance(instance);
		}

		roles.clear();
		roles.addAll(RoleLookup.getInstance().getAllRoles());
		roleOverrides = RoleLookup.getInstance().getAllOverrides();
		messages = MessageLookup.getInstance().getAllMessages();
	}
}
