package at.floxx.scrumify.sprintBoardSwt.composites;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Composite containing the Header of the Scrumboard, with principle Information.
 * @author Mathias Breuss
 *
 */
public class BoardHeaderComposite extends org.eclipse.swt.widgets.Composite {
	private Label sprintIdLabel;
	private Label fromLabel;
	private Button simulationCheckbox;
	private Label simulationLabel;
	private Label toLabel;
	private Text sprintIdText;
	private DateTime fromDate;
	private DateTime toDate;
	private DateTime simulationDate;
	private Label sprintGoalLabel;
	private Text sprintGoalText;

	
	
	/**
	 * @return simulationCheckbox
	 */
	public Button getSimulationCheckbox() {
		return simulationCheckbox;
	}

	/**
	 * @return sprintIdText
	 */
	public Text getSprintIdText() {
		return sprintIdText;
	}

	/**
	 * @return fromDate
	 */
	public DateTime getFromDate() {
		return fromDate;
	}

	/**
	 * @return toDate
	 */
	public DateTime getToDate() {
		return toDate;
	}

	/**
	 * @return simulationDate
	 */
	public DateTime getSimulationDate() {
		return simulationDate;
	}

	/**
	 * @return sprintGoalText
	 */
	public Text getSprintGoalText() {
		return sprintGoalText;
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
		BoardHeaderComposite inst = new BoardHeaderComposite(shell, SWT.NULL);
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
	public BoardHeaderComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			RowLayout thisLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			{
				sprintIdLabel = new Label(this, SWT.NONE);
				RowData sprintIdLabelLData = new RowData();
				sprintIdLabel.setLayoutData(sprintIdLabelLData);
				sprintIdLabel.setText("Sprint: ");
			}
			{
				sprintIdText = new Text(this, SWT.NONE);
				RowData sprintIdTextLData = new RowData();
				sprintIdText.setLayoutData(sprintIdTextLData);
				sprintIdText.setText("...");
				sprintIdText.setEnabled(false);
			}
			{
				sprintGoalLabel = new Label(this, SWT.NONE);
				RowData sprintGoalLabelLData = new RowData();
				sprintGoalLabel.setLayoutData(sprintGoalLabelLData);
				sprintGoalLabel.setText("Goal: ");
			}
			{
				sprintGoalText = new Text(this, SWT.NONE);
				RowData sprintGoalTextLData = new RowData();
				sprintGoalText.setLayoutData(sprintGoalTextLData);
				sprintGoalText.setText("Well whatever");
				sprintGoalText.setEnabled(false);
			}
			{
				fromLabel = new Label(this, SWT.NONE);
				RowData fromLabelLData = new RowData();
				fromLabel.setLayoutData(fromLabelLData);
				fromLabel.setText("From:");
			}
			{
				fromDate = new DateTime(this, SWT.NONE);
				fromDate.setEnabled(false);
			}
			{
				toLabel = new Label(this, SWT.NONE);
				RowData toLabelLData = new RowData();
				toLabel.setLayoutData(toLabelLData);
				toLabel.setText("To:");
			}
			{
				toDate = new DateTime(this, SWT.NONE);
				toDate.setEnabled(false);
			}
			{
				simulationLabel = new Label(this, SWT.NONE);
				RowData simulationLabelLData = new RowData();
				simulationLabel.setLayoutData(simulationLabelLData);
				simulationLabel.setText("Simulation:");
			}
			{
				simulationCheckbox = new Button(this, SWT.CHECK | SWT.LEFT);
				RowData simulationCheckboxLData = new RowData();
				simulationCheckbox.setLayoutData(simulationCheckboxLData);
				simulationCheckbox.setText("Simulationmode");
			}
			{
				simulationDate = new DateTime(this, SWT.NONE);
			}
			
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
