package at.floxx.scrumify.sprintEditForm;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.event.Event;

import org.osgi.service.event.EventConstants;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.coreObjectsProvider.core.Sprint;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements IComponentUiController {

	private ReleasePlanEditFormComposite productBacklogItemEditFormComposite;
	private ButtonComposite buttonComposite;
	private InputFieldsComposite inputFieldsComposite;
	private Button updateButton;
	private Button newButton;
	private Button cancelButton;
	private Text nameText;
	private Text descriptionText;

	private Sprint item;
	private Wire[] wires;
	private IComponent component;
	private Button deleteButton;
	private DateFromToComposite dateFromToComposite;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private Text speedText;
	private Composite parent;
	
	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		FillLayout layout = new FillLayout();
		parent.setLayout(layout);
		this.setComponent(component);
		this.parent = parent;

		initGUI();
	}

	private void initGUI() {
		productBacklogItemEditFormComposite = new ReleasePlanEditFormComposite(
				parent, SWT.NONE);

		// Get references to controls for modification
		buttonComposite = productBacklogItemEditFormComposite
				.getButtonComposite();
		inputFieldsComposite = productBacklogItemEditFormComposite
				.getInputFieldsComposite();
		dateFromToComposite = productBacklogItemEditFormComposite
				.getDateFromToComposite();

		updateButton = this.buttonComposite.getUpdateButton();
		newButton = this.buttonComposite.getNewButton();
		cancelButton = this.buttonComposite.getCancelButton();
		deleteButton = this.buttonComposite.getDeleteButton();

		nameText = this.inputFieldsComposite.getNameFieldText();
		descriptionText = this.inputFieldsComposite.getDescriptionText();

		startDateTime = dateFromToComposite.getStartDateTime();
		endDateTime = dateFromToComposite.getEndDateTime();

		speedText = this.inputFieldsComposite.getSpeedText();

		setEnableButton(false);

		updateButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (item != null)
					try {
						item.setGoal((nameText.getText()));
						item.setComments((descriptionText.getText()));
						item.setSpeed(Integer.parseInt(speedText.getText()));

						GregorianCalendar start = new GregorianCalendar();
						start.set(startDateTime.getYear(), startDateTime
								.getMonth(), startDateTime.getDay());

						GregorianCalendar end = new GregorianCalendar();
						end.set(endDateTime.getYear(), endDateTime.getMonth(),
								endDateTime.getDay());

						if (start.after(end)) {
							showDateErrorMsgBox();
							return;
						}

						item.setStartDate(start);
						item.setEndDate(end);

						reloadItemList();
					} catch (NumberFormatException ex) {
						showInputNumberErrorMsgBox();
					}

			}
		});

		newButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				item = Activator.getObjectProviderService().createSprint(
						nameText.getText(), descriptionText.getText(),
						Integer.parseInt(speedText.getText()));

				GregorianCalendar start = new GregorianCalendar();
				start.set(startDateTime.getYear(), startDateTime.getMonth(),
						startDateTime.getDay());

				GregorianCalendar end = new GregorianCalendar();
				end.set(endDateTime.getYear(), endDateTime.getMonth(),
						endDateTime.getDay());

				if (start.after(end)) {
					showDateErrorMsgBox();
					return;
				}

				item.setStartDate(start);
				item.setEndDate(end);

				update();
				clearForm();
			}

		});

		cancelButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				item = null;
				clearForm();

			}
		});

		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				clearForm();
				deleteItem();
			}
		});

		nameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setEnableButton(true);
			}
		});

		descriptionText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setEnableButton(true);
			}
		});
	}

	private void showInputNumberErrorMsgBox() {
		MessageBox dialog = new MessageBox(parent.getShell(), SWT.OK
				| SWT.ICON_ERROR);
		dialog.setMessage("Value provided is not a Number!");
		dialog.open();
	}

	private void clearForm() {
		nameText.setText("");
		descriptionText.setText("");
		speedText.setText("0");
		// Disable Buttons
		setEnableButton(false);
	}

	private void showDateErrorMsgBox() {
		MessageBox dialog = new MessageBox(parent.getShell(), SWT.OK
				| SWT.ICON_ERROR);
		dialog.setMessage("Start Date is after End Date!");
		dialog.open();
	}

	private void updateForm(Sprint item) {
		this.item = item;
		this.nameText.setText(item.getGoal());
		this.descriptionText.setText(item.getComments());
		this.speedText.setText(item.getSpeed() + "");

		this.startDateTime.setDate(item.getStartDate().get(Calendar.YEAR), item
				.getStartDate().get(Calendar.MONTH), item.getStartDate().get(
				Calendar.DAY_OF_MONTH));
		this.endDateTime.setDate(item.getEndDate().get(Calendar.YEAR), item
				.getEndDate().get(Calendar.MONTH), item.getEndDate().get(
				Calendar.DAY_OF_MONTH));

		// Enable Buttons
		setEnableButton(true);
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		if (wires != null) {
			for (int i = 0; i < wires.length; i++) {
				wires[i].update(polled(wires[i]));
			}
			this.wires = wires;
		}
	}

	/**Returns Data used to persist.
	 * @return HashMap
	 */
	public HashMap<String, String> getData() {
		// TODO: OPTIONAL Return the data which is needed to initialize the
		// component at start-up
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.getProperty(EventConstants.MESSAGE).equals(
				"reloadObjectProvider")) {
			Activator.loadObjectProvider();
		}
	}

	/**Puts back data.
	 * @param data
	 */
	public void initialize(HashMap<String, String> data) {
		// TODO: OPTIONAL Initialize the component with the given data (that was
		// returned by the "getData()"-method)
	}

	@Override
	public Object polled(Wire wire) {
		return this.item;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		if (wires == null) {
			System.out.println("Not connected to any wires");
		} else {
			System.out.println("Connected to " + wires.length + " wires");
		}
	}

	@Override
	public void updated(Wire wire, Object value) {
		Sprint item = (Sprint) value;
		updateForm(item);
	}

	private void update() {
		if (wires != null && wires.length > 0) {
			for (Wire w : wires) {
				w.update(polled(w));
			}
		}
	}

	private void setEnableButton(boolean value) {
		updateButton.setEnabled(value);
		newButton.setEnabled(value);
		deleteButton.setEnabled(value);
	}

	private void reloadItemList() {
		Dictionary<String, String> prop = new Hashtable<String, String>();
		prop.put(EventConstants.MESSAGE, "reload");
		Event relaodItemListEvent = new Event(
				"at/floxx/scrumify/sprintEditForm/Event", prop);
		Activator.getEventAdmin().sendEvent(relaodItemListEvent);
	}

	private void deleteItem() {
		Dictionary<String, String> prop = new Hashtable<String, String>();
		prop.put(EventConstants.MESSAGE, "delete");
		Event relaodItemListEvent = new Event(
				"at/floxx/scrumify/sprintEditForm/Event", prop);
		Activator.getEventAdmin().sendEvent(relaodItemListEvent);
	}

	/**
	 * @param component
	 */
	public void setComponent(IComponent component) {
		this.component = component;
	}

	/**
	 * @return component
	 */
	public IComponent getComponent() {
		return component;
	}
}
