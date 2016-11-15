package org.cheetahplatform.modeler.experiment.editor.model;

import java.util.List;

import org.cheetahplatform.modeler.experiment.editor.xml.ExperimentEditorMarshaller;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.EditPart;

public class ExperimentGraph extends Graph {

	public static Graph copyGraph(Graph graph) {
		ExperimentEditorMarshaller eem = new ExperimentEditorMarshaller();
		return (Graph) eem.unmarshall(eem.marshall(graph));
	}

	private String process;
	private String email;
	private String url;
	private String user;
	private String password;

	private boolean startModelingDialogShown;

	public ExperimentGraph(List<IGraphElementDescriptor> descriptors) {
		super(descriptors);
		process = "";
		email = "";
		url = "";
		user = "";
		password = "";
		startModelingDialogShown = false;
	}

	@Override
	public EditPart createEditPart(EditPart context) {
		return ExperimentEditorMarshaller.getEditPartFactory().createEditPart(context, this);
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getProcess() {
		return process;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public boolean isStartModelingDialogShown() {
		return startModelingDialogShown;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public void setStartModelingDialogShown(boolean startModelignDialogShown) {
		this.startModelingDialogShown = startModelignDialogShown;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
