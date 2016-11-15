package at.floxx.scrumify.sprintBoardSwt.composites.taskControl;
import com.cloudgarden.resource.SWTResourceManager;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

/**Composite contains Status controls of the Task Composite.
 * @author Mathias Breuss
 *
 */
public class StatusComposite extends org.eclipse.swt.widgets.Composite {

	 {
		 //Register as a resource user - SWTResourceManager will
		 //handle the obtaining and disposing of resources
		 SWTResourceManager.registerResourceUser(this);
	 }
	 
	private Combo openEstimateCombo;
	private Text responsibleMemberText;

	
	/**
	 * @return responsibleMemberText
	 */
	public Text getResponsibleMemberText() {
		return responsibleMemberText;
	}

	/**
	 * @return openEstimateCombo
	 */
	public Combo getOpenEstimateCombo() {
		return openEstimateCombo;
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
	 * Overriding checkSubclass allows this class to extend
	 * org.eclipse.swt.widgets.Composite
	 */
	protected void checkSubclass() {
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite
	 * inside a new Shell.
	 */
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		StatusComposite inst = new StatusComposite(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if (size.x == 0 && size.y == 0) {
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
	public StatusComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FillLayout thisLayout = new FillLayout(
					org.eclipse.swt.SWT.HORIZONTAL);
			this.setLayout(thisLayout);
			this.setBackground(SWTResourceManager.getColor(173, 216, 230));
			{
				openEstimateCombo = new Combo(this, SWT.NONE);
				openEstimateCombo.setFont(SWTResourceManager.getFont("Sans", 8, 0, false, false));

				responsibleMemberText = new Text(this, SWT.NONE);
				responsibleMemberText.setText("Who");
				responsibleMemberText.setFont(SWTResourceManager.getFont("Sans", 8, 0, false, false));
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
