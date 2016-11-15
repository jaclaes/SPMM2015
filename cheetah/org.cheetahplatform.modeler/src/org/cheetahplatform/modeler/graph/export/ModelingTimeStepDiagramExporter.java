package org.cheetahplatform.modeler.graph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.CommonConstants;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.ProcessInstance;
import org.cheetahplatform.common.logging.db.DatabaseUtil;
import org.cheetahplatform.common.ui.dialog.ProcessInstanceDatabaseHandle;
import org.cheetahplatform.modeler.ModelerConstants;
import org.cheetahplatform.modeler.engine.AbstractModelingActivity;
import org.cheetahplatform.modeler.engine.ProcessRepository;
import org.cheetahplatform.modeler.generic.GraphCommandStack;
import org.cheetahplatform.modeler.graph.CommandDelegate;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.dialog.ReplayModel;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         05.03.2010
 */
public class ModelingTimeStepDiagramExporter extends AbstractExporter {
	private class Entry {
		private final long time;
		private final int numberOfElements;
		private final String type;

		public Entry(long time, int numberOfElements, String type) {
			Assert.isNotNull(type);
			this.time = time;
			this.numberOfElements = numberOfElements;
			this.type = type;
		}

		public Color getColor() {
			if (type.equals(AbstractGraphCommand.CREATE_NODE)) {
				return SWTResourceManager.getColor(255, 0, 0);
			} else if (type.equals(AbstractGraphCommand.CREATE_EDGE)) {
				return SWTResourceManager.getColor(0, 0, 255);
			}

			return SWTResourceManager.getColor(125, 125, 125);
		}

		/**
		 * Returns the numberOfElements.
		 * 
		 * @return the numberOfElements
		 */
		public int getNumberOfElements() {
			return numberOfElements;
		}

		/**
		 * Returns the time.
		 * 
		 * @return the time
		 */
		public long getTime() {
			return time;
		}

		/**
		 * Returns the type.
		 * 
		 * @return the type
		 */
		public String getType() {
			return type;
		}
	}

	/**
	 * Vertical line step in number of elements (unscaled)
	 */
	private static final int HORIZONTAL_LINE_STEP = 20;
	private static final int HORIZONTAL_SCALING = 5;
	private static final int VERTICAL_SCALING = 4;
	private static final int MAXIMUM_TIME = 3000;
	private static final int MAXIMUM_NUMBER_OF_EVENTS = 100;

	private static final int MARGIN_TOP = 30;
	private static final int MARGIN_BOTTOM = 15;
	private static final int MARGIN_LEFT = 25;
	private static final int MARGIN_RIGHT = 25;
	private static final int CHART_HEIGHT = MAXIMUM_NUMBER_OF_EVENTS * VERTICAL_SCALING;
	private static final int TOTAL_HEIGHT = CHART_HEIGHT + MARGIN_BOTTOM + MARGIN_TOP;
	private static final int CHART_WIDTH = MAXIMUM_TIME / HORIZONTAL_SCALING;
	private static final int TOTAL_WIDTH = CHART_WIDTH + MARGIN_LEFT + MARGIN_RIGHT;

	private static final int LEGEND_X = TOTAL_WIDTH - MARGIN_RIGHT - 150;
	private static final int LEGEND_Y = MARGIN_TOP + 270;

	private File target;

	@Override
	protected void doExportModelingProcessInstance(ProcessInstanceDatabaseHandle handle, AuditTrailEntry entry) {
		ProcessInstance modelingInstance = handle.getInstance();

		// TODO remove
		String attribute = modelingInstance.getAttribute(CommonConstants.ATTRIBUTE_PROCESS);
		if (attribute.equals("equipment_transport_change1_1.0")) {
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

		List<Entry> points = extractPoints(graph, replayModel);
		writeValueFile(modelingInstance, points);
		drawChart(modelingInstance, points);
	}

	private void drawChart(ProcessInstance modelingInstance, List<Entry> points) {
		Image image = new Image(Display.getDefault(), TOTAL_WIDTH, TOTAL_HEIGHT);
		GC gc = new GC(image);

		drawChartAxis(gc, modelingInstance);
		drawPoints(points, gc);
		writeChartToFile(modelingInstance, image);
	}

	private void drawChartAxis(GC gc, ProcessInstance modelingInstance) {
		TextLayout header = new TextLayout(Display.getDefault());
		header.setText(modelingInstance.getId());
		header.setFont(SWTResourceManager.getFont("Calibri", 16, SWT.BOLD));
		header.draw(gc, MARGIN_LEFT + CHART_WIDTH / 2 - 10, 3);

		gc.setBackground(SWTResourceManager.getColor(240, 240, 240));
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
		gc.drawString(String.valueOf(MAXIMUM_TIME), TOTAL_WIDTH - MARGIN_RIGHT - 10, MARGIN_TOP + CHART_HEIGHT + 2);
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

	private void drawPoints(List<Entry> points, GC gc) {
		List<String> typesToDisplay = new ArrayList<String>();
		typesToDisplay.add(AbstractGraphCommand.CREATE_EDGE);
		typesToDisplay.add(AbstractGraphCommand.CREATE_NODE);
		// typesToDisplay.add(AbstractGraphCommand.DELETE_NODE);

		Map<String, Color> legendEntries = new HashMap<String, Color>();
		for (Entry entry : points) {
			if (!typesToDisplay.contains(entry.getType())) {
				continue;
			}

			legendEntries.put(entry.getType(), entry.getColor());

			gc.setBackground(entry.getColor());
			int y = CHART_HEIGHT + MARGIN_TOP - (entry.getNumberOfElements() * VERTICAL_SCALING);
			int x = (int) entry.getTime() / HORIZONTAL_SCALING + MARGIN_LEFT;
			gc.fillRectangle(x, y, 2, 2);
		}

		drawLegend(gc, legendEntries);
	}

	private List<Entry> extractPoints(Graph graph, ReplayModel replayModel) {
		long startTime = 0;
		boolean first = true;
		List<Entry> points = new ArrayList<Entry>();
		while (replayModel.getReplayer().hasCommands()) {
			CommandDelegate currentCommand = replayModel.getReplayer().getCurrentCommand();
			if (first) {
				startTime = currentCommand.getAuditTrailEntry().getTimestamp().getTime();
				first = false;
			}
			replayModel.getReplayer().executeNextCommand();

			long time = (currentCommand.getAuditTrailEntry().getTimestamp().getTime() - startTime) / 1000;
			int size = graph.getGraphElements().size();
			points.add(new Entry(time, size, currentCommand.getType()));
		}
		return points;
	}

	private String getPathToFile(ProcessInstance modelingInstance) {
		return target.getAbsolutePath() + "/" + modelingInstance.getId();
	}

	@Override
	public void initializeExport(File target) {
		this.target = target;
	}

	private void writeChartToFile(ProcessInstance modelingInstance, Image image) {
		String pathname = getPathToFile(modelingInstance);
		File chartFile = new File(pathname + ".png");
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

	private void writeValueFile(ProcessInstance modelingInstance, List<Entry> points) {
		String path = getPathToFile(modelingInstance) + ".csv";
		File file = new File(path);

		if (file.exists()) {
			final String error = "Could not write file: " + path;
			showErrorMessage(error);
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("Model id;" + modelingInstance.getId() + ";\n");

			List<Attribute> attributes = modelingInstance.getAttributes();
			for (Attribute attribute : attributes) {
				writer.write(attribute.getName() + ";" + attribute.getContent() + ";\n");
			}
			writer.write("\n");

			writer.write("type;time;numberOfElements;\n");

			for (Entry point : points) {
				writer.write(point.getType() + ";" + point.getTime() + ";" + point.getNumberOfElements() + ";\n");
			}

			writer.close();
		} catch (IOException e) {
			showErrorMessage("Error writing csv file: " + path);
		}
	}
}
