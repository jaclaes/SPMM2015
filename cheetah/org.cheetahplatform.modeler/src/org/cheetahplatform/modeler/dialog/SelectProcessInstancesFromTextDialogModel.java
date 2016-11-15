package org.cheetahplatform.modeler.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.ui.dialog.DefaultExtraColumnProvider;
import org.cheetahplatform.common.ui.dialog.ExperimentalWorkflowElementDatabaseHandle;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.engine.ProcessRepository;

public class SelectProcessInstancesFromTextDialogModel {
	private static class ExperimentalProcessIdSelector implements IProcessInstanceSelector {

		@Override
		public List<ProcessInstanceDatabaseHandle> selectFrom(List<ProcessInstanceDatabaseHandle> allHandles, List<String> ids) {
			List<ProcessInstanceDatabaseHandle> selected = new ArrayList<ProcessInstanceDatabaseHandle>();
			for (ProcessInstanceDatabaseHandle handle : allHandles) {
				if (ids.contains(handle.getId())) {
					selected.add(handle);
				}
			}

			return selected;
		}

	}

	private static class ExperimentalProcessInstanceSelector implements IProcessInstanceSelector {

		@Override
		public List<ProcessInstanceDatabaseHandle> selectFrom(List<ProcessInstanceDatabaseHandle> allHandles, List<String> ids) {
			List<ProcessInstanceDatabaseHandle> selected = new ArrayList<ProcessInstanceDatabaseHandle>();
			for (String id : ids) {
				for (ProcessInstanceDatabaseHandle handle : allHandles) {
					if (handle.hasChildWithId(id)) {
						selected.add(handle);
					}
				}
			}

			return selected;
		}

	}

	private interface IProcessInstanceSelector {
		/**
		 * Select from a list of handles.
		 * 
		 * @param allHandles
		 *            the handles
		 * @param ids
		 *            the ids
		 * @return the selected ids - *must* be in the same order as the given ids
		 */
		List<ProcessInstanceDatabaseHandle> selectFrom(List<ProcessInstanceDatabaseHandle> allHandles, List<String> ids);
	}

	private static class ProcessInstanceByIdSelector implements IProcessInstanceSelector {

		@Override
		public List<ProcessInstanceDatabaseHandle> selectFrom(List<ProcessInstanceDatabaseHandle> allHandles, List<String> ids) {
			List<ProcessInstanceDatabaseHandle> selected = new ArrayList<ProcessInstanceDatabaseHandle>();
			for (String id : ids) {
				for (ProcessInstanceDatabaseHandle handle : allHandles) {
					if (handle.getId().equals(id)) {
						selected.add(handle);
					}
				}
			}

			return selected;
		}
	}

	private static final String[] SEPARATORS = new String[] { ";", " ", "\r\n", "\n" };

	public List<ProcessInstanceDatabaseHandle> loadExperimentalProcessInstances(String toParse) {
		return loadProcessInstances(toParse, new ExperimentalProcessIdSelector());
	}

	public List<ProcessInstanceDatabaseHandle> loadExperimentalProcessInstancesByChildIds(String toParse) {
		return loadProcessInstances(toParse, new ExperimentalProcessInstanceSelector());
	}

	private List<ProcessInstanceDatabaseHandle> loadProcessInstances(String toParse, IProcessInstanceSelector selector) {
		Set<String> parsed = new LinkedHashSet<String>();
		parsed.add(toParse);

		for (String separator : SEPARATORS) {
			Set<String> newParsed = new LinkedHashSet<String>();

			for (String current : parsed) {
				String[] splitted = current.split(separator);
				newParsed.addAll(Arrays.asList(splitted));
			}

			parsed = newParsed;
		}

		Set<String> trimmed = new LinkedHashSet<String>();
		for (String current : parsed) {
			String parsedAndTrimmed = current.trim();
			if (!parsedAndTrimmed.isEmpty()) {
				trimmed.add(parsedAndTrimmed);
			}
		}

		SelectProcessInstanceModel model = new SelectProcessInstanceModel(new DefaultExtraColumnProvider());
		for (Process process : ProcessRepository.getExperimentalProcesses()) {
			model.addIncludedProcess(process.getId());
		}

		List<ProcessInstanceDatabaseHandle> allHandles = new ArrayList<ProcessInstanceDatabaseHandle>();
		for (ProcessInstanceDatabaseHandle handle : model.loadAllProcessInstances()) {
			List<ExperimentalWorkflowElementDatabaseHandle> children = model.loadChildren(handle);
			allHandles.add(handle);

			for (ExperimentalWorkflowElementDatabaseHandle child : children) {
				if (child.isAttributeDefined(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE)) {
					allHandles.add(new ProcessInstanceDatabaseHandle(child));
					handle.addChild(child);
				}
			}
		}

		model.dispose();
		return selector.selectFrom(allHandles, new ArrayList<String>(trimmed));
	}

	public List<ProcessInstanceDatabaseHandle> loadProcessInstancesById(String toParse) {
		return loadProcessInstances(toParse, new ProcessInstanceByIdSelector());
	}
}
