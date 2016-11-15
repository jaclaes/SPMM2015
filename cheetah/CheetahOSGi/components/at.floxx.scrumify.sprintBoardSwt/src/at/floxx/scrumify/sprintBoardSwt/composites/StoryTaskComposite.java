package at.floxx.scrumify.sprintBoardSwt.composites;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;

import at.floxx.scrumify.coreObjectsProvider.core.SprintItemState;


/**Composite contains the different categories of Tasks.
 * @author Mathias Breuss
 *
 */
public class StoryTaskComposite extends org.eclipse.swt.widgets.Composite {
	private HeaderContentComposite requirementHeaderContentComposite;
	private HeaderContentComposite wipContentComposite;
	private HeaderContentComposite doneContentComposite;
	private HeaderContentComposite openContentComposite;
	private Composite requirementContent;
	private Composite openContent;
	private Composite doneContent;
	private Composite wipContent;
	private HeaderContentComposite tbvContentComposite;
	private Composite tbvContent;

	private Integer[] rightsideAttachValue = {10,40,70,85,100};
	
	
	
	/**
	 * @return rightsideAttachValue
	 */
	public Integer[] getRightsideAttachValue() {
		return rightsideAttachValue;
	}

	/**
	 * @param rightsideAttachValue
	 */
	public void setRightsideAttachValue(Integer[] rightsideAttachValue) {
		this.rightsideAttachValue = rightsideAttachValue;
	}

	/**
	 * @return requirementContent
	 */
	public Composite getRequirementContent() {
		return requirementContent;
	}

	/**
	 * @return openContent
	 */
	public Composite getOpenContent() {
		return openContent;
	}

	/**
	 * @return doneContent
	 */
	public Composite getDoneContent() {
		return doneContent;
	}

	/**
	 * @return wipContent
	 */
	public Composite getWipContent() {
		return wipContent;
	}

	/**
	 * @return tbvContent
	 */
	public Composite getTbvContent() {
		return tbvContent;
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
		StoryTaskComposite inst = new StoryTaskComposite(shell, SWT.NULL);
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
	public StoryTaskComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}
	
	/**The Constructor.
	 * @param parent
	 * @param style
	 * @param rightsideAttachValue
	 */
	public StoryTaskComposite(org.eclipse.swt.widgets.Composite parent, int style, Integer[] rightsideAttachValue) {
		super(parent, style);
		this.rightsideAttachValue = rightsideAttachValue;
		initGUI();
	}
	
	@Override
	public Point computeSize(int wHint,
            int hHint,
            boolean changed) {
				return new Point(1500, 200);
		
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			int columnWidth = 15;

			{
				int requirementHeaderContentWidth = 80;
				FormData requirementHeaderContentCompositeLData = new FormData();
				requirementHeaderContentCompositeLData.left =  new FormAttachment(0);
				requirementHeaderContentCompositeLData.top =  new FormAttachment(0, 1000, 0);
				requirementHeaderContentCompositeLData.bottom =  new FormAttachment(1000, 1000, 0);
				requirementHeaderContentCompositeLData.right =  new FormAttachment(rightsideAttachValue[0]);
				requirementHeaderContentComposite = new HeaderContentComposite(this, SWT.NONE, requirementHeaderContentWidth, null, 1);
				requirementHeaderContentComposite.getColumnLabel().setText("Req.");
				requirementHeaderContentComposite.setLayoutData(requirementHeaderContentCompositeLData);
				requirementContent = requirementHeaderContentComposite.getContentComposite();
				requirementHeaderContentComposite.getScrolledComposite().setExpandHorizontal(true);
				requirementHeaderContentComposite.getScrolledComposite().setExpandVertical(true);
			}
			{
				FormData openContentCompositeLData = new FormData();
				openContentCompositeLData.left =  new FormAttachment(requirementHeaderContentComposite, 0, SWT.RIGHT);
				openContentCompositeLData.top =  new FormAttachment(0, 1000, 0);
				openContentCompositeLData.bottom =  new FormAttachment(1000, 1000, 0);
				openContentCompositeLData.right =  new FormAttachment(rightsideAttachValue[1]);
				int openContentColumns = (rightsideAttachValue[1] - rightsideAttachValue[0]) / columnWidth;
				openContentComposite = new HeaderContentComposite(this, SWT.NONE, SprintItemState.OPEN, openContentColumns);
				openContentComposite.getColumnLabel().setText("Open");
				openContentComposite.setLayoutData(openContentCompositeLData);
				openContent = openContentComposite.getContentComposite();
			}
			{
				FormData wipContentCompositeLData = new FormData();
				wipContentCompositeLData.left =  new FormAttachment(openContentComposite, 0, SWT.RIGHT);
				wipContentCompositeLData.top =  new FormAttachment(0, 1000, 0);
				wipContentCompositeLData.bottom =  new FormAttachment(1000, 1000, 0);
				wipContentCompositeLData.right =  new FormAttachment(rightsideAttachValue[2]);
				int wipContentColumns = (rightsideAttachValue[2] - rightsideAttachValue[1]) / columnWidth;
				wipContentComposite = new HeaderContentComposite(this, SWT.NONE, SprintItemState.WIP, wipContentColumns);
				wipContentComposite.getColumnLabel().setText("Work in Progress");
				wipContentComposite.setLayoutData(wipContentCompositeLData);
				wipContent = wipContentComposite.getContentComposite();
			}
			{
				FormData tbvContentCompositeLData = new FormData();
				tbvContentCompositeLData.left =  new FormAttachment(wipContentComposite, 0, SWT.RIGHT);
				tbvContentCompositeLData.top =  new FormAttachment(0, 1000, 0);
				tbvContentCompositeLData.bottom =  new FormAttachment(1000, 1000, 0);
				tbvContentCompositeLData.right =  new FormAttachment(rightsideAttachValue[3]);
				int tbvContentColumns = (rightsideAttachValue[3] - rightsideAttachValue[2]) / columnWidth;
				tbvContentComposite = new HeaderContentComposite(this, SWT.NONE, SprintItemState.TBV, tbvContentColumns);
				tbvContentComposite.getColumnLabel().setText("TBV");
				tbvContentComposite.setLayoutData(tbvContentCompositeLData);
				tbvContent = tbvContentComposite.getContentComposite();
			}
			{
				FormData doneContentCompositeLData = new FormData();
				doneContentCompositeLData.left =  new FormAttachment(tbvContentComposite, 0, SWT.RIGHT);
				doneContentCompositeLData.top =  new FormAttachment(0, 1000, 0);
				doneContentCompositeLData.bottom =  new FormAttachment(1000, 1000, 0);
				doneContentCompositeLData.right =  new FormAttachment(rightsideAttachValue[4]);
				int doneContentColumns = (rightsideAttachValue[4] - rightsideAttachValue[3]) / columnWidth;
				doneContentComposite = new HeaderContentComposite(this, SWT.NONE, SprintItemState.DONE, doneContentColumns);
				doneContentComposite.getColumnLabel().setText("Done");
				doneContentComposite.setLayoutData(doneContentCompositeLData);
				doneContent = doneContentComposite.getContentComposite();
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
