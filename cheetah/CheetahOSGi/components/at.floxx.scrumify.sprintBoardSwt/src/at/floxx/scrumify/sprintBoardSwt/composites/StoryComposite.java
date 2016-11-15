package at.floxx.scrumify.sprintBoardSwt.composites;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Composite contains the Story Information.
 * @author Mathias Breuss
 *
 */
public class StoryComposite extends org.eclipse.swt.widgets.Composite {
	private Label idLabel;
	private Text idText;
	private Label descriptionLabel;
	private Text descriptionText;
	private Text storyNameText;
	private Label nameLabel;

	
	/**
	 * @return descriptionText
	 */
	public Text getDescriptionText() {
		return descriptionText;
	}

	/**
	 * @return idText
	 */
	public Text getIdText() {
		return idText;
	}

	/**
	 * @return storyNameText
	 */
	public Text getStoryNameText() {
		return storyNameText;
	}

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	 * @param args 
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		StoryComposite inst = new StoryComposite(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**The Constructor.
	 * @param parent
	 * @param style
	 */
	public StoryComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				idLabel = new Label(this, SWT.NONE);
				FormData idLabelLData = new FormData();
				idLabelLData.width = 60;
				idLabelLData.height = 19;
				idLabelLData.left =  new FormAttachment(0, 1000, 0);
				idLabelLData.right =  new FormAttachment(400, 1000, 0);
				idLabelLData.top =  new FormAttachment(2, 1000, 0);
				idLabelLData.bottom =  new FormAttachment(106, 1000, 0);
				idLabel.setLayoutData(idLabelLData);
				idLabel.setText("Id:");
			}
			{
				idText = new Text(this, SWT.NONE);
				idText.setText("Id...");
				FormData idTextLData = new FormData();
				idTextLData.width = 80;
				idTextLData.height = 15;
				idTextLData.left =  new FormAttachment(400, 1000, 0);
				idTextLData.right =  new FormAttachment(1000, 1000, 0);
				idTextLData.top =  new FormAttachment(2, 1000, 0);
				idTextLData.bottom =  new FormAttachment(106, 1000, 0);
				idText.setLayoutData(idTextLData);
				idText.setEnabled(false);
			}
			{
				nameLabel = new Label(this, SWT.NONE);
				FormData nameLabelLData = new FormData();
				nameLabelLData.width = 140;
				nameLabelLData.height = 19;
				nameLabelLData.left =  new FormAttachment(0, 1000, 0);
				nameLabelLData.right =  new FormAttachment(1000, 1000, 0);
				nameLabelLData.top =  new FormAttachment(106, 1000, 0);
				nameLabelLData.bottom =  new FormAttachment(210, 1000, 0);
				nameLabel.setLayoutData(nameLabelLData);
				nameLabel.setText("Name:");
			}
			{
				storyNameText = new Text(this, SWT.MULTI | SWT.WRAP);
				FormData storyNameTextLData = new FormData();
				storyNameTextLData.width = 140;
				storyNameTextLData.height = 43;
				storyNameTextLData.left =  new FormAttachment(0, 1000, 0);
				storyNameTextLData.right =  new FormAttachment(1000, 1000, 0);
				storyNameTextLData.top =  new FormAttachment(210, 1000, 0);
				storyNameTextLData.bottom =  new FormAttachment(445, 1000, 0);
				storyNameText.setLayoutData(storyNameTextLData);
				storyNameText.setText("Name...");
			}
			{
				descriptionLabel = new Label(this, SWT.NONE);
				FormData descriptionLabelLData = new FormData();
				descriptionLabelLData.width = 140;
				descriptionLabelLData.height = 22;
				descriptionLabelLData.left =  new FormAttachment(0, 1000, 0);
				descriptionLabelLData.right =  new FormAttachment(1000, 1000, 0);
				descriptionLabelLData.top =  new FormAttachment(445, 1000, 0);
				descriptionLabelLData.bottom =  new FormAttachment(565, 1000, 0);
				descriptionLabel.setLayoutData(descriptionLabelLData);
				descriptionLabel.setText("Description:");
			}
			{
				descriptionText = new Text(this, SWT.MULTI | SWT.WRAP);
				descriptionText.setText("Description");
				FormData descriptionTextLData = new FormData();
				descriptionTextLData.width = 140;
				descriptionTextLData.height = 80;
				descriptionTextLData.left =  new FormAttachment(2, 1000, 0);
				descriptionTextLData.right =  new FormAttachment(1000, 1000, 0);
				descriptionTextLData.top =  new FormAttachment(565, 1000, 0);
				descriptionTextLData.bottom =  new FormAttachment(1002, 1000, 0);
				descriptionText.setLayoutData(descriptionTextLData);
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
