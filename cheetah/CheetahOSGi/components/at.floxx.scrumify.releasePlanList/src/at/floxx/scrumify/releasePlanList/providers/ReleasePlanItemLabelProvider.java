package at.floxx.scrumify.releasePlanList.providers;

import java.util.Calendar;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.floxx.scrumify.coreObjectsProvider.core.ReleasePlan;



/**Labelprovider.
 * @author Mathias Breuss
 *
 */
public class ReleasePlanItemLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ReleasePlan item = (ReleasePlan) element;
		switch (columnIndex) {
		case 0:
			return item.getProjectName();
		case 1:
			return item.getReleaseGoalDescription();
		case 2: 
		{
			Calendar d = item.getStartDate();
			return d.get(Calendar.DAY_OF_MONTH) + "." + (d.get(Calendar.MONTH)+1) + "." + d.get(Calendar.YEAR);
		}
		case 3:
			Calendar d = item.getEndDate();
			return d.get(Calendar.DAY_OF_MONTH) + "." + (d.get(Calendar.MONTH)+1) + "." + d.get(Calendar.YEAR);
		default:
			throw new RuntimeException(
					"Something wired happened in ProductBacklogItemLabelProvider");

		}
	}

}
