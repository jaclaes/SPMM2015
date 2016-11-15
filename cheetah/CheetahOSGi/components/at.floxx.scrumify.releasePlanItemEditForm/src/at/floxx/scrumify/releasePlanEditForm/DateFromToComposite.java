package at.floxx.scrumify.releasePlanEditForm;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

/**DateFromToComposite contains the From and To Date fields.
 * @author mathias
 *
 */
public class DateFromToComposite extends org.eclipse.swt.widgets.Composite {

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	 * @param args 
	*/
	public static void main(String[] args) {
		showGUI();
	}

	private Label startLabel;
	private DateTime startDateTime;
	private Label endLabel;
	private DateTime endDateTime;
	
	
	/**
	 * @return startDateTime
	 */
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	/**
	 * @return endDateTime
	 */
	public DateTime getEndDateTime() {
		return endDateTime;
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
		DateFromToComposite inst = new DateFromToComposite(shell, SWT.NULL);
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
	public DateFromToComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(new RowLayout(SWT.HORIZONTAL));
			startLabel = new Label(this, SWT.NONE);
			startLabel.setText("Start: ");
	
			startDateTime = new DateTime(this, SWT.NONE);
			
			endLabel = new Label(this, SWT.NONE);
			endLabel.setText("End: ");
	
			endDateTime = new DateTime(this, SWT.NONE);
			
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
