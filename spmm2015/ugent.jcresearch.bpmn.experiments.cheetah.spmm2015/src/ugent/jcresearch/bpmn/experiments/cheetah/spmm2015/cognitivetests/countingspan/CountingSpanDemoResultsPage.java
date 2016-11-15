package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.cognitivetests.countingspan;

import org.eclipse.swt.graphics.Color;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.framework.Bundle;

import com.swtdesigner.SWTResourceManager;

public class CountingSpanDemoResultsPage extends CountingSpanResultsPage {

	private Button solutionButton;
	private Composite container;
	private List<Image> images;


	public CountingSpanDemoResultsPage(String pageName, List<CountingSpanExercise> level) {
		super(pageName, level, 0, 0);
		this.images = new ArrayList<Image>();
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

	private Image getSmallImage(String file) {
		try {
			Bundle bundle = Platform.getBundle("ugent.jcresearch.bpmn.experiments.cheetah.spmt2014");
			String path = FileLocator.resolve(bundle.getEntry("resources/CSPAN/" + file)).getPath();
			Image img = new Image(getShell().getDisplay(), path);
			
			double scale = 0.25;
			int width = img.getBounds().width;
			int height = img.getBounds().height;
			Image smallImg = new Image(getShell().getDisplay(),
			        img.getImageData().scaledTo((int)(width*scale),(int)(height*scale)));
			img.dispose();
			return smallImg;
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	protected boolean isDemo() {
		return true;
	}

	protected void showSolution() {
		solutionButton.setVisible(false);
		Font font = SWTResourceManager.getFont("Verdana", 10, SWT.NONE);
		Color color = container.getDisplay().getSystemColor(SWT.COLOR_BLUE);

		String text = "These were the numbers to remember: ";
		for (CountingSpanExercise exercise : level) {
			text += exercise.getTargets() + " ";
			
			Composite extra = new Composite(container, SWT.NONE);
			extra.setLayout(new RowLayout(SWT.HORIZONTAL));
			
			Label picLabel = new Label(extra, SWT.NONE);
			Image img = getSmallImage(exercise.getImg());
			images.add(img);
			picLabel.setImage(img);
			
			Label questionLabel = new Label(extra, SWT.NONE);
			questionLabel.setText(" < you should have counted " + exercise.getTargets() + " dark blue circles");
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

	public void dispose() {
		super.dispose();
		for (Image img : images)
			img.dispose();
	}
}
