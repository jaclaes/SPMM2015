package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CountingSpanDemoWizardPage extends CountingSpanWizardPage {

	public CountingSpanDemoWizardPage(String pageName, List<CountingSpanExercise> level, int idx) {
		super(pageName, level, idx, 0, 0);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		setTitle(getTitle());
		
		@SuppressWarnings("unused")
		Label filler = new Label(control, SWT.WRAP);
		
		Label text = new Label(control, SWT.NONE);
		text.setText("DEMO. Count the dark blue circles, enter the amount and press 'Answer'.");
		text.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + " - DEMO";
	}	
}
