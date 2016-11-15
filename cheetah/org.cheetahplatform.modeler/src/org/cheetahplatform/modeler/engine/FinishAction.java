package org.cheetahplatform.modeler.engine;

import java.util.Locale;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import com.swtdesigner.ResourceManager;

public class FinishAction extends Action implements ILogListener {
	public static final String ID = "org.cheetahplatform.modeler.engine.FinishAction"; //$NON-NLS-1$

	protected final AbstractExperimentsWorkflowActivity activity;
	private Graph graph;

	public FinishAction(AbstractExperimentsWorkflowActivity activity, Graph graph, boolean optional) {
		this.activity = activity;
		this.graph = graph;

		setId(ID);
		setText(Messages.FinishAction_1);

		if (Locale.getDefault().equals(new Locale("es"))) {
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/finish_modeling_es.png")); //$NON-NLS-1$
		} else if (Locale.getDefault().equals(new Locale("de"))) {
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/finish_modeling_de.png")); //$NON-NLS-1$
		} else {
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/finish_modeling.png")); //$NON-NLS-1$
		}

		if (!optional) {
			setEnabled(false);
		}

		if (Activator.isTestEnvironment()) {
			setEnabled(true);
		}

		graph.addLogListener(this);
	}

	@Override
	public void log(AuditTrailEntry entry) {
		setEnabled(true);
		graph.removeLogListener(this);
	}

	@Override
	public void run() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (!MessageDialog.openQuestion(shell, Messages.FinishAction_3, Messages.FinishAction_4)) {
			return;
		}

		activity.setFinished(true);
		ICoolBarManager menuManager = ((WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow()).getCoolBarManager2();
		// menuManager.removeAll();
		menuManager.update(true);
	}
}
