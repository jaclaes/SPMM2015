package org.cheetahplatform.modeler.engine;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.ModelerConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractExperimentsWorkflowActivity implements IExperimentalWorkflowActivity {

	private final String id;
	private long duration;
	private boolean finished;

	protected AbstractExperimentsWorkflowActivity(String id) {
		this.id = id;
		this.finished = true;
	}

	/**
	 * Carry out the work to be done.
	 * 
	 */
	protected abstract void doExecute();

	@Override
	public List<Attribute> execute() {
		preExecute();
		long startTime = System.currentTimeMillis();

		doExecute();
		runEventLoop();
		duration = System.currentTimeMillis() - startTime;

		postExecute();

		return getData();
	}

	/**
	 * Return the produced data.
	 * 
	 * @return the data
	 */
	protected List<Attribute> getData() {
		List<Attribute> data = new ArrayList<Attribute>();
		data.add(new Attribute(ModelerConstants.ATTRIBUTE_EXPERIMENTAL_WORKFLOW_ACTIVITY_DURATION, duration));

		return data;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * Determine whether this activity has been finished.
	 * 
	 * @return <code>true</code> if the activity has been finished, <code>false</code> otherwise
	 */
	protected boolean isFinished() {
		return finished;
	}

	protected void postExecute() {
		// nothing to do
	}

	protected void preExecute() {
		// nothing to do
	}

	@Override
	public List<Attribute> restart() {
		return execute();
	}

	protected void runEventLoop() {
		Display display = Display.getDefault();

		while (!isFinished()) {
			try {
				if (display.isDisposed() || PlatformUI.getWorkbench().isClosing()) {
					return;
				}

				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Throwable e) {
				Activator.logError("An error ocurred during the experimental workflow.", e);
			}
		}
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
