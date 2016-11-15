package at.floxx.scrumify.sprintList.providers;

import java.util.Calendar;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.floxx.scrumify.coreObjectsProvider.core.Sprint;

/**Sprint Label Provider
 * @author Mathias Breuss
 *
 */
public class SprintLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Sprint item = (Sprint) element;
		switch (columnIndex) {
		case 0:
			return item.getGoal();
		case 1:
			return item.getComments();
		case 2:
			return item.getSpeed() + "";
		case 3: 
		{
			Calendar d = item.getStartDate();
			return d.get(Calendar.DAY_OF_MONTH) + "." + (d.get(Calendar.MONTH)+1) + "." + d.get(Calendar.YEAR);
		}
		case 4:
			Calendar d = item.getEndDate();
			return d.get(Calendar.DAY_OF_MONTH) + "." + (d.get(Calendar.MONTH)+1) + "." + d.get(Calendar.YEAR);
		default:
			throw new RuntimeException(
					"Something wired happened in SprintLabelProvider");

		}
	}

}
