package org.cheetahplatform.modeler.action;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.graph.export.AbstractExporter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class ExportRunnable implements IRunnableWithProgress {
	private final File target;
	private final List<ProcessInstanceDatabaseHandle> toExport;
	private final AbstractExporter exporter;

	public ExportRunnable(List<ProcessInstanceDatabaseHandle> toExport, File target, AbstractExporter exporter) {
		this.target = target;
		this.toExport = toExport;
		this.exporter = exporter;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			Iterator<ProcessInstanceDatabaseHandle> iterator = toExport.iterator();
			monitor.beginTask("Export", toExport.size());
			exporter.initializeExport(target);

			while (iterator.hasNext()) {
				ProcessInstanceDatabaseHandle current = iterator.next();
				exporter.export(current, monitor);
				monitor.worked(1);

				iterator.remove();
			}

			exporter.exportFinished();
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}

}