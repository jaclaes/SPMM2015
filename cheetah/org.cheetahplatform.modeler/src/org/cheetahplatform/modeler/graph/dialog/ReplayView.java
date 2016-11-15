package org.cheetahplatform.modeler.graph.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.cheetahplatform.common.ui.composite.MultiLineButton;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.ICommandReplayerCallback;
import org.cheetahplatform.modeler.graph.ReplayModelProvider;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class ReplayView extends ViewPart {

	private class FirstAction extends Action {
		public FirstAction() {
			setText("first");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-first.png"));
		}

		@Override
		public void run() {
			try {
				isPlaying = false;
				model.gotoFirstStep();
				updateStepCounter();
			} catch (Exception ex) {
				MessageDialog.openError(getViewSite().getShell(), "Replay Failed", ex.getMessage());
				return;
			}
		}
	}

	private class JumpBackAction extends Action {
		public JumpBackAction() {
			setText("jump back");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-back.png"));
		}

		@Override
		public void run() {
			try {
				isPlaying = false;
				model.jumpBack();
				updateStepCounter();
			} catch (Exception ex) {
				MessageDialog.openError(getViewSite().getShell(), "Replay Failed", ex.getMessage());
				return;
			}
		}
	}

	private class JumpForwardAction extends Action {
		public JumpForwardAction() {
			setText("jump forward");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-forward.png"));
		}

		@Override
		public void run() {
			try {
				isPlaying = false;
				model.jumpForward();
				updateStepCounter();
			} catch (Exception ex) {
				MessageDialog.openError(getViewSite().getShell(), "Replay Failed", ex.getMessage());
				return;
			}
		}
	}

	private class LastAction extends Action {
		public LastAction() {
			setText("last");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-last.png"));
		}

		@Override
		public void run() {
			try {
				isPlaying = false;
				model.gotoLastStep();
				updateStepCounter();
			} catch (Exception ex) {
				MessageDialog.openError(getViewSite().getShell(), "Replay Failed", ex.getMessage());
				return;
			}
		}
	}

	private class NextAction extends Action {

		public NextAction() {
			setText("next");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-next.png"));
		}

		@Override
		public void run() {
			isPlaying = false;
			model.next();
			updateStepCounter();
		}
	}

	private class PauseAction extends Action {
		public PauseAction() {
			setText("pause");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-control-pause.png"));
		}

		@Override
		public void run() {
			isPlaying = false;
		}
	}

	private class PlayAction extends Action {
		public PlayAction() {
			setText("play");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-next.png"));
		}

		@Override
		public void run() {
			isPlaying = true;

			if (isPlaying && model.hasMoreCommands()) {
				model.next();
				updateStepCounter();
			}
		}
	}

	private class PlayTimer implements ICommandReplayerCallback {
		@Override
		public void processed(CommandDelegate command, boolean last) {
			if (isPlaying && model.hasMoreCommands()) {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								model.next();
								updateStepCounter();
							}
						});
						timer.cancel();
					};

				}, currentSpeed);
			} else {
				isPlaying = false;
			}
		}
	}

	private class PreviousAction extends Action {
		public PreviousAction() {
			setText("previous");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-arrow-previous.png"));
		}

		@Override
		public void run() {
			model.previous();
			updateStepCounter();
		}
	}

	private class StepsAction extends Action {
		public StepsAction() {
			setText("steps");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/24-columns.png"));
		}

		@Override
		public void run() {
			showStepsDialog();
		}
	}

	private class TranscriptsAction extends Action {
		public TranscriptsAction() {
			setText("transcript");
			setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "img/modelingTranscripts24x24.png"));
		}

		@Override
		public void run() {
			showTranscriptsDialog();
		}
	}

	private class UpdateRunnable implements ICommandReplayerCallback {
		@Override
		public void processed(CommandDelegate command, boolean last) {
			updateStepCounter();
		}
	}

	private static final int MAX_SPEED = 100; // 100 ms delay
	private static final int MIN_SPEED = 2000; // 2000 ms delay
	private static final int INITIAL_SPEED = 1000; // 2000 ms delay

	private int currentSpeed = INITIAL_SPEED;
	private boolean isPlaying = false;

	public static final String ID = "org.cheetahplatform.view.replayview";

	private ReplayModel model;
	// private IReplayAdvisor advisor;
	private Composite composite;

	private UpdateRunnable callBack;

	protected List<IAction> createActions() {
		List<IAction> actions = new ArrayList<IAction>();
		actions.add(new FirstAction());
		actions.add(new JumpBackAction());
		actions.add(new PreviousAction());
		actions.add(new NextAction());
		actions.add(new JumpForwardAction());
		actions.add(new LastAction());
		actions.add(new StepsAction());
		actions.add(new TranscriptsAction());
		actions.add(new PlayAction());
		actions.add(new PauseAction());

		return actions;
	}

	@Override
	public void createPartControl(Composite parent) {
		List<IAction> actions = createActions();
		composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(255, 255, 255));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		composite.setLayout(new GridLayout());

		initialize(actions);

		IStructuredSelection selection = (IStructuredSelection) ReplayModelProvider.getInstance().getSelection();
		this.model = (ReplayModel) ((IAdaptable) selection.getFirstElement()).getAdapter(ReplayModel.class);
		callBack = new UpdateRunnable();
		model.addCallbackListener(callBack);

		model.addCallbackListener(new PlayTimer());

		updateStepCounter();
	}

	@Override
	public void dispose() {
		model.removeCallbackListener(callBack);
		super.dispose();
	}

	private IWorkbenchWindow getWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	private void initialize(List<IAction> actions) {
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(8, true);
		buttonComposite.setLayout(layout);
		buttonComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		for (final IAction action : actions) {
			ImageDescriptor descriptor = action.getImageDescriptor();
			Image image = ResourceManager.getImage(descriptor);
			final MultiLineButton button = new MultiLineButton(buttonComposite, SWT.NONE, action.getText(), image);
			if (!action.isEnabled()) {
				button.setEnabled(false);
			}

			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.run();
				}
			});

			action.addPropertyChangeListener(new IPropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					if (event.getProperty().equals(IAction.ENABLED)) {
						button.setEnabled((Boolean) event.getNewValue());
					}
				}
			});
		}

		final Scale speedScale = new Scale(buttonComposite, SWT.HORIZONTAL);
		speedScale.setMaximum(MIN_SPEED);
		speedScale.setMinimum(MAX_SPEED);
		speedScale.setIncrement(50);
		speedScale.setSelection(INITIAL_SPEED);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		speedScale.setLayoutData(gridData);

		speedScale.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println(MIN_SPEED - speedScale.getSelection() + MAX_SPEED);
				currentSpeed = MIN_SPEED - speedScale.getSelection() + MAX_SPEED;
			}
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	protected void showStepsDialog() {
		try {
			getWorkbenchWindow().getActivePage().showView(StepsView.ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void showTranscriptsDialog() {
		try {
			getWorkbenchWindow().getActivePage().showView(ModelingTranscriptsView.ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void updateStepCounter() {
		// int currentStepIndex = model.getCurrentStepIndex();
		//
		// Shell replayDialogShell = getShell();
		// if (replayDialogShell == null || replayDialogShell.isDisposed()) {
		// return;
		// }
		//
		// if (currentStepIndex == -1) {
		// replayDialogShell.setText("No more steps to execute");
		// } else {
		// long durationToNextStep = model.getDurationToNextStep();
		// replayDialogShell.setText("Next step to be executed: " + currentStepIndex + " - pause to next step: " + durationToNextStep
		// + "ms");
		// }
	}
}
