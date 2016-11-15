package org.cheetahplatform.modeler.graph.export;

import java.io.File;
import java.util.List;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.ViewerWithAccessibleLightweightSystem;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GenericEditPartFactory;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.generic.GraphDimension;
import org.cheetahplatform.modeler.generic.GraphEditDomain;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.command.CompoundGraphCommand;
import org.cheetahplatform.modeler.graph.command.HorizontalScrollCommand;
import org.cheetahplatform.modeler.graph.command.VerticalScrollCommand;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.cheetahplatform.tdm.routing.ShortestPathConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class ModelImageExporter extends AbstractExporter {

	private File target;

	@Override
	protected void doExportModelingProcessInstance(final ProcessInstanceDatabaseHandle handle, final AuditTrailEntry entry) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				ProcessInstance modelingInstance = handle.getInstance();
				String attribute = modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);

				Process process = ProcessRepository.getProcess(attribute);
				String type = modelingInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
				Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);

				String data = DatabaseUtil.toDatabaseRepresentation(modelingInstance.getAttributes());
				ProcessInstanceDatabaseHandle processInstanceHandle = new ProcessInstanceDatabaseHandle(handle.getDatabaseId(),
						modelingInstance.getId(), data, null);
				processInstanceHandle.setInstance(modelingInstance);
				ReplayModel replayModel = new ReplayModel(new GraphCommandStack(graph), processInstanceHandle, graph);
				List<CommandDelegate> commands = replayModel.getCommands();
				for (CommandDelegate command : commands) {
					AbstractGraphCommand unwrappedCommand = command.getCommand();
					if (unwrappedCommand instanceof TDMCommand) {
						continue;// TDM commands are of no use for model images
					}
					if (unwrappedCommand instanceof HorizontalScrollCommand) {
						continue;// don't care about scrolling here
					}
					if (unwrappedCommand instanceof VerticalScrollCommand) {
						continue;// don't care about scrolling here
					}

					if (unwrappedCommand instanceof CompoundGraphCommand) {
						List<CommandDelegate> children = command.getChildren();
						for (CommandDelegate child : children) {
							if (child.getCommand() instanceof TDMCommand) {
								continue;// TDM commands are of no use for model images
							}

							child.getCommand().execute();
						}
					} else {
						unwrappedCommand.execute();
					}
				}

				ViewerWithAccessibleLightweightSystem outputViewer = new ViewerWithAccessibleLightweightSystem();
				outputViewer.setEditDomain(new GraphEditDomain());
				outputViewer.setEditPartFactory(new GenericEditPartFactory());

				outputViewer.setContents(graph);
				((GraphCommandStack) outputViewer.getEditDomain().getCommandStack()).setGraph(graph);

				ScalableRootEditPart rootEditPart = (ScalableRootEditPart) outputViewer.getRootEditPart();
				ConnectionLayer layer = (ConnectionLayer) rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
				ShortestPathConnectionRouter router = new ShortestPathConnectionRouter((IFigure) ((Layer) rootEditPart.getContentPane())
						.getChildren().get(0));
				router.setSpacing(10);
				layer.setConnectionRouter(router);

				// make the bendpoints appear (we have set a new connection router --> update the bendpoints in the new router)
				for (Edge edge : graph.getEdges()) {
					AbstractGraphicalEditPart editPart = (AbstractGraphicalEditPart) outputViewer.getEditPartRegistry().get(edge);
					editPart.refresh();
				}

				// reset some values in the root edit part, as they are inherited by children - not allowed to be null
				IFigure rootFigure = ((AbstractGraphicalEditPart) outputViewer.getRootEditPart()).getFigure();
				rootFigure.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NONE)); //$NON-NLS-1$
				rootFigure.setForegroundColor(SWTResourceManager.getColor(0, 0, 0));
				rootFigure.setBackgroundColor(SWTResourceManager.getColor(0, 0, 0));

				GraphDimension graphDimension = new GraphDimension(graph, outputViewer);
				Dimension size = graphDimension.getDimension().expand(graphDimension.getTranslateValue().negate());
				IFigure root = outputViewer.getLightweightSystem().getRootFigure();
				root.setSize(size);

				Image image = new Image(null, size.width, size.height);
				GC gc = new GC(image);
				outputViewer.getLightweightSystem().paint(gc);
				gc.setBackground(SWTResourceManager.getColor(255, 255, 255));
				gc.setForeground(SWTResourceManager.getColor(0, 0, 0));
				gc.drawString("Id: " + modelingInstance.getId(), 20, 20);

				gc.dispose();

				ImageLoader loader = new ImageLoader();
				loader.data = new ImageData[] { image.getImageData() };
				loader.save(target.getAbsolutePath() + "/" + processInstanceHandle.getId() + "_" + attribute + ".png", SWT.IMAGE_PNG);
			}
		});
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}
}
