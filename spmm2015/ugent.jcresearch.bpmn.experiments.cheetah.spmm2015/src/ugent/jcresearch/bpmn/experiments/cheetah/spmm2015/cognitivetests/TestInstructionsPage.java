package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import java.util.*;

public class TestInstructionsPage extends WizardPage {

	private String instruction;
	private List<IFocusGainedListener> listeners;
	private int num, totalNum;

	public TestInstructionsPage(String pageName, String instruction, int num, int totalNum) {
		super(pageName);
		this.instruction = instruction;
		this.listeners = new ArrayList<IFocusGainedListener>();
		this.num = num;
		this.totalNum = totalNum;
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

		Label textLabel = new Label(control, SWT.WRAP);
		textLabel.setFont(new Font(Display.getCurrent(), "Verdana", 10, SWT.NONE));
		GridData labelLayoutData = new GridData(SWT.LEFT, SWT.FILL, false, false);
		labelLayoutData.widthHint = 500;
		textLabel.setLayoutData(labelLayoutData);
		textLabel.setText(instruction);
		setControl(control);

		if (totalNum != 0)
			setTitle("Instructions (" + num + "/" + totalNum + ")");
		else
			setTitle("Instructions");
		setDescription("Please read the next instructions carefully.");
	}

	public String getInstuctions() {
		return instruction;
	}
	
	public void addFocusGainedListener(IFocusGainedListener listener) {
		listeners.add(listener);
	}
	
	public void onFocusGained() {
		for (IFocusGainedListener listener : listeners)
			listener.onFocusGained();
	}
}
