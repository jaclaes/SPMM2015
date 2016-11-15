package org.cheetahplatform.modeler.graph.command;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.graph.model.Graph;

public class UnsuccesfulLayoutCommand extends AbstractGraphCommand {

	private final Exception cause;

	public UnsuccesfulLayoutCommand(Graph graph, Exception cause) {
		super(graph, null);

		this.cause = cause;
	}

	@Override
	public void execute() {
		String workflowModelElement = getAffectedElementName();
		AuditTrailEntry entry = new AuditTrailEntry(AbstractGraphCommand.UNSUCCESSFUL_LAYOUT, workflowModelElement);
		entry.setAttribute(NAME, workflowModelElement);

		if (cause != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(out);
			cause.printStackTrace(writer);
			writer.flush();

			entry.setAttribute(ModelerConstants.ATTRIBUTE_UNSUCCESFUL_LAYOUT_CAUSE, new String(out.toByteArray()));
		}

		log(entry);
	}

	@Override
	protected String getAffectedElementName() {
		return "Model";
	}

}
