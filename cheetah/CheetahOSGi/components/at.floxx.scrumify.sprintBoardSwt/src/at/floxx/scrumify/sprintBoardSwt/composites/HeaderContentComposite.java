package at.floxx.scrumify.sprintBoardSwt.composites;

import at.floxx.scrumify.coreObjectsProvider.core.SprintItemState;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;


/**Composite contains Header and Content of a Story.
 * @author Mathias Breuss
 *
 */
public class HeaderContentComposite extends org.eclipse.swt.widgets.Composite {
	private Label columnLabel;
	private Composite contentComposite;
	private ScrolledComposite scrolledComposite;
	private BoardDimensionComposite boardDimensionComposite;
	private GridLayout contentCompositeLayout;
	private int width;
	private SprintItemState sprintItemState;
	private DropTarget dropTarget;
	private int columns;

	/**
	 * @return the scrolledComposite
	 */
	public ScrolledComposite getScrolledComposite() {
		return scrolledComposite;
	}

	/**
	 * @return dropTarget
	 */
	public DropTarget getDropTarget() {
		return dropTarget;
	}

	/**
	 * @return boardDimensionComposite
	 */
	public BoardDimensionComposite getBoardDimensionComposite() {
		return boardDimensionComposite;
	}

	/**
	 * @return columnLabel
	 */
	public Label getColumnLabel() {
		return columnLabel;
	}

	/**
	 * @return contentComposite
	 */
	public Composite getContentComposite() {
		return contentComposite;
	}

	
	/**
	 * @return sprintItemState
	 */
	public SprintItemState getSprintItemState() {
		return sprintItemState;
	}

	/**
	 * @param sprintItemState
	 */
	public void setSprintItemState(SprintItemState sprintItemState) {
		this.sprintItemState = sprintItemState;
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
		HeaderContentComposite inst = new HeaderContentComposite(shell, SWT.NULL, SprintItemState.DONE, 2);
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
	 * @param state
	 * @param columns
	 */
	public HeaderContentComposite(org.eclipse.swt.widgets.Composite parent, int style, SprintItemState state, int columns) {
		this(parent, style, 360, state, columns);

	}
	
	/**The Constructor.
	 * @param parent
	 * @param style
	 * @param width
	 * @param state
	 * @param columns
	 */
	public HeaderContentComposite(org.eclipse.swt.widgets.Composite parent, int style, int width, SprintItemState state, int columns) {
		super(parent, style);
		this.sprintItemState = state;
		this.width = width;
		this.columns = columns;
		initGUI();
	}
	
	@Override
	public Point computeSize(int wHint,
            int hHint,
            boolean changed) {
				return new Point(width, 180);
		
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				FormData scrolledCompositeLData = new FormData();
				scrolledCompositeLData.left =  new FormAttachment(0, 1000, 0);
				scrolledCompositeLData.top =  new FormAttachment(0, 1000, 20);
				scrolledCompositeLData.bottom =  new FormAttachment(1000, 1000, 0);
				scrolledCompositeLData.right =  new FormAttachment(1000, 1000, 0);
				scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
				FillLayout scrolledCompositeLayout = new FillLayout(org.eclipse.swt.SWT.VERTICAL);
				scrolledComposite.setLayout(scrolledCompositeLayout);
				scrolledComposite.setLayoutData(scrolledCompositeLData);
				{
					contentComposite = new Composite(scrolledComposite, SWT.NONE);
					//RowLayout contentCompositeLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
					contentCompositeLayout = new GridLayout();
					contentCompositeLayout.numColumns = this.columns;
					contentComposite.setLayout(contentCompositeLayout);
					scrolledComposite.setContent(contentComposite);
				}
				

				int operations = DND.DROP_MOVE;
				dropTarget = new DropTarget(scrolledComposite, operations);
				Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
				dropTarget.setTransfer(types);

				
			}
			{
				columnLabel = new Label(this, SWT.NONE);
				FormData label1LData = new FormData();
				label1LData.left =  new FormAttachment(0, 1000, 0);
				label1LData.right =  new FormAttachment(500, 1000, 0);
				label1LData.top =  new FormAttachment(0, 1000, 0);
				columnLabel.setLayoutData(label1LData);
				columnLabel.setText("label1");
			}
			{
				boolean compositeVisible = false;
				if (sprintItemState != null
						&& (sprintItemState.equals(SprintItemState.OPEN)
								|| sprintItemState.equals(SprintItemState.WIP) || sprintItemState
								.equals(SprintItemState.TBV))) {
					compositeVisible = true;
				}
					boardDimensionComposite = new BoardDimensionComposite(this,
							SWT.NONE);
					boardDimensionComposite.setVisible(compositeVisible);
					FormData boardDimensionLData = new FormData();
					boardDimensionLData.top = new FormAttachment(0, 1000, 0);
					boardDimensionLData.right = new FormAttachment(1000, 1000,
							0);
					boardDimensionComposite.setLayoutData(boardDimensionLData);

			}
			

			
			
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
