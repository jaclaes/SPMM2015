package org.cheetahplatform.testarossa;

import com.swtdesigner.SWTResourceManager;
import org.cheetahplatform.testarossa.action.SelectProcessInstanceAction;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void postWindowCreate() {
		super.postWindowCreate();

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		shell.setMaximized(true);

		// paint a line above the editor area
		StackLayout layout = (StackLayout) ((Composite) shell.getChildren()[3]).getLayout();
		layout.topControl.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(SWTResourceManager.getColor(180, 180, 180));
				e.gc.drawLine(0, 0, 100000, 0);
			}
		});
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();

		new SelectProcessInstanceAction().run();
		// try {
		// WeekView view = (WeekView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(WeekView.ID);
		// DeclarativeProcessSchema process = PersistentModel.getInstance().getProcesses().get(0);
		// List<DeclarativeProcessInstance> instances = PersistentModel.getInstance().getInstances(process);
		// TestaRossaModel.getInstance().setCurrentInstance(instances.get(0));
		// view.refresh();
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }

		new ReminderJob().schedule();
		new TimeJob().schedule();
	}

	@Override
	public void preWindowOpen() {
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(true);
		configurer.setTitle("Process Support for Testa Rossa by Cheetah");
	}
}
