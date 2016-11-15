package org.cheetahplatform.modeler.understandability;

import static org.cheetahplatform.modeler.ModelerConstants.ATTRIBUTE_EXPERIMENTAL_WORKFLOW_ACTIVITY_DURATION;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.modeler.Activator;
import org.cheetahplatform.modeler.Perspective;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.ResourceManager;

public class UnderstandabilityActivitiy extends AbstractExperimentsWorkflowActivity {
	private class DismissQuestionListener implements Runnable {
		@Override
		public void run() {
			setFinished(true);
		}
	}

	private String imageStr;
	private Image image;
	private SurveyAttribute question;
	private List<Attribute> additionalData;
	private String hint;
	private final String headerText;

	private UnderstandabilityView understandabilityView;
	private final boolean showProgress;

	public UnderstandabilityActivitiy(Image image, SurveyAttribute question, List<Attribute> additionalData) {
		this("", question, additionalData, null, null);
		this.image = image;
	}

	public UnderstandabilityActivitiy(String id, String image, SurveyAttribute question, List<Attribute> additionalData) {
		this(id, image, question, additionalData, null, null, true);
	}

	public UnderstandabilityActivitiy(String id, String image, SurveyAttribute question, List<Attribute> additionalData,
			boolean showProgress) {
		this(id, image, question, additionalData, null, null, showProgress);
	}

	public UnderstandabilityActivitiy(String id, String image, SurveyAttribute question, List<Attribute> additionalData, String hint,
			String headerText, boolean showProgress) {
		super(id);

		this.imageStr = image;
		this.question = question;
		this.additionalData = additionalData;
		this.hint = hint;
		this.headerText = headerText;
		this.showProgress = showProgress;
	}

	public UnderstandabilityActivitiy(String image, SurveyAttribute question, List<Attribute> additionalData) {
		this(image, question, additionalData, null, null);
	}

	public UnderstandabilityActivitiy(String image, SurveyAttribute question, List<Attribute> additionalData, String hint, String headerText) {
		this("UNDERSTANDABILITY", image, question, additionalData, hint, headerText, true);
	}

	@Override
	protected void doExecute() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(UnderstandabilityPerspective.ID);
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspective);

		ImageView imageView = (ImageView) activePage.findView(ImageView.ID);

		if (image == null) {
			image = ResourceManager.getPluginImage(Activator.getDefault(), imageStr);
		}

		imageView.setImage(image);

		understandabilityView = (UnderstandabilityView) activePage.findView(UnderstandabilityView.ID);
		understandabilityView.setQuestion(question);
		understandabilityView.setHint(hint);
		understandabilityView.addDismissListener(new DismissQuestionListener());
		understandabilityView.setHeaderText(headerText);
		understandabilityView.setShowProgress(showProgress);

		setFinished(false);
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.addAll(additionalData);
		data.addAll(understandabilityView.collectInput());

		String modelName = null;
		for (Attribute attribute : additionalData) {
			if (attribute.getName().equals("model name")) {
				modelName = attribute.getContent();
				break;
			}
		}

		if (modelName != null) {
			List<Attribute> prefixed = new ArrayList<Attribute>();
			for (Attribute attribute : data) {
				if (attribute.getName().startsWith(question.getName())) {
					prefixed.add(new Attribute(modelName + " " + attribute.getName(), attribute.getContent()));
				} else if (attribute.getName().startsWith(ATTRIBUTE_EXPERIMENTAL_WORKFLOW_ACTIVITY_DURATION)) {
					prefixed.add(new Attribute(modelName + " " + question.getName() + " " + attribute.getName(), attribute.getContent()));
				} else {
					prefixed.add(attribute);
				}
			}
			return prefixed;
		}

		return data;
	}

	/**
	 * Returns the hint.
	 * 
	 * @return the hint
	 */
	public String getHint() {
		return hint;
	}

	@Override
	public Object getName() {
		return getId() + " Question";
	}

	public boolean hasHint() {
		return hint != null;
	}

	@Override
	protected void postExecute() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.setPerspective(PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(Perspective.ID));
	}

}
