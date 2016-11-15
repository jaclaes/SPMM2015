package org.cheetahplatform.modeler.understandability;

import static org.cheetahplatform.modeler.configuration.IConfiguration.ASK_FOR_UNDERSTANDABILITY_EXPLANATION;

import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.Messages;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.shared.ListenerList;
import org.cheetahplatform.survey.controller.AbstractButtonChoiceController;
import org.cheetahplatform.survey.controller.AttributeControllerFactory;
import org.cheetahplatform.survey.controller.IAttributeController;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class UnderstandabilityView extends ViewPart {

	private class QuestionListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			// ignore
		}
	}

	private class WorkflowStateListener implements Runnable {
		@Override
		public void run() {
			updateProgress();
		}

	}

	public static final String ID = "org.cheetahplatform.modeler.understandability.UnderstandabilityView"; //$NON-NLS-1$

	public static final String EXPLANATION = "explanation";
	private UnderstandabilityViewComposite composite;
	private ListenerList dismissListeners;
	private IAttributeController controller;
	private String hint;

	public void addDismissListener(Runnable listener) {
		dismissListeners.add(listener);
	}

	public List<Attribute> collectInput() {
		List<Attribute> attributeInput = controller.collectInput();
		if (CheetahPlatformConfigurator.getBoolean(IConfiguration.ASK_FOR_UNDERSTANDABILITY_EXPLANATION)) {
			attributeInput.add(new Attribute(EXPLANATION, composite.getExplanationText().getText()));
		}

		return attributeInput;
	}

	@Override
	public void createPartControl(Composite parent) {
		composite = new UnderstandabilityViewComposite(parent, SWT.NONE);
		dismissListeners = new ListenerList();

		composite.getDismissButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dimissQuestion();
			}
		});

		composite.getHintButton().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						Messages.UnderstandabilityView_1, hint);
			}
		});

		ExperimentalWorkflowEngine.addListener(new WorkflowStateListener());
	}

	protected void dimissQuestion() {
		if (controller.validate() != null) {
			MessageDialog.openInformation(composite.getShell(), Messages.UnderstandabilityView_2, Messages.UnderstandabilityView_3);
			return;
		}

		boolean explanationRequired = CheetahPlatformConfigurator.getBoolean(ASK_FOR_UNDERSTANDABILITY_EXPLANATION);
		boolean noExplanationGiven = composite.getExplanationText().getText().trim().isEmpty();
		if (explanationRequired && noExplanationGiven) {
			MessageDialog.openInformation(composite.getShell(), "Explanation Missing", "Please add a short explanation for your answer.");
			return;
		}

		for (Object listener : dismissListeners.getListeners()) {
			((Runnable) listener).run();
		}
	}

	public void removeDismissListener(Runnable listener) {
		dismissListeners.remove(listener);
	}

	@Override
	public void setFocus() {
		// ignore
	}

	public void setHeaderText(String headerText) {
		if (headerText == null) {
			headerText = Messages.UnderstandabilityView_4;
		}

		composite.getHeaderLabel().setText(headerText);
		composite.layout(true, true);
	}

	public void setHint(String hint) {
		composite.getHintButton().setVisible(hint != null);
		this.hint = hint;
	}

	public void setQuestion(SurveyAttribute question) {
		for (Control child : composite.getQuestionComposite().getChildren()) {
			child.dispose();
		}

		controller = AttributeControllerFactory.createController(composite.getQuestionComposite(), question, new QuestionListener(), null,
				800);
		composite.layout(true, true);
		composite.getExplanationText().setText("");
	}

	public void setShowProgress(boolean showProgress) {
		composite.getProgressLabel().setVisible(showProgress);
		composite.getProgressBar().setVisible(showProgress);
		composite.layout(true, true);
	}

	public void setStyleRanges(StyleRange[] styleRanges) {
		if (controller instanceof AbstractButtonChoiceController) {
			((AbstractButtonChoiceController) controller).setStyleRanges(styleRanges);
		} else {
			MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.UnderstandabilityView_5,
					"Styleranges are only supported for class AbstractButtonChoiceController."); //$NON-NLS-1$
		}
	}

	public void updateProgress() {
		ExperimentalWorkflowEngine engine = ExperimentalWorkflowEngine.getEngine();

		composite.getProgressBar().setMaximum(engine.getActivityCount());
		composite.getProgressBar().setSelection(engine.getCurrentActivityIndex());
	}
}
