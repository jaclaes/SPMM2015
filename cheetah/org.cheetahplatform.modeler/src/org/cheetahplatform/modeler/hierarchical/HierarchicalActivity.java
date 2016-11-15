package org.cheetahplatform.modeler.hierarchical;

import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.common.Activator;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AbstractPromLogger;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.Perspective;
import org.cheetahplatform.modeler.engine.AbstractExperimentsWorkflowActivity;
import org.cheetahplatform.modeler.engine.ExperimentalWorkflowEngine;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.understandability.UnderstandabilityView;
import org.cheetahplatform.survey.core.SurveyAttribute;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class HierarchicalActivity extends AbstractExperimentsWorkflowActivity {

	private class LogListener implements ILogListener {

		@Override
		public void log(AuditTrailEntry entry) {
			getLogger().append(entry);
		}

	}

	public static final String HIERARCHICAL = "HIERARCHICAL";

	private Process process;
	private OutlineViewNode<Graph> mainNode;
	private SurveyAttribute question;
	private String hint;
	private String headerText;
	private PromLogger logger;
	private LogListener hierarchicalViewListener;
	private ISelectionChangedListener outlineViewListener;
	private UnderstandabilityView understandabilityView;
	private StyleRange[] styleRanges;

	public HierarchicalActivity(SurveyAttribute question, String hint, String headerText, OutlineViewNode<Graph> modelRootNode,
			Process process, StyleRange... styleRanges) {
		super(HIERARCHICAL);

		this.process = process;
		this.mainNode = modelRootNode;
		this.question = question;
		this.hint = hint;
		this.headerText = headerText;
		this.styleRanges = styleRanges;
	}

	@Override
	protected void doExecute() {
		initLogger();

		boolean hasHierarchy = mainNode.hasChildren();

		IWorkbench workbench = PlatformUI.getWorkbench();
		String perspectiveId = HierarchicalPerspective.ID;
		if (!hasHierarchy) {
			perspectiveId = HierarchicalPerspectiveWithoutOutline.ID;
		}

		IPerspectiveDescriptor perspective = workbench.getPerspectiveRegistry().findPerspectiveWithId(perspectiveId);
		IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
		activePage.setPerspective(perspective);

		understandabilityView = (UnderstandabilityView) activePage.findView(UnderstandabilityView.ID);
		understandabilityView.setQuestion(question);

		if (styleRanges != null && styleRanges.length > 0) {
			understandabilityView.setStyleRanges(styleRanges);
		}

		understandabilityView.setHint(hint);
		understandabilityView.addDismissListener(new Runnable() {
			@Override
			public void run() {
				setFinished(true);
			}
		});
		understandabilityView.setHeaderText(headerText);
		final HierarchicalView hierarchicalView = (HierarchicalView) activePage.findView(HierarchicalView.ID);
		hierarchicalViewListener = new LogListener();
		hierarchicalView.addLogListener(hierarchicalViewListener);

		if (hasHierarchy) {
			HierarchicalOutlineView hierarchicalOutlineView = (HierarchicalOutlineView) activePage.findView(HierarchicalOutlineView.ID);

			outlineViewListener = new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (event.getSelection() instanceof IStructuredSelection) {
						IStructuredSelection selection = (IStructuredSelection) event.getSelection();
						Iterator it = selection.iterator();
						if (it.hasNext()) {
							@SuppressWarnings("unchecked")
							OutlineViewNode<Graph> node = (OutlineViewNode<Graph>) it.next();
							hierarchicalView.setInput(node);
						}

					}

				}
			};

			hierarchicalOutlineView.addSelectionChangeListener(outlineViewListener);
			hierarchicalOutlineView.setInput(mainNode);
		} else {
			hierarchicalView.setInput(mainNode);
		}

		// HierarchicalComboView hierarchicalComboView = (HierarchicalComboView) activePage.findView(HierarchicalComboView.ID);
		// hierarchicalComboView.setInput(mainNode);

		setFinished(false);
	}

	@Override
	protected List<Attribute> getData() {
		List<Attribute> data = super.getData();
		data.add(new Attribute(CommonConstants.ATTRIBUTE_PROCESS_INSTANCE, getLogger().getProcessInstanceId()));
		data.addAll(understandabilityView.collectInput());

		return data;
	}

	private AbstractPromLogger getLogger() {
		if (logger == null) {
			this.logger = new PromLogger();
		}

		return logger;
	}

	public OutlineViewNode<Graph> getMainNode() {
		return mainNode;
	}

	@Override
	public Object getName() {
		return "Hierarchical Modeling Task";
	}

	protected void initLogger() {
		ProcessInstance instance = new ProcessInstance();
		String instanceId = ExperimentalWorkflowEngine.generateProcessInstanceId();
		instance.setId(instanceId);
		instance.setAttribute(AbstractGraphCommand.ASSIGNED_ID, instanceId);
		instance.setAttribute(ModelerConstants.ATTRIBUTE_TYPE, EditorRegistry.BPMN);
		instance.setAttribute(CommonConstants.ATTRIBUTE_TIMESTAMP, System.currentTimeMillis());
		PromLogger.addHost(instance);

		try {
			getLogger().append(process, instance);
		} catch (Exception ex) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not initialize the log file.", ex);
			Activator.getDefault().getLog().log(status);
		}
	}

	@Override
	protected void postExecute() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		HierarchicalOutlineView hierarchicalOutlineView = (HierarchicalOutlineView) page.findView(HierarchicalOutlineView.ID);
		if (hierarchicalOutlineView != null) {
			hierarchicalOutlineView.removeListener(outlineViewListener);
		}

		HierarchicalView hierarchicalView = (HierarchicalView) page.findView(HierarchicalView.ID);
		hierarchicalView.removeListener(hierarchicalViewListener);

		getLogger().close();
		page.setPerspective(PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(Perspective.ID));
	}

}
