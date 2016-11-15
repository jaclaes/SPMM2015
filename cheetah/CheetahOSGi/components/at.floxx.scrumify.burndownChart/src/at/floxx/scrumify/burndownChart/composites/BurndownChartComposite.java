package at.floxx.scrumify.burndownChart.composites;
import info.monitorenter.gui.chart.Chart2D;


import java.awt.Frame;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;

/**Contains the BurndownChart.
 * @author Mathias Breuss
 *
 */
public class BurndownChartComposite extends org.eclipse.swt.widgets.Composite {
	private Composite swingComposite;
	private Frame frame1;
	private Chart2D chart;

	/**Returns the Chart2D Object.
	 * @return current Chart2D
	 */
	public Chart2D getChart() {
		return chart;
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
		BurndownChartComposite inst = new BurndownChartComposite(shell, SWT.NULL);
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

	/**Constructor.
	 * @param parent
	 * @param style
	 */
	public BurndownChartComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FillLayout thisLayout = new FillLayout();
			this.setLayout(thisLayout);
			{
				swingComposite = new Composite(this, SWT.EMBEDDED);
				{
					frame1 = SWT_AWT.new_Frame(swingComposite);
					{
					    // Create a chart:  
					    chart = new Chart2D();
						frame1.add(chart);
					}
				}
			}

			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
