package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.operationspan;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.swtdesigner.SWTResourceManager;

public class OperationSpanDemoResultsPage extends OperationSpanResultsPage {

	private Button solutionButton;
	private Composite container;

	public OperationSpanDemoResultsPage(String pageName, List<OperationSpanExercise> level) {
		super(pageName, level, 0, 0);
	}

	@Override
	protected void addAdditionalUiElements(Composite parent) {
		this.container = parent;
		solutionButton = new Button(parent, SWT.NONE);
		solutionButton.setText("Show solution");
		solutionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showSolution();
			}
		});
	}

	@Override
	protected boolean isDemo() {
		return true;
	}

	protected void showSolution() {
		solutionButton.setVisible(false);
		Font font = SWTResourceManager.getFont("Verdana", 10, SWT.NONE);
		Color color = container.getDisplay().getSystemColor(SWT.COLOR_BLUE);

		String text = "These were the words to remember: ";
		for (OperationSpanExercise exercise : level) {
			text += exercise.getWord() + " ";
			Label questionLabel = new Label(container, SWT.NONE);
			questionLabel.setText(exercise.getCalculation() + " < you should have answered " + (exercise.checkSolution()?"CORRECT":"WRONG"));
			questionLabel.setFont(font);
			questionLabel.setForeground(color);
		}
		Label words = new Label(container, SWT.NONE);
		words.setText(text);
		words.setFont(font);
		words.setForeground(color);

		container.layout(true, true);
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + " - DEMO";
	}	
}
