package at.floxx.scrumify.coreObjectsProvider;


import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.wireadmin.Wire;

import at.component.IComponent;
import at.component.IComponentUiController;
import at.floxx.scrumify.coreObjectsProvider.fileselector.FileSelectorComposite;
import at.floxx.scrumify.coreObjectsProvider.persistency.FilePersistency;
import at.floxx.scrumify.coreObjectsProvider.service.ObjectProviderService;

/**The ComponentUiController.
 * @author Mathias Breuss
 *
 */
public class ComponentUiController implements IComponentUiController {

	private Composite parent;
	@SuppressWarnings("unused")
	private IComponent component;
	private Text pathText;

	/**The Constructor.
	 * @param parent
	 * @param component
	 */
	public ComponentUiController(Composite parent, IComponent component) {
		this.parent = parent;
		this.component = component;
		
		initGUI();

	}
	
	private void initGUI() {
		FileSelectorComposite fileSelector = new FileSelectorComposite(parent, SWT.NONE);
		Button loadButton = fileSelector.getLoadButton();
		Button saveButton = fileSelector.getSaveButton();
		Button browseButton = fileSelector.getBrowseButton();
		pathText = fileSelector.getPathText();
		
		browseButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell(), SWT.SAVE);
				dialog.setFilterExtensions(new String[] {"*.scru"});
				String result = dialog.open();
				if(result != null && !result.isEmpty())
				{
					if(!result.contains(".scru"))
						result += ".scru";
					pathText.setText(result);
				}
			}});
		
		loadButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Activator.getObjectProviderService();
					ObjectProviderService obs = (ObjectProviderService) FilePersistency.read(pathText.getText());
					ObjectProviderService.setObjectProviderService(obs);
					Activator.reregisterObjectProvider();
					reloadItemList();
				} catch (IOException e1) {
					showReadErrorMsgBox();
				} catch (ClassNotFoundException e1) {
					showReadErrorMsgBox();
				}		
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		saveButton.addSelectionListener(new SelectionListener() {
			
			private ObjectProviderService obs;

			@Override
			public void widgetSelected(SelectionEvent e) {
				Activator.getObjectProviderService();
				obs = ObjectProviderService.getInstance();
				try {
					FilePersistency.save(obs, pathText.getText());
				} catch (IOException e1) {
					showWriteErrorMsgBox();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});


	}
	
	
	private void showReadErrorMsgBox() {
		MessageBox dialog = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
		dialog.setMessage("Sorry, there was an error reading that file!");
		dialog.open();
	}
	
	private void showWriteErrorMsgBox() {
		MessageBox dialog = new MessageBox(parent.getShell(), SWT.OK | SWT.ICON_ERROR);
		dialog.setMessage("Sorry, there was an error writing that file!");
		dialog.open();
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		// TODO: OPTIONAL Implement this method if you want to send data to another component
		// Knowledge about the OSGi WireAdmin Service is required
	}

	/**Returns Data used to persist.
	 * @return HashMap
	 */
	public HashMap<String, String> getData() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("filePath", pathText != null ? pathText.getText() : "");
		return data;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO: OPTIONAL Implement this method if you want to handle events
		// Knowledge about the OSGi EventAdmin Service is required
	}

	/**Puts back data.
	 * @param data
	 */
	public void initialize(HashMap<String, String> data) {
		if(data == null)
			return;
		String path = data.get("filePath");
		if(path != null && !path.isEmpty())
			pathText.setText(path);
	}

	@Override
	public Object polled(Wire wire) {
		// TODO: OPTIONAL Implement this method if you want to send data to another component
		// Knowledge about the OSGi WireAdmin Service is required
		return null;
	}

	@Override
	public void producersConnected(Wire[] wires) {
		// TODO: OPTIONAL Implement this method if you want to receive data from data producers
		// Knowledge about the OSGi WireAdmin Service is required
	}

	@Override
	public void updated(Wire wire, Object value) {
		// TODO: OPTIONAL Implement this method if you want to receive data from data producers
		// Knowledge about the OSGi WireAdmin Service is required
	}
	
	private void reloadItemList() {
		Dictionary<String, String> prop = new Hashtable<String,String>();
		prop.put(EventConstants.MESSAGE, "reloadObjectProvider");
		Event relaodListEvent = new Event("at/floxx/scrumify/coreObjectProvider/Event", prop);
		Activator.getEventAdmin().sendEvent(relaodListEvent);
	}
}
