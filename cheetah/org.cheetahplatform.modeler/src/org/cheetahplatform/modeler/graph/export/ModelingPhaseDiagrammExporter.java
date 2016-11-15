package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.SWTResourceManager;

public class ModelingPhaseDiagrammExporter extends AbstractExporter {

	private static final int TIME_SCALING = 200;

	/**
	 * Vertical line step in number of elements (unscaled)
	 */
	private static final int HORIZONTAL_LINE_STEP = 20;
	private static final int HORIZONTAL_SCALING = 5;
	private static final int VERTICAL_SCALING = 4;
	private static final int MAXIMUM_TIME = 18000;
	private static final int MAXIMUM_NUMBER_OF_EVENTS = 120;

	private static final int MARGIN_TOP = 30;
	private static final int MARGIN_BOTTOM = 15;
	private static final int MARGIN_LEFT = 25;
	private static final int MARGIN_RIGHT = 25;
	private static final int CHART_HEIGHT = MAXIMUM_NUMBER_OF_EVENTS * VERTICAL_SCALING;
	private static final int TOTAL_HEIGHT = CHART_HEIGHT + MARGIN_BOTTOM + MARGIN_TOP;
	private static final int CHART_WIDTH = MAXIMUM_TIME / HORIZONTAL_SCALING;
	private static final int TOTAL_WIDTH = CHART_WIDTH + MARGIN_LEFT + MARGIN_RIGHT;

	private static final int LEGEND_X = TOTAL_WIDTH - MARGIN_RIGHT - 300;
	private static final int LEGEND_Y = MARGIN_TOP + 270;

	private ModelingPhaseChunkExtractor chunkExtractor;
	private boolean keepSettings = false;
	private File target;

	private void addPoints(List<ModelingPhase> points, BufferedWriter writer, String type) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(type + ";\n");
		builder.append("time;elements;\n");
		for (ModelingPhase entry : points) {
			if (!type.equals(entry.getType())) {
				builder.append(";\n");
				builder.append(";\n");
				continue;
			}
			builder.append(entry.getStart() * TIME_SCALING / 1000);
			builder.append(";");
			builder.append(entry.getStartNumberOfElements());
			builder.append(";\n");
			builder.append(entry.getEnd() * TIME_SCALING / 1000);
			builder.append(";");
			builder.append(entry.getEndNumberOfElements());
			builder.append(";\n");
		}
		writer.write(builder.toString());
	}

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		ProcessInstance modelingInstance = handle.getInstance();
		String attribute = modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		if (!(attribute.equals("nve_modeling_task1_1.0") || attribute.equals("modeling_task1_bpmn_1.0"))) {
			return;
		}

		Process process = ProcessRepository.getProcess(attribute);
		String type = modelingInstance.getAttribute(ModelerConstants.ATTRIBUTE_TYPE);
		Graph graph = AbstractModelingActivity.loadInitialGraph(process, type);

		String data = DatabaseUtil.toDatabaseRepresentation(modelingInstance.getAttributes());
		ProcessInstanceDatabaseHandle processInstanceHandle = new ProcessInstanceDatabaseHandle(handle.getDatabaseId(),
				modelingInstance.getId(), data, null);
		processInstanceHandle.setInstance(modelingInstance);
		ReplayModel replayModel = new ReplayModel(new GraphCommandStack(graph), processInstanceHandle, graph);

		if (!keepSettings) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					Shell shell = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell();
					ModelingPhaseDiagramDialog dialog = new ModelingPhaseDiagramDialog(shell);
					if (dialog.open() != Window.OK) {
						return;
					}
					chunkExtractor = new ModelingPhaseChunkExtractor(dialog.getModelingPhaseDetectionStrategy(), dialog
							.getComprehensionThreshold(), dialog.getComprehensionAggregationThreshold());
					keepSettings = dialog.isKeepSettings();
				}
			});
		}

		if (chunkExtractor == null) {
			return;
		}

		List<ModelingPhase> points = extractPoints(graph, replayModel, chunkExtractor, modelingInstance);
		drawChart(modelingInstance, points, chunkExtractor);
		writeChunkFile(modelingInstance, points);
	}

	private void drawChart(ProcessInstance modelingInstance, List<ModelingPhase> points, ModelingPhaseChunkExtractor chunkExtractor) {
		Image image = new Image(Display.getDefault(), TOTAL_WIDTH, TOTAL_HEIGHT);
		GC gc = new GC(image);

		drawChartAxis(gc, modelingInstance);
		drawPoints(points, gc, chunkExtractor);
		writeChartToFile(target, modelingInstance, image);
	}

	private void drawChartAxis(GC gc, ProcessInstance modelingInstance) {
		TextLayout header = new TextLayout(Display.getDefault());
		header.setText(modelingInstance.getId());
		header.setFont(SWTResourceManager.getFont("Calibri", 16, SWT.BOLD));
		header.draw(gc, MARGIN_LEFT + CHART_WIDTH / 2 - 10, 3);

		gc.setBackground(SWTResourceManager.getColor(250, 250, 250));
		gc.fillRectangle(MARGIN_LEFT, MARGIN_TOP, CHART_WIDTH, CHART_HEIGHT);
		gc.setBackground(SWTResourceManager.getColor(255, 255, 255));

		gc.setForeground(SWTResourceManager.getColor(0, 0, 0));
		gc.drawLine(MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT, MARGIN_TOP + CHART_HEIGHT);
		gc.drawLine(MARGIN_LEFT, MARGIN_TOP + CHART_HEIGHT, MARGIN_LEFT + CHART_WIDTH, MARGIN_TOP + CHART_HEIGHT);

		int y = MARGIN_TOP + CHART_HEIGHT - (HORIZONTAL_LINE_STEP * VERTICAL_SCALING);
		do {
			gc.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + CHART_WIDTH, y);
			y -= (HORIZONTAL_LINE_STEP * VERTICAL_SCALING);
		} while (y >= MARGIN_TOP);

		gc.drawString(String.valueOf(MAXIMUM_NUMBER_OF_EVENTS), 4, MARGIN_TOP - 2);
		gc.drawString(String.valueOf(0), MARGIN_LEFT, MARGIN_TOP + CHART_HEIGHT + 2);
		gc.drawString(String.valueOf(MAXIMUM_TIME * TIME_SCALING / 1000), TOTAL_WIDTH - MARGIN_RIGHT - 10, MARGIN_TOP + CHART_HEIGHT + 2);
		gc.drawString("time", MARGIN_LEFT + 300, MARGIN_TOP + CHART_HEIGHT + 2);

		TextLayout textLayout = new TextLayout(Display.getDefault());
		textLayout.setText("#elements");
		textLayout.setWidth(5);
		textLayout.draw(gc, 7, MARGIN_TOP + 100);
	}

	private void drawLegend(GC gc, Map<String, Color> legendEntries) {
		if (legendEntries.isEmpty()) {
			return;
		}

		int width = 0;
		int y = LEGEND_Y;
		for (String type : legendEntries.keySet()) {
			gc.setForeground(legendEntries.get(type));

			TextLayout textLayout = new TextLayout(Display.getDefault());
			textLayout.setText(type);
			textLayout.draw(gc, LEGEND_X, y);
			if (textLayout.getBounds().width > width) {
				width = textLayout.getBounds().width;
			}
			y += 15;
		}

		gc.setForeground(SWTResourceManager.getColor(0, 0, 0));
		gc.drawRectangle(LEGEND_X - 5, LEGEND_Y - 5, width + 10, y - LEGEND_Y + 10);
	}

	private void drawPoints(List<ModelingPhase> points, GC gc, ModelingPhaseChunkExtractor chunkExtractor) {
		Map<String, Color> legendEntries = new HashMap<String, Color>();
		List<String> additionalInformation = chunkExtractor.getAdditionalInformation();
		for (String info : additionalInformation) {
			legendEntries.put(info, SWTResourceManager.getColor(0, 0, 0));
		}
		boolean left = true;
		for (ModelingPhase entry : points) {
			legendEntries.put(entry.getType(), getColor(entry.getType()));

			gc.setBackground(getColor(entry.getType()));
			gc.setForeground(getColor(entry.getType()));
			int y = CHART_HEIGHT + MARGIN_TOP - (entry.getStartNumberOfElements() * VERTICAL_SCALING);
			int x = (int) (entry.getStart() / TIME_SCALING) / HORIZONTAL_SCALING + MARGIN_LEFT + 2;
			gc.fillRectangle(x, y - 3, 2, 6);
			int endy = CHART_HEIGHT + MARGIN_TOP - (entry.getEndNumberOfElements() * VERTICAL_SCALING);
			int endx = (int) (entry.getEnd() / TIME_SCALING) / HORIZONTAL_SCALING + MARGIN_LEFT;
			if (endx < x) {
				endx = x;
			}
			gc.fillRectangle(endx, endy - 3, 2, 6);
			gc.setLineWidth(3);
			gc.drawLine(x, y, endx, endy);
			gc.setLineWidth(1);
			if (!entry.getType().equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
				gc.setBackground(SWTResourceManager.getColor(255, 255, 255));
				if (left) {
					gc.drawString(String.valueOf(entry.getChunkSize()), x - 5, y - 20);
				} else {
					gc.drawString(String.valueOf(entry.getChunkSize()), x + 5, y + 5);
				}
				left = !left;
			}
		}

		drawLegend(gc, legendEntries);
	}

	private List<ModelingPhase> extractPoints(Graph graph, ReplayModel replayModel, ModelingPhaseChunkExtractor chunkExtractor,
			ProcessInstance modelingInstance) {
		List<Chunk> chunks = chunkExtractor.extractChunks(modelingInstance);
		List<ModelingPhase> points = new ModelingPhaseDiagrammLineFragmentExtrator().extractLineFragments(graph, replayModel.getReplayer(),
				modelingInstance, chunks);
		return points;
	}

	public Color getColor(String type) {
		if (type.equals(ModelingPhaseChunkExtractor.COMPREHENSION)) {
			return SWTResourceManager.getColor(192, 80, 77);
		} else if (type.equals(ModelingPhaseChunkExtractor.MODELING)) {
			return SWTResourceManager.getColor(247, 150, 70);
		} else if (type.equals(ModelingPhaseChunkExtractor.RECONCILIATION)) {
			return SWTResourceManager.getColor(155, 187, 89);
		}

		throw new IllegalArgumentException("Unknown modeling phase");
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

	private void writeChartToFile(File target, ProcessInstance modelingInstance, Image image) {
		File chartFile = new File(getPathToFile(target, modelingInstance, "png"));
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(chartFile);
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { image.getImageData() };
			loader.save(fileOutputStream, SWT.IMAGE_PNG);
			fileOutputStream.close();
		} catch (Exception e) {
			showErrorMessage("Error writing file: " + chartFile.getAbsolutePath());
		} finally {
			image.dispose();
		}
	}

	private void writeChunkFile(ProcessInstance modelingInstance, List<ModelingPhase> points) {
		try {
			File file = new File(getPathToFile(target, modelingInstance, "csv"));
			if (file.exists()) {
				final String error = "Could not write file: " + file.getAbsolutePath();
				showErrorMessage(error);
				return;
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Model id;" + modelingInstance.getId() + ";\n");

			writer.write("\n");

			List<String> additionalInformation = chunkExtractor.getAdditionalInformation();
			if (additionalInformation != null && !(additionalInformation.isEmpty())) {
				for (String info : additionalInformation) {
					writer.write(info);
					writer.write("\n");
				}
			}

			addPoints(points, writer, ModelingPhaseChunkExtractor.COMPREHENSION);
			addPoints(points, writer, ModelingPhaseChunkExtractor.MODELING);
			addPoints(points, writer, ModelingPhaseChunkExtractor.RECONCILIATION);
			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error writing csv file for modeling process " + modelingInstance.getId() + " to " + target.getAbsolutePath());
		}
	}
}
