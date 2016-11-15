package at.floxx.scrumify.sprintBoardSwt.composites.taskControl;
import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;

import com.cloudgarden.resource.SWTResourceManager;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**Composite contains the Task aka SprintBacklogEntry on the Scrumboard.
 * @author Mathias Breuss
 *
 */
public class TaskComposite extends org.eclipse.swt.widgets.Composite {
	private StatusComposite statusComposite;
	private Text descriptionText;
	private SprintBacklogItem sprintBacklogItem;
	private Composite grabComposite;

	/**
	 * @return grabComposite
	 */
	public Composite getGrabComposite() {
		return grabComposite;
	}

	/**
	 * @return sprintBacklogItem
	 */
	public SprintBacklogItem getSprintBacklogItem() {
		return sprintBacklogItem;
	}

	/**
	 * @param sprintBacklogItem
	 */
	public void setSprintBacklogItem(SprintBacklogItem sprintBacklogItem) {
		this.sprintBacklogItem = sprintBacklogItem;
	}

	/**
	 * @return statusComposite
	 */
	public StatusComposite getStatusComposite() {
		return statusComposite;
	}

	/**
	 * @return descriptionText
	 */
	public Text getDescriptionText() {
		return descriptionText;
	}
	
	/**
	 * @return ResponsibleMemberText
	 */
	public Text getResponsibleMemberText() {
		return statusComposite.getResponsibleMemberText();
	}

	/**
	 * @return OpenEstimateCombo
	 */
	public Combo getOpenEstimateCombo() {
		return statusComposite.getOpenEstimateCombo();
	}

	{
		//Register as a resource user - SWTResourceManager will
		//handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
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
		TaskComposite inst = new TaskComposite(shell, SWT.NULL);
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
	public TaskComposite(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {

		
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				FormData grabCompositeLData = new FormData();
				grabCompositeLData.left =  new FormAttachment(0, 1000, 156);
				grabCompositeLData.top =  new FormAttachment(0, 1000, 25);
				grabCompositeLData.width = 32;
				grabCompositeLData.height = 57;
				
				grabComposite = new Composite(this, SWT.NONE);
				grabComposite.setLayoutData(grabCompositeLData);
				GridLayout grabCompositeLayout = new GridLayout();
				grabCompositeLayout.makeColumnsEqualWidth = true;
				grabComposite.setLayout(grabCompositeLayout);
				grabComposite.setBackgroundImage(SWTResourceManager.getImage("resources/hand.png"));
			}
			{
				FormData statusCompositeLData = new FormData();
				statusCompositeLData.left =  new FormAttachment(0, 1000, 0);
				statusCompositeLData.top =  new FormAttachment(0, 1000, 0);
				statusCompositeLData.width = 190;
				statusComposite = new StatusComposite(this, SWT.NONE);
				statusComposite.setLayoutData(statusCompositeLData);
			}
			{
				descriptionText = new Text(this, SWT.MULTI | SWT.WRAP);
				FormData descriptionTextLData = new FormData();
				descriptionTextLData.left =  new FormAttachment(0, 1000, 0);
				descriptionTextLData.right =  new FormAttachment(1000, 1000, 0);
				descriptionTextLData.bottom =  new FormAttachment(1000, 1000, 0);
				descriptionTextLData.height = 57;
				descriptionTextLData.width = 155;
				descriptionTextLData.top =  new FormAttachment(0, 1000, 25);
				descriptionText.setLayoutData(descriptionTextLData);
				descriptionText.setText("");
			}
			
			
			//Drag for DND
			int operations = DND.DROP_MOVE;
			DragSource source = new DragSource(grabComposite, operations);
			Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
			source.setTransfer(types);
			source.addDragListener(new DragSourceListener() {
				
				@Override
				public void dragStart(DragSourceEvent event) {}
				
				@Override
				public void dragSetData(DragSourceEvent event) {
					event.data = String.valueOf(sprintBacklogItem.getId());
					
				}
				
				@Override
				public void dragFinished(DragSourceEvent event) {}
			});

			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
