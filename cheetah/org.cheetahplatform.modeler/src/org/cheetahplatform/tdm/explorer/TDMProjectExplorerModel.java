/*******************************************************************************
 * This file is provided under the terms of the Eclipse Public License 1.0. 
 * Any use, reproduction or distribution of the program
 * constitutes recipient's acceptance of this agreement.
 *******************************************************************************/
package org.cheetahplatform.tdm.explorer;

import org.cheetahplatform.common.INamed;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromLogger;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.swtdesigner.ResourceManager;

public class TDMProjectExplorerModel {

	private static class TDMProjectExplorerContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof TDMProcess) {
				return ((TDMProcess) parentElement).getTests().toArray();
			}

			return new Object[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof TDMProcess) {
				return ((TDMProcess) element).hasTests();
			}

			return false;
		}

	}

	private static class TDMProjectExplorerLabelProvider extends LabelProvider {
		@Override
		public Image getImage(Object element) {
			if (element instanceof TDMProcess) {
				if (((TDMProcess) element).allTestsPass()) {
					return ResourceManager.getPluginImage(Activator.getDefault(), "img/tdm/process_passed.gif");
				}

				return ResourceManager.getPluginImage(Activator.getDefault(), "img/tdm/process_failed.gif");
			} else if (element instanceof TDMTest) {
				if (((TDMTest) element).passes()) {
					return ResourceManager.getPluginImage(Activator.getDefault(), "img/tdm/test_passed.gif");
				}

				return ResourceManager.getPluginImage(Activator.getDefault(), "img/tdm/test_failed.gif");
			}

			return null;
		}

		@Override
		public String getText(Object element) {
			return ((INamed) element).getName();
		}
	}

	private TDMProcess process;
	private IPromLogger logger;

	public TDMTest addTest(String name, long id) {
		TDMTest test = process.addTest(name, id);
		logTestAdded(test);

		return test;
	}

	public void addTest(TDMTest test) {
		process.add(test);
		logTestAdded(test);
	}

	public IContentProvider createContentProvider() {
		return new TDMProjectExplorerContentProvider();
	}

	public LabelProvider createLabelProvider() {
		return new TDMProjectExplorerLabelProvider();
	}

	public TDMProcess getInput() {
		return process;
	}

	public IPromLogger getLogger() {
		return logger;
	}

	public TDMTest getTest(long id) {
		return process.getTest(id);
	}

	public void log(AuditTrailEntry entry) {
		logger.log(entry);
	}

	protected void logTestAdded(TDMTest test) {
		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_CREATE_TEST);
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, test.getName());
		entry.setAttribute(AbstractGraphCommand.ID, test.getCheetahId());
		logger.log(entry);
	}

	public void removeTest(TDMTest toRemove) {
		AuditTrailEntry entry = new AuditTrailEntry(TDMCommand.COMMAND_REMOVE_TEST);
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_ID, toRemove.getCheetahId());
		entry.setAttribute(TDMCommand.ATTRIBUTE_TEST_NAME, toRemove.getName());
		logger.log(entry);

		process.removeTest(toRemove);
	}

	public void setLogger(IPromLogger logger) {
		this.logger = logger;

	}

	public void setProcess(TDMProcess process) {
		this.process = process;
	}

}
