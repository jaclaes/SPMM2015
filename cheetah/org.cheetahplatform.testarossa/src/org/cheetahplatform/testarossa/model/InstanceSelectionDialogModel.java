package org.cheetahplatform.testarossa.model;

import static org.cheetahplatform.testarossa.persistence.PersistentTestaRossaModel.FR_MITTERRUTZNER;
import static org.cheetahplatform.testarossa.persistence.PersistentTestaRossaModel.HR_PETZOLD;

import java.text.Collator;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.common.date.Date;
import org.cheetahplatform.common.date.DateTime;
import org.cheetahplatform.common.date.DateTimeProvider;
import org.cheetahplatform.common.date.Duration;
import org.cheetahplatform.core.common.modeling.INode;
import org.cheetahplatform.core.common.modeling.ProcessSchema;
import org.cheetahplatform.core.declarative.modeling.DeclarativeActivity;
import org.cheetahplatform.core.declarative.modeling.DeclarativeProcessSchema;
import org.cheetahplatform.core.declarative.runtime.DeclarativeProcessInstance;
import org.cheetahplatform.core.declarative.runtime.MilestoneInstance;
import com.swtdesigner.ResourceManager;
import org.cheetahplatform.tdm.Role;
import org.cheetahplatform.tdm.RoleLookup;
import org.cheetahplatform.testarossa.Activator;
import org.cheetahplatform.testarossa.persistence.PersistentModel;
import org.cheetahplatform.testarossa.persistence.PersistentTestaRossaModel;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;

public class InstanceSelectionDialogModel {
	private static class InstanceSelectionContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		private final boolean showInstances;

		public InstanceSelectionContentProvider(boolean showInstances) {
			this.showInstances = showInstances;
		}

		public Object[] getChildren(Object parentElement) {
			if (showInstances && parentElement instanceof DeclarativeProcessSchema) {
				List<DeclarativeProcessInstance> instances = PersistentModel.getInstance().getInstances(
						(DeclarativeProcessSchema) parentElement);
				return instances.toArray();
			}

			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return showInstances && element instanceof ProcessSchema
					&& (!PersistentModel.getInstance().getInstances((DeclarativeProcessSchema) element).isEmpty());
		}

	}

	private static class InstanceSelectionLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof DeclarativeProcessSchema) {
				return ResourceManager.getPluginImage(org.cheetahplatform.testarossa.Activator.getDefault(), "img/process.gif");
			}

			return ResourceManager.getPluginImage(Activator.getDefault(), "img/process_instance.gif");
		}

		@Override
		public String getText(Object element) {
			return ((INamed) element).getName();
		}
	}

	private static class NamedSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			INamed named1 = (INamed) e1;
			INamed named2 = (INamed) e2;

			return Collator.getInstance().compare(named1.getName(), named2.getName());
		}
	}

	private void adaptMilestones(DeclarativeProcessInstance newInstance, DateTime choiceOfLocationDate, DateTime openingDate) {
		Set<MilestoneInstance> milestones = newInstance.getMilestones();
		for (MilestoneInstance milestoneInstance : milestones) {
			if (milestoneInstance.getNode().getName().equals(PersistentTestaRossaModel.CHOICE_OF_LOCATION)) {
				milestoneInstance.setStartTime(choiceOfLocationDate);
			}
			if (milestoneInstance.getNode().getName().equals(PersistentTestaRossaModel.OPENING)) {
				milestoneInstance.setStartTime(openingDate);
			}
			if (milestoneInstance.getNode().getName().equals(PersistentTestaRossaModel.APPROVAL)) {
				DateTime approval = new DateTime(choiceOfLocationDate);
				approval.plus(new Duration(35, 0, 0));
				milestoneInstance.setStartTime(approval);
			}
			if (milestoneInstance.getNode().getName().equals(PersistentTestaRossaModel.OP_MANAGER)) {
				DateTime approval = new DateTime(choiceOfLocationDate);
				approval.plus(new Duration(21, 0, 0));
				milestoneInstance.setStartTime(approval);
			}
		}
	}

	private void adaptRoleOverrides(Role hrPetzoldRole, Role frMitterrutznerRole, DeclarativeProcessInstance newInstance) {
		RoleLookup lookup = RoleLookup.getInstance();
		Role originalHrPetzoldRole = lookup.getRole(FR_MITTERRUTZNER);
		Role originalFrMitterrutznerrole = lookup.getRole(HR_PETZOLD);

		for (INode node : newInstance.getSchema().getNodes()) {
			Role role = lookup.getRole((DeclarativeActivity) node);
			if (role == null) {
				continue;
			}

			boolean isFrMitterrutzner = role.equals(originalFrMitterrutznerrole);
			boolean isHrPetzold = role.equals(originalHrPetzoldRole);
			Assert.isTrue(isFrMitterrutzner || isHrPetzold, "Currently only two roles supported.");
			if (isHrPetzold) {
				lookup.assignRole(newInstance, (DeclarativeActivity) node, hrPetzoldRole);
			}
			if (isFrMitterrutzner) {
				lookup.assignRole(newInstance, (DeclarativeActivity) node, frMitterrutznerRole);
			}
		}
	}

	public IContentProvider createContentProvider(boolean showInstances) {
		return new InstanceSelectionContentProvider(showInstances);
	}

	public LabelProvider createLabelProvider() {
		return new InstanceSelectionLabelProvider();
	}

	public ViewerSorter createSorter() {
		return new NamedSorter();
	}

	public List<DeclarativeProcessSchema> getAllProcesses() {
		return PersistentModel.getInstance().getProcesses();
	}

	public ISelection getLastSelectedInstance() {
		long id = Activator.getDefault().getPreferenceStore().getLong(Activator.LAST_SELECTED_INSTANCE);

		if (id == 0) {
			PersistentModel model = PersistentModel.getInstance();
			for (DeclarativeProcessSchema schema : model.getProcesses()) {
				List<DeclarativeProcessInstance> instances = model.getInstances(schema);
				if (!instances.isEmpty()) {
					return new StructuredSelection(instances.get(0));
				}
			}

			return new StructuredSelection();
		}

		DeclarativeProcessInstance lastInstance = PersistentModel.getInstance().getInstanceByCheetahId(id);
		if (lastInstance == null) {
			return new StructuredSelection();
		}
		return new StructuredSelection(lastInstance);
	}

	public DeclarativeProcessInstance instantiateProcess(DeclarativeProcessSchema process, String name, Calendar choiceOfLocation,
			Calendar opening, Role hrPetzoldRole, Role frMitterrutznerRole) {
		DeclarativeProcessInstance newInstance = process.instantiate();
		newInstance.setStartTime(Date.weekStart(DateTimeProvider.getDateTimeSource().getCurrentTime(true)));
		DateTime startTime = newInstance.getStartTime();
		startTime.truncateToDate();
		DateTime choiceOfLocationDate = Date.weekEnd(Date.dateOnly(choiceOfLocation.getTime()));
		DateTime openingDate = Date.weekEnd(Date.dateOnly(opening.getTime()));

		newInstance.setName(name);
		adaptMilestones(newInstance, choiceOfLocationDate, openingDate);
		adaptRoleOverrides(hrPetzoldRole, frMitterrutznerRole, newInstance);

		PersistentModel.getInstance().addInstance(newInstance);
		return newInstance;
	}

	public void storeLastSelectedInstance(DeclarativeProcessInstance selection) {
		Activator.getDefault().getPreferenceStore().setValue(Activator.LAST_SELECTED_INSTANCE, selection.getCheetahId());
	}
}
