package org.cheetahplatform.experiment.editor.prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alaskasimulator.config.composite.SurveyCreationComposite;
import org.alaskasimulator.config.dialog.SurveyComboInputAttributeEditDialog;
import org.alaskasimulator.config.dialog.TextAttributeInputDialog;
import org.alaskasimulator.config.rcp.ConfigResourceRegistry;
import org.cheetahplatform.experiment.editor.Activator;
import org.cheetahplatform.survey.core.AbstractChoiceInputAttribute;
import org.cheetahplatform.survey.core.ComboInputAttribute;
import org.cheetahplatform.survey.core.IntegerInputAttribute;
import org.cheetahplatform.survey.core.MultiLineStringInputAttribute;
import org.cheetahplatform.survey.core.StringInputAttribute;
import org.cheetahplatform.survey.core.Survey;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.cheetahplatform.survey.core.TextInputAttribute;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.thoughtworks.xstream.XStream;

public class SurveyDialog extends Dialog {
	
	private static final String ERR1 = "Could not save choices config file.";

	private Survey survey;
	private TableViewer attributesTable;
	private SurveyCreationComposite composite;
	private Map<String, List<String>> choices;
	private static final String CHOICES_CONFIG = "choices-config.xml";
	
	private String title = "Survey Creation";
	private String subTitle = "Survey";
	private String description = "Add questions to the survey";

	public SurveyDialog(Shell parentShell, Survey survey) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.SHELL_TRIM);
		this.survey = survey;
	}
	
	public SurveyDialog(Shell parentShell, Survey survey, String title, String subtitle, String description) {
		this(parentShell, survey);
		this.title = title;
		this.subTitle = subtitle;
		this.description = description;
	}

	public Survey getSurvey() {
		return survey;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		ManagedForm managedForm = new ManagedForm(container);
		ScrolledForm form = managedForm.getForm();
		form.setText(title);
		form.setImage(ConfigResourceRegistry
				.getImage(ConfigResourceRegistry.IMAGE_GENERAL_INFO));
		managedForm.getToolkit().decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.setLayout(new GridLayout());
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		form.getBody().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		composite = new SurveyCreationComposite(managedForm, subTitle, description) {
			@Override
			protected void createTopComposite(Composite parent) {
				//remove top composite; so do nothing
			}
			
		};
				
		attributesTable = new TableViewer(composite.getAttributesTable());

		loadChoices();
		fillUi();
		addListener();

		return form;
	}
	
	private void addListener() {
		composite.getComboMenuItem().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addComboAttribute();
					}
				});
		composite.getTextMenuItem().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addAttribute(new StringInputAttribute("", false));
					}
				});
		composite.getIntegerMenuItem().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addAttribute(new IntegerInputAttribute("", false, 0,
								Integer.MAX_VALUE));
					}
				});
		composite.getMultiLineTextMenuItem().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addAttribute(new MultiLineStringInputAttribute("", true));
					}
				});
		composite.getEditButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editAttribute();
			}
		});
		attributesTable.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				editAttribute();
			}
		});
		composite.getRemoveButton().addSelectionListener(
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						removeAttribute();
					}
				});
		composite.getUpButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveAttribute(-1);
			}
		});

		composite.getDownButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveAttribute(1);
			}
		});
	}
	
	/**
	 * Moves the currently selected element by the given delta.
	 * 
	 * @param delta
	 *            the delta - negative numbers to move towards the beginning of
	 *            the list, positive for moving backwards
	 */
	protected void moveAttribute(int delta) {
		IStructuredSelection selection = (IStructuredSelection) attributesTable
				.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(composite.getShell(), "No Selection",
					"Please select the attribute to move.");
			return;
		}

		SurveyAttribute attribute = (SurveyAttribute) selection
				.getFirstElement();
		List<SurveyAttribute> attributes = getSurvey().getAttributes();
		int newIndex = attributes.indexOf(attribute) + delta;
		if (newIndex < 0 || newIndex >= attributes.size()) {
			return;
		}

		getSurvey().removeAttribute(attribute);
		getSurvey().addAttribute(newIndex, attribute);

		attributesTable.setInput(getSurvey().getAttributes());
	}
	
	/**
	 * Remove the currently selected attribute.
	 */
	protected final void removeAttribute() {
		IStructuredSelection selection = (IStructuredSelection) attributesTable
				.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(composite.getShell(), "No Selection",
					"Please select the attribute to remove.");
			return;
		}

		SurveyAttribute attribute = (SurveyAttribute) selection
				.getFirstElement();
		getSurvey().removeAttribute(attribute);
		attributesTable.setInput(getSurvey().getAttributes());
	}
	
	/**
	 * Edits the currently selected attribute.
	 */
	protected final void editAttribute() {
		IStructuredSelection selection = (IStructuredSelection) attributesTable
				.getSelection();
		if (selection.isEmpty()) {
			MessageDialog.openError(composite.getShell(), "No Selection",
					"Please select the attribute to edit.");
			return;
		}

		SurveyAttribute attribute = (SurveyAttribute) selection
				.getFirstElement();
		boolean refresh = false;

		if (attribute instanceof ComboInputAttribute) {
			refresh = new SurveyComboInputAttributeEditDialog(
					composite.getShell(),
					(AbstractChoiceInputAttribute) attribute,
					getSurvey(), getChoices()).open() == Window.OK;
		} else {
			TextAttributeInputDialog dialog = new TextAttributeInputDialog(
					composite.getShell(), "Edit Attribute",
					"Enter the new name for the attribute",
					attribute.getName(), new QuestionTextValidator(attribute),
					attribute.isMandatory());
			if (dialog.open() == Window.OK) {
				attribute.setName(dialog.getValue());
				attribute.setMandatory(dialog.isMandatory());
				refresh = true;
			}

		}

		if (refresh) {
			attributesTable.refresh();
		}
	}
	
	/**
	 * Adds a new attribute.
	 * 
	 * @param attribute
	 *            the attribute
	 */
	protected final void addAttribute(TextInputAttribute attribute) {
		TextAttributeInputDialog dialog = new TextAttributeInputDialog(
				composite.getShell(), "New Question",
				"Enter the question text", "", new QuestionTextValidator(),
				false);
		if (dialog.open() == Window.OK) {
			String name = dialog.getValue();
			attribute.setName(name);
			attribute.setMandatory(dialog.isMandatory());
			getSurvey().addAttribute(attribute);

			attributesTable.setInput(getSurvey().getAttributes());
		}
	}
	
	/**
	 * Adds a combo attribute.
	 */
	protected final void addComboAttribute() {
		AbstractChoiceInputAttribute attribute = new ComboInputAttribute("",
				false);
		SurveyComboInputAttributeEditDialog dialog = new SurveyComboInputAttributeEditDialog(
				composite.getShell(), attribute, getSurvey(), getChoices());

		if (dialog.open() == Window.OK) {
			getSurvey().addAttribute(attribute);
			attributesTable.setInput(getSurvey().getAttributes());
			addChoices(attribute.getChoices());
		}
	}
	
	private void addChoices(List<String> choices) {
		for (List<String> containedChoices : getChoices().values()) {
			if (containedChoices.size() == choices.size()
					&& containedChoices.containsAll(choices))
				return;
		}

		String key = null;
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			key = "set " + i;
			if (!getChoices().keySet().contains(key)) {
				getChoices().put(key, choices);
				return;
			}
		}
	}

	private void fillUi() {
		attributesTable.setContentProvider(new ArrayContentProvider());
		attributesTable.setLabelProvider(new AttributeLabelProvider());
		attributesTable.setInput(getSurvey().getAttributes());
	}

	private static class AttributeLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			SurveyAttribute attribute = (SurveyAttribute) element;

			if (columnIndex == 0) {
				return attribute.getName();
			}
			if (columnIndex == 1) {
				return attribute.getType();
			}

			if (columnIndex == 2) {
				if (attribute.isMandatory()) {
					return "yes";
				}

				return "no";
			}

			return null;
		}

	}
	
	private class QuestionTextValidator implements IInputValidator {

		private final SurveyAttribute attribute;

		public QuestionTextValidator() {
			this.attribute = null;
		}

		public QuestionTextValidator(SurveyAttribute attribute) {
			this.attribute = attribute;
		}

		@Override
		public String isValid(String newText) {
			if (newText.trim().isEmpty()) {
				return "Enter a text";
			} else if (getSurvey().isAttributeNameInUse(newText.trim(),
					attribute)) {
				return "The text \"" + newText.trim() + "\" is already in use";
			}

			return null;
		}
	}
	
	public Map<String, List<String>> getChoices() {
		return choices;
	}
	
	public static XStream createXStream() {
		XStream xStream = new XStream();
		xStream.setClassLoader(Activator.class.getClassLoader());
		return xStream;
	}
	
	@SuppressWarnings("unchecked")
	private void loadChoices() {
		InputStream inputStream = null;
		try {
			XStream xStream = createXStream();
			inputStream = new FileInputStream(getChoiceConfigFile());
			choices = (Map<String, List<String>>) xStream.fromXML(inputStream);
		} catch (Exception e) {
			//do nothing
		} finally {
			try {
				if (choices == null)
					choices = new HashMap<String, List<String>>();
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				//do nothing
			}
		}
	}
	
	
	
	@Override
	public boolean close() {
		boolean result = super.close();
		try {
			saveChoices();
		} catch (IOException ex) {
			Activator.log(IStatus.ERROR, 
					 ERR1, ex);
		}
		return result;
	}

	private void saveChoices() throws IOException {
		XStream xStream = createXStream();
		FileOutputStream out = new FileOutputStream(getChoiceConfigFile());
		xStream.toXML(choices, out);
		out.close();
    }

	private File getChoiceConfigFile() {
		try {
			IPath stateLocation = Activator.getDefault().getStateLocation().append(CHOICES_CONFIG);
			File file = new File(stateLocation.toOSString());
			if (!file.exists())
				file.createNewFile();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
