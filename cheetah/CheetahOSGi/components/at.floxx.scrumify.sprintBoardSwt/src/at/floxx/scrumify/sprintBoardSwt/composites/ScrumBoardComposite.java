package at.floxx.scrumify.sprintBoardSwt.composites;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

/**Composite contains Scrumboard.
 * @author Mathias Breuss
 *
 */
public class ScrumBoardComposite extends org.eclipse.swt.widgets.Composite {
	private ScrolledComposite scrolledComposite1;
	private Composite storyContainer;
	private BoardHeaderComposite boardHeaderComposite;

	/**
	 * @return boardHeaderComposite
	 */
	public BoardHeaderComposite getBoardHeaderComposite() {
		return boardHeaderComposite;
	}

	/**
	 * @return storyContainer
	 */
	public Composite getStoryContainer() {
		return storyContainer;
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
		ScrumBoardComposite inst = new ScrumBoardComposite(shell, SWT.NULL);
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
	public ScrumBoardComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FillLayout thisLayout = new FillLayout(org.eclipse.swt.SWT.VERTICAL);
			this.setLayout(thisLayout);
			{
				scrolledComposite1 = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				FillLayout scrolledComposite1Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
				scrolledComposite1.setLayout(scrolledComposite1Layout);
				{
					storyContainer = new Composite(scrolledComposite1, SWT.NONE);
					boardHeaderComposite = new BoardHeaderComposite(storyContainer, SWT.NONE);
					RowLayout composite1Layout = new RowLayout(org.eclipse.swt.SWT.VERTICAL);
					storyContainer.setLayout(composite1Layout);
					scrolledComposite1.setContent(storyContainer);

					storyContainer.pack();
				}

				scrolledComposite1.pack();
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
