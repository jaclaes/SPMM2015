package at.floxx.scrumify.sprintBoardSwt.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;
import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.SprintItemState;
import at.floxx.scrumify.sprintBoardSwt.Activator;
import at.floxx.scrumify.sprintBoardSwt.composites.BoardHeaderComposite;
import at.floxx.scrumify.sprintBoardSwt.composites.HeaderContentComposite;
import at.floxx.scrumify.sprintBoardSwt.composites.ScrumBoardComposite;
import at.floxx.scrumify.sprintBoardSwt.composites.StoryComposite;
import at.floxx.scrumify.sprintBoardSwt.composites.StoryTaskComposite;
import at.floxx.scrumify.sprintBoardSwt.composites.taskControl.TaskComposite;

/**The Scrumboard Controller class.
 * @author Mathias Breuss
 *
 */
public class ScrumBoardController {
	
	private Composite parent;
	private ScrumBoardComposite scrumBoardComposite;
	private Composite storyContainer;
	private Sprint sprint;
	private BoardHeaderComposite boardHeaderComposite;
	
	private boolean simulateDay = false;
	private Calendar simulateDate;
	
	private Map<Long, Integer[]> columnConfigMap = new HashMap<Long, Integer[]>();


	

	/**
	 * @return scrumBoardComposite
	 */
	public ScrumBoardComposite getScrumBoardComposite() {
		return scrumBoardComposite;
	}

	/**
	 * @param parent
	 */
	public ScrumBoardController(Composite parent) {
		this.parent = parent;
		initGui();
	}

	private void initGui() {
		sprint = new Sprint("", "", 28);
		sprint.setStorries(new ArrayList<ProductBacklogItem>());
		setSprint(sprint);
	}
	
	/**
	 * Reloads the board to reflect updated content.
	 */
	public void reloadBoard() {
		if(this.sprint == null)
			throw new RuntimeException("There was no Sprint defined when reloading the board");
		setSprint(this.sprint);
	}
	
	
	/**Sets the Sprint to be displayed on the Scrumboard.
	 * @param sprint
	 */
	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
		
		if(this.scrumBoardComposite != null && !this.scrumBoardComposite.isDisposed())
			this.scrumBoardComposite.dispose();	//Think about overwriting the dispose method to make it recursive for the childrens

		
		this.scrumBoardComposite = new ScrumBoardComposite(this.parent, SWT.NONE);
		this.storyContainer = scrumBoardComposite.getStoryContainer();
		this.boardHeaderComposite = scrumBoardComposite.getBoardHeaderComposite();
		
		//Set Header
		boardHeaderComposite.getSprintIdText().setText(String.valueOf(sprint.getId()));
		boardHeaderComposite.getSprintGoalText().setText(sprint.getGoal());
		boardHeaderComposite.getFromDate().setYear(3);

		if (sprint.getStartDate() != null) {
			boardHeaderComposite.getFromDate().setYear(
					sprint.getStartDate().get(Calendar.YEAR));
			boardHeaderComposite.getFromDate().setMonth(
					sprint.getStartDate().get(Calendar.MONTH));
			boardHeaderComposite.getFromDate().setDay(
					sprint.getStartDate().get(Calendar.DAY_OF_MONTH));
			boardHeaderComposite.getToDate().setYear(
					sprint.getEndDate().get(Calendar.YEAR));
			boardHeaderComposite.getToDate().setMonth(
					sprint.getEndDate().get(Calendar.MONTH));
			boardHeaderComposite.getToDate().setDay(
					sprint.getEndDate().get(Calendar.DAY_OF_MONTH));
		}
		//Set Day Simulations stuff
		boardHeaderComposite.getSimulationCheckbox().setSelection(simulateDay);
		boardHeaderComposite.getSimulationDate().setEnabled(simulateDay);
		if(simulateDate != null) {
			boardHeaderComposite.getSimulationDate().setDate(
					simulateDate.get(Calendar.YEAR),
					simulateDate.get(Calendar.MONTH),
					simulateDate.get(Calendar.DAY_OF_MONTH));
		}
		boardHeaderComposite.getSimulationCheckbox().addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
						if (!simulateDay) {
							simulateDay = true;
							boardHeaderComposite.getSimulationDate()
									.setEnabled(simulateDay);
							simulateDate = new GregorianCalendar(
									boardHeaderComposite.getSimulationDate()
											.getYear(), boardHeaderComposite
											.getSimulationDate().getMonth(),
									boardHeaderComposite.getSimulationDate()
											.getDay());
						}
						else {
							simulateDay = false;
							boardHeaderComposite.getSimulationDate()
									.setEnabled(simulateDay);
						}
				
			}});
		
		boardHeaderComposite.getSimulationDate().addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				simulateDate = new GregorianCalendar(
						boardHeaderComposite.getSimulationDate()
								.getYear(), boardHeaderComposite
								.getSimulationDate().getMonth(),
						boardHeaderComposite.getSimulationDate()
								.getDay());
				
			}});
		
		//Add ProductBacklogItems to board
		for (ProductBacklogItem pbItem : sprint.getStorries()) {
			addStory(pbItem);
		}
		scrumBoardComposite.pack();
		parent.pack();
		parent.getParent().pack();
	}
	
	private void addStory(final ProductBacklogItem pbItem) {

		StoryTaskComposite storyTaskComposite;
		
		if(columnConfigMap.containsKey(pbItem.getId()))
			storyTaskComposite = new StoryTaskComposite(this.storyContainer, SWT.NONE, columnConfigMap.get(pbItem.getId()));
		else {
			storyTaskComposite = new StoryTaskComposite(this.storyContainer, SWT.NONE);
			columnConfigMap.put(pbItem.getId(), storyTaskComposite.getRightsideAttachValue());
		}
		
		Composite requirementContent = storyTaskComposite.getRequirementContent();
		Composite openContent = storyTaskComposite.getOpenContent();
		Composite wipContent = storyTaskComposite.getWipContent();
		Composite doneContent = storyTaskComposite.getDoneContent();
		Composite tbvContent = storyTaskComposite.getTbvContent();
		
		Composite[] contents = new Composite[] {openContent, wipContent, tbvContent, doneContent};
		for (Composite content : contents) {
			final HeaderContentComposite hcc = (HeaderContentComposite)content.getParent().getParent();
			final SprintItemState sprintItemState = hcc.getSprintItemState();
			hcc.getDropTarget().addDropListener(new DropTargetListener(){

				@Override
				public void dragEnter(DropTargetEvent event) {
					System.out.println("Drag Enter");
					
				}

				@Override
				public void dragLeave(DropTargetEvent event) {}

				@Override
				public void dragOperationChanged(DropTargetEvent event) {}

				@Override
				public void dragOver(DropTargetEvent event) {}

				@Override
				public void drop(DropTargetEvent event) {
					String idString = (String)event.data;
					SprintBacklogItem sprintBacklogItem = Activator.getObjectProviderService().getSprintBacklogItemById(Long.parseLong(idString));
					if (!sprintBacklogItem.equals(null)) {
						sprintBacklogItem.setState(sprintItemState);
						if(!sprintItemState.equals(SprintItemState.OPEN)) {
							doUpdateOpenEstimate(sprintBacklogItem, sprintBacklogItem.getOpenEstimate());
						}
						reloadBoard();
					}
				}

				@Override
				public void dropAccept(DropTargetEvent event) {}});
			
			hcc.getBoardDimensionComposite().getAddButton().addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Integer[] config = columnConfigMap.get(pbItem.getId());
					switch (hcc.getSprintItemState()) {
					case OPEN:
						if(config[1] + 15 < config[2]) {
							config[1] += 15;
						}
						break;
					case WIP:
						if(config[2] + 15 < config[3]) {
							config[2] += 15;
						}
						break;
					case TBV:
						if(config[3] + 15 < config[4]) {
							config[3] += 15;
						}
						break;
					case DONE:
						break;
					default:
						throw new RuntimeException("Error in calculating columns");
					}
					
					columnConfigMap.put(pbItem.getId(), config);
					reloadBoard();
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			hcc.getBoardDimensionComposite().getRemoveButton().addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Integer[] config = columnConfigMap.get(pbItem.getId());
					switch (hcc.getSprintItemState()) {
					case OPEN:
						if(config[1] - 15 > config[0]) {
							config[1] -= 15;
						}
						break;
					case WIP:
						if(config[2] - 15 > config[1]) {
							config[2] -= 15;
						}
						break;
					case TBV:
						if(config[3] - 15 > config[2]) {
							config[3] -= 15;
						}
						break;
					case DONE:
						break;
					default:
						throw new RuntimeException("Error in calculating columns");
					}
					
					columnConfigMap.put(pbItem.getId(), config);
					reloadBoard();
					
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			

		}
		
		//Create Requirement Description from Story
		final StoryComposite storyComposite = new StoryComposite(requirementContent, SWT.NONE);
		storyComposite.getIdText().setText(String.valueOf(pbItem.getId()));
		storyComposite.getStoryNameText().setText(pbItem.getName());
		storyComposite.getDescriptionText().setText(pbItem.getDescription());
		//Add EventHandler
		storyComposite.getStoryNameText().addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				pbItem.setName(storyComposite.getStoryNameText().getText());
				
			}});
		storyComposite.getDescriptionText().addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				pbItem.setDescription(storyComposite.getDescriptionText().getText());
				
			}});
		
		storyComposite.pack();
		requirementContent.pack();
		
		//Add the Tasks
		for (SprintBacklogItem task : pbItem.getTasks()) {
			switch (task.getState()) {
			case OPEN:
				addTask(openContent, task);
				break;
			case WIP:
				addTask(wipContent, task);
				break;
			case TBV:
				addTask(tbvContent, task);
				break;
			case DONE:
				addTask(doneContent, task);
				break;
			default:
				throw new RuntimeException("State of Task does not match with board");
			}
		}
		openContent.pack();
		wipContent.pack();
		tbvContent.pack();
		doneContent.pack();
		
		storyContainer.pack();
		scrumBoardComposite.pack();
	}
	
	private void addTask(Composite target, final SprintBacklogItem task) {
		final TaskComposite newTask = new TaskComposite(target, SWT.BORDER);
		newTask.getDescriptionText().setText(task.getDescription());
		//adapt combo
		for(int i = 0; i <= task.getOpenEstimate(); i++) {
			newTask.getOpenEstimateCombo().add(String.valueOf(i));
		}
		newTask.getOpenEstimateCombo().select(task.getOpenEstimate());
		newTask.getResponsibleMemberText().setText(task.getResponsibility());
		newTask.setSprintBacklogItem(task);
		
		newTask.getOpenEstimateCombo().addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				int estimate = Integer.parseInt(newTask.getOpenEstimateCombo().getText());
				task.setOpenEstimate(estimate);
				doUpdateOpenEstimate(task, estimate);
			}});
		
		newTask.getResponsibleMemberText().addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				task.setResponsibility(newTask.getResponsibleMemberText().getText());
				
			}});
		
		newTask.getDescriptionText().addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				task.setDescription(newTask.getDescriptionText().getText());
				
			}});
		

		newTask.pack();
	}


	private void doUpdateOpenEstimate(SprintBacklogItem task, int estimate) {
		if(simulateDay)
			task.updateOpenEstimate(simulateDate, estimate);
		else
			task.updateOpenEstimate(new GregorianCalendar(), estimate);
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
		ScrumBoardController scrumBoardController = new ScrumBoardController(shell);
		ScrumBoardComposite inst = scrumBoardController.getScrumBoardComposite();
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

}
