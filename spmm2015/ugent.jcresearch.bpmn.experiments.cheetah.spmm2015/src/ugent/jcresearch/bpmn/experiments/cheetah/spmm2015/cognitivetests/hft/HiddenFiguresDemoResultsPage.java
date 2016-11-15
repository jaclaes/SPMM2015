package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.hft;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.IFocusGainedListener;

public class HiddenFiguresDemoResultsPage extends HiddenFiguresDemoPage implements IFocusGainedListener {
	protected List<IFocusGainedListener> listeners;

	public HiddenFiguresDemoResultsPage(String pageName, List<HiddenFiguresExercise> level, int idx) {
		super(pageName, level, idx);
		listeners = new ArrayList<IFocusGainedListener>();
		listeners.add(this);
	}
	
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		String file = exercise.getImg().replace(".png", "b.png");
		image = getImage(file);
		imgLabel.setImage(image);
		
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setEnabled(false);
		}
		
		text.setText("This is the solution of the exercise. Press 'Back' to view the exercise again or 'Next' to continue.");
	}
	
	public void onFocusGained() {
		for (int i=0; i<buttons.length; i++) {
			buttons[i].setSelection(((HiddenFiguresWizardPage)getPreviousPage()).buttons[i].getSelection());
		}
	}
}
