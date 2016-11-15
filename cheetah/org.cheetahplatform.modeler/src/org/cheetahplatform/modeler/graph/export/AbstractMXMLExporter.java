package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.IPromWriter;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.xml.MemoryByteStorage;
import org.cheetahplatform.common.logging.xml.XMLPromWriter;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractMXMLExporter extends AbstractExporter {

	private IPromWriter writer;
	private MemoryByteStorage buffer;
	private File target;

	@Override
	protected void doExportAuditTrailEntry(AuditTrailEntry entry) {
		exportToMXML(writer, entry);
	}

	@Override
	protected void doExportExperimentalProcessInstance(ProcessInstanceDatabaseHandle instance) {
		exportHeaderToMXML(writer, instance.getInstance());
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		exportToMXML(writer, handle, entry);
	}

	@Override
	public void exportFinished() {
		writer.close();
		BufferedOutputStream out = null;

		try {
			out = new BufferedOutputStream(new FileOutputStream(target));
			out.write(buffer.getStorage());
			out.flush();
			out.close();
		} catch (Exception e) {
			Activator.logError("Could not write the output.", e);
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", "Could not write the file.");

			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					// ignore
				}
			}
		}
	}

	@SuppressWarnings("unused")
	protected void exportHeaderToMXML(IPromWriter writer, ProcessInstance instance) {
		// nothing to do
	}

	@SuppressWarnings("unused")
	protected void exportToMXML(IPromWriter writer, AuditTrailEntry entry) {
		// nothing to do
	}

	/**
	 * Carry out the export to MXML.
	 * 
	 * @param writer
	 *            the writer to be used
	 * @param handle
	 * @param entry
	 */
	protected abstract void exportToMXML(IPromWriter writer, ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry);

	@Override
	public void initializeExport(File target) {
		this.target = target;
		buffer = new MemoryByteStorage();
		writer = new XMLPromWriter(buffer);

	}

}
