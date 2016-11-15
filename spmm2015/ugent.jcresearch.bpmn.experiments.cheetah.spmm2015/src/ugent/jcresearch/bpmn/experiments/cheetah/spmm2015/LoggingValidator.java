package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.export.OperationSpanIncrementalComputation;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class LoggingValidator implements ILogListener {
		private List<AuditTrailEntry> ateQueue = new ArrayList<AuditTrailEntry>();
		private OperationSpanIncrementalComputation comp = new OperationSpanIncrementalComputation();
		private AuditTrailEntry prevSizeAte = null;
		private PromLogger logger;
		
		public LoggingValidator(Process process, String processtype) {
			logger = new PromLogger();
			ProcessInstance instance = new ProcessInstance();
			String instanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
			instance.setId(instanceId);
			instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, instanceId);
			instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, processtype);
			instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
			PromLogger.addHost(instance);
			try {
				logger.append(process, instance);
			} catch (Exception ex) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not initialize the log file.", ex);
				Activator.getDefault().getLog().log(status);
			}
		}

		public boolean isTestAbortable(AuditTrailEntry sizeAte) {
			boolean result = false;
			if (prevSizeAte != null) {
				comp.add(ateQueue.iterator());
				ateQueue.clear();

				ateQueue.add(sizeAte); // check if test can be aborted after level change.
				ateQueue.add(new AuditTrailEntry()); // add a dummy entry; after a size entry there must be another entry.
				comp.add(ateQueue.iterator());
				result = comp.isSummationAborted();
			}
			prevSizeAte = sizeAte;
			return result;
		}

		@Override
		public void log(AuditTrailEntry entry) {
			ateQueue.add(entry);
			logger.append(entry);
		}
	}
