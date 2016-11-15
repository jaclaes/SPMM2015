package at.component.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import at.component.event.EventDefinition;
import at.component.event.EventPropertyDefinition;
import at.component.event.IEventInformation;

public class EventInformationComposite extends Composite {

	public EventInformationComposite(Composite parent, int style, IEventInformation eventInformation) {
		super(parent, style);

		this.setLayout(new GridLayout());
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createUi(eventInformation);
	}

	private Control createEventDefinitionComposite(EventDefinition eventDefinition, TabFolder eventDefinitionsTabFolder) {
		Composite eventDefinitionComposite = new Composite(eventDefinitionsTabFolder, SWT.NONE);
		eventDefinitionComposite.setLayout(new GridLayout(2, false));
		eventDefinitionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		eventDefinitionComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		eventDefinitionComposite.setBackgroundMode(SWT.INHERIT_FORCE);

		Label eventNameLabel = new Label(eventDefinitionComposite, SWT.NONE);
		eventNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
		eventNameLabel.setText(eventDefinition.getEventName());
		eventNameLabel.setFont(new Font(eventNameLabel.getDisplay(), "Verdana", 16, SWT.BOLD));

		Label descriptionLabel = new Label(eventDefinitionComposite, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
		descriptionLabel.setText(eventDefinition.getEventDescription());

		Label spacer1 = new Label(eventDefinitionComposite, SWT.NONE);
		spacer1.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));

		Label topicLabel = new Label(eventDefinitionComposite, SWT.NONE);
		topicLabel.setText("Topic:");
		topicLabel.setFont(new Font(topicLabel.getDisplay(), "Verdana", Display.getDefault().getSystemFont().getFontData()[0].getHeight(),
				SWT.BOLD));

		Label topicStringLabel = new Label(eventDefinitionComposite, SWT.NONE);
		topicStringLabel.setText("\"" + eventDefinition.getEventTopic() + "\"");

		Label spacer2 = new Label(eventDefinitionComposite, SWT.NONE);
		spacer2.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));

		CTabFolder eventPropertiesTabFolder = new CTabFolder(eventDefinitionComposite, SWT.BORDER);
		eventPropertiesTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		eventDefinitionsTabFolder.setBackgroundMode(SWT.INHERIT_FORCE);

		int counter = 1;

		for (EventPropertyDefinition eventPropertyDefinition : eventDefinition.getEventProperties()) {
			CTabItem eventPropertyTabItem = new CTabItem(eventPropertiesTabFolder, SWT.NONE);
			eventPropertyTabItem.setText("Property " + counter++);
			eventPropertyTabItem.setControl(createEventPropertyDefinitionComposite(eventPropertyDefinition, eventPropertiesTabFolder));
		}

		if (eventPropertiesTabFolder.getItemCount() > 0)
			eventPropertiesTabFolder.setSelection(0);

		return eventDefinitionComposite;
	}

	private Control createEventPropertyDefinitionComposite(EventPropertyDefinition eventPropertyDefinition,
			CTabFolder eventPropertiesTabFolder) {
		Composite eventPropertyDefinitionComposite = new Composite(eventPropertiesTabFolder, SWT.NONE);
		eventPropertyDefinitionComposite.setLayout(new GridLayout(2, false));
		eventPropertyDefinitionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label nameLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
		nameLabel.setText(eventPropertyDefinition.getPropertyName());
		nameLabel.setFont(new Font(nameLabel.getDisplay(), "Verdana", 11, SWT.BOLD));

		Label descriptionLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
		descriptionLabel.setText(eventPropertyDefinition.getPropertyDescription());

		Label spacer1 = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		spacer1.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));

		Label keyLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		keyLabel.setText("Key:");
		keyLabel.setFont(new Font(keyLabel.getDisplay(), "Verdana", Display.getDefault().getSystemFont().getFontData()[0].getHeight(),
				SWT.BOLD));

		Label keyStringLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		keyStringLabel.setText("\"" + eventPropertyDefinition.getPropertyKey() + "\"");

		Label defaultValueLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		defaultValueLabel.setText("Default Value:");
		defaultValueLabel.setFont(new Font(defaultValueLabel.getDisplay(), "Verdana", Display.getDefault().getSystemFont().getFontData()[0]
				.getHeight(), SWT.BOLD));

		Label defaultValueStringLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		defaultValueStringLabel.setText("\"" + eventPropertyDefinition.getPropertyDefaultValue() + "\"");

		Label typeLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		typeLabel.setText("Type:");
		typeLabel.setFont(new Font(typeLabel.getDisplay(), "Verdana", Display.getDefault().getSystemFont().getFontData()[0].getHeight(),
				SWT.BOLD));

		Label typeStringLabel = new Label(eventPropertyDefinitionComposite, SWT.NONE);
		typeStringLabel.setText("\"" + eventPropertyDefinition.getPropertyTypeAsString() + "\"");

		return eventPropertyDefinitionComposite;
	}

	private Control createUi(IEventInformation eventInformation) {
		TabFolder eventDefinitionsTabFolder = new TabFolder(this, SWT.NONE);
		eventDefinitionsTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		int counter = 1;

		for (EventDefinition eventDefinition : eventInformation.getEventDefinitions()) {
			TabItem eventDefinitionTabItem = new TabItem(eventDefinitionsTabFolder, SWT.NONE);
			eventDefinitionTabItem.setText("Event " + counter++);
			eventDefinitionTabItem.setControl(createEventDefinitionComposite(eventDefinition, eventDefinitionsTabFolder));
		}

		return eventDefinitionsTabFolder;
	}
}