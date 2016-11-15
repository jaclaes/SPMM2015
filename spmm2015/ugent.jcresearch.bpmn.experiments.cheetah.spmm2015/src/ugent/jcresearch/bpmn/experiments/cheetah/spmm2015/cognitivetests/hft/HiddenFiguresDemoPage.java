package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class HiddenFiguresDemoPage extends HiddenFiguresWizardPage {

	protected Label text;
	
	public HiddenFiguresDemoPage(String pageName, List<HiddenFiguresExercise> level, int idx) {
		super(pageName, level, idx, 0, 0);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		text = new Label(control, SWT.NONE);
		text.setText("Indicate above which of the top figures is comprised in the bottom one and then click 'Next'");
		text.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + " - DEMO";
	}	
}
