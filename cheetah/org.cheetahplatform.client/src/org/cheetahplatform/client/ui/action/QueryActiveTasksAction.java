package org.cheetahplatform.client.ui.action;

import javax.jms.JMSException;

import org.cheetahplatform.client.Activator;
import org.cheetahplatform.client.UiUtils;
import org.cheetahplatform.client.model.WorklistModel;
import org.eclipse.jface.action.Action;

import com.swtdesigner.ResourceManager;

/**
 * An Action responsible for querying all active activities from the server.
 * 
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         01.07.2009
 */
public class QueryActiveTasksAction extends Action {
	private final WorklistModel worklistModel;

	/**
	 * Creates a new action.
	 * 
	 * @param worklistModel
	 *            the {@link WorklistModel}
	 */
	public QueryActiveTasksAction(WorklistModel worklistModel) {
		super("Refresh", ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/refresh-16x16.gif"));
		this.worklistModel = worklistModel;
		setToolTipText("Refreshes the worklist");
	}

	@Override
	public void run() {
		try {
			worklistModel.queryActiveTasks();
		} catch (JMSException e) {
			UiUtils.showAndLogError("Could not query active tasks", e);
		}
	}
}