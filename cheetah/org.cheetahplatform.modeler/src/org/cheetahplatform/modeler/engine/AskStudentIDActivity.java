package org.cheetahplatform.modeler.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.Messages;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AskStudentIDActivity extends AbstractExperimentsWorkflowActivity {

	private String studentID;

	public AskStudentIDActivity() {
		super("STUDENTID"); //$NON-NLS-1$
	}

	@Override
	protected void doExecute() {
		Shell shell = Display.getDefault().getActiveShell();
		String title = Messages.AskStudentIDActivity_3;
		String message = Messages.AskStudentIDActivity_4;
		InputDialog dialog = new InputDialog(shell, title, message, "", null);
		if (dialog.open() != Window.OK) {
			return;
		}
		studentID = dialog.getValue();
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-" + studentID);

		try {
			if (org.cheetahplatform.common.Activator.getDatabaseConnector() != null
					&& org.cheetahplatform.common.Activator.getDatabaseConnector().checkConnection()
					&& org.cheetahplatform.common.Activator.getDatabaseConnector().getDatabaseConnection() != null) {
				Connection connection = Activator.getDatabaseConnector().getDatabaseConnection();
				String sql = "insert into studentid (process_instance, student_id) values (?,?)";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, ExperimentalWorkflowEngine.getProcessInstanceId());
				statement.setString(2, studentID);
				statement.execute();
				statement.close();
			}
		} catch (Exception e) {
			Activator.logError("Unable to retrieve student id", e);
		}
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute("studentID", studentID)); //$NON-NLS-1$

		return data;
	}

	@Override
	public Object getName() {
		return Messages.AskStudentIDActivity_5;
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

}
