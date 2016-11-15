package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.readingspan;

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

public class ReadingSpanDemoResultsPage extends ReadingSpanResultsPage {

	private Button solutionButton;
	private Composite container;

	public ReadingSpanDemoResultsPage(String pageName, List<ReadingSpanExercise> level) {
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
		for (ReadingSpanExercise exercise : level) {
			text += exercise.getWord() + " ";
			Label questionLabel = new Label(container, SWT.NONE);
			questionLabel.setText(trim(exercise.toString(), 70) + " < you should have answered " + (exercise.makesSense()?"MAKES SENSE":"MAKES NO SENSE"));
			questionLabel.setFont(font);
			questionLabel.setForeground(color);
		}
		Label words = new Label(container, SWT.NONE);
		words.setText(text);
		words.setFont(font);
		words.setForeground(color);
		

		container.layout(true, true);
	}
	private String trim(String s, int len) {
		//assumes len > 3
		if (s.length() <= len)
			return s;
		return s.substring(0,len-3) + "...";
	}
	
	@Override
	public String getTitle() {
		return super.getTitle() + " - DEMO";
	}	
}
