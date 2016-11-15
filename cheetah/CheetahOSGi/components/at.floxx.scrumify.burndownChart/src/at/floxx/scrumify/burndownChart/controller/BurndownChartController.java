package at.floxx.scrumify.burndownChart.controller;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import at.component.IComponent;
import at.floxx.scrumify.burndownChart.ComponentUiController;
import at.floxx.scrumify.burndownChart.composites.BurndownChartComposite;
import at.floxx.scrumify.coreObjectsProvider.core.ProductBacklogItem;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;
import at.floxx.scrumify.coreObjectsProvider.core.SprintBacklogItem;

/**The BurndownChart Controller for drawing the Burndown Chart.
 * @author mathias
 *
 */
public class BurndownChartController implements Observer {
	
	private Composite parent;
	private IComponent component;
	private BurndownChartComposite burndownChartComposite;
	private ComponentUiController componentUiController;
	private Sprint sprintItem;

	/**The Constructor.
	 * @param parent
	 * @param component
	 * @param componentUiController
	 */
	public BurndownChartController(Composite parent, IComponent component, ComponentUiController componentUiController) {
		this.parent = parent;
		this.setComponent(component);
		this.componentUiController = componentUiController;
		
		componentUiController.addObserver(this);
		
		init();
	}

	private void init() {
		burndownChartComposite = new BurndownChartComposite(parent, SWT.NONE);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		sprintItem = componentUiController.getSprintItem();
		
		long endDayNr = dayOfTime(sprintItem.getEndDate());
		
		if(endDayNr == 0)
			return;
		
		long sprintSpeed = sprintItem.getSpeed();
		long sprintSpeedPerDay = sprintSpeed / (endDayNr + 1);
		
		
		long currentAmount = sprintSpeed;
	    
		//Create Traces
		burndownChartComposite.getChart().removeAllTraces();
		
		ITrace2D optimalTrace = new Trace2DSimple(); 
	    optimalTrace.setName("Optimal");
	    optimalTrace.setColor(Color.GREEN);
	    
		ITrace2D actualTrace = new Trace2DSimple(); 
		actualTrace.setName("Actual");
		actualTrace.setColor(Color.BLUE);
	    
		for(int day = 0; day <= endDayNr+1; day++) {
			//Optimal Trace
			optimalTrace.addPoint(day, currentAmount);
			currentAmount -= sprintSpeedPerDay;
			
			//Actual Progress
			actualTrace.addPoint(day, getAmount(day));
		}
		
		//Handle the axsis
		burndownChartComposite.getChart().getAxisX().getAxisTitle().setTitle("Days of Sprint");
		burndownChartComposite.getChart().getAxisY().getAxisTitle().setTitle("Open Work");
		burndownChartComposite.getChart().getAxisX().setMajorTickSpacing(1);
		burndownChartComposite.getChart().getAxisX().setMinorTickSpacing(1);

		burndownChartComposite.getChart().addTrace(optimalTrace);
		burndownChartComposite.getChart().addTrace(actualTrace);
	}
	
	private double getAmount(int day) {
		Calendar startDate = new GregorianCalendar();
		startDate.setTimeInMillis(sprintItem.getStartDate().getTimeInMillis());
	
		startDate.add(Calendar.DAY_OF_YEAR, day);
		String date = startDate.get(Calendar.YEAR) + "-"
		+ startDate.get(Calendar.MONTH) + "-"
		+ startDate.get(Calendar.DAY_OF_MONTH);
		
		startDate.add(Calendar.DAY_OF_YEAR, -1);
		String previosDate = startDate.get(Calendar.YEAR) + "-"
		+ startDate.get(Calendar.MONTH) + "-"
		+ startDate.get(Calendar.DAY_OF_MONTH);
		
		System.out.println("Date: " + date);
		
		double amount = 0;
		
		for(ProductBacklogItem pbItem : sprintItem.getStorries()) {
			for(SprintBacklogItem sbItem: pbItem.getTasks()) {
				//Add the initial entry to the burdown
				if(day == 0 && !sbItem.getBurnDown().containsKey(date)) {
					sbItem.getBurnDown().put(date, sbItem.getOpenEstimate());
				}
				else if(!sbItem.getBurnDown().containsKey(date) || (sbItem.getBurnDown().containsKey(previosDate) && sbItem.getBurnDown().get(previosDate) < sbItem.getBurnDown().get(date)))
					sbItem.getBurnDown().put(date, sbItem.getBurnDown().get(previosDate));
				if(sbItem.getBurnDown().containsKey(date))
					amount += sbItem.getBurnDown().get(date);
				else {

					amount += sbItem.getOpenEstimate();
				}
				
				System.out.println("Day: " + day + ", Amount: " + amount + ", Date: " + date
						+ ", Item " + sbItem.getDescription() + "OpenE: "
						+ sbItem.getBurnDown().get(date) + ", COpenE: "
						+ sbItem.getOpenEstimate());
			}
		}
		
		return amount;
	}

	private long dayOfTime(Calendar date) {
		long startTime = sprintItem.getStartDate().getTimeInMillis();
		long duration = date.getTimeInMillis() - startTime;
		long dayNr = duration / (1000 * 60 * 60 * 24);
		return dayNr;
	}

	/**Sets the component Object.
	 * @param component
	 */
	public void setComponent(IComponent component) {
		this.component = component;
	}

	/**Gets the component Object.
	 * @return IComponent
	 */
	public IComponent getComponent() {
		return component;
	}

}
