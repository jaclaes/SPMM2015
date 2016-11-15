package org.cheetahplatform.modeler.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.modeler.generic.figure.IPathFigure;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.swtdesigner.SWTResourceManager;

public class PathFigureAnchor extends ChopboxAnchor {

	private Dimension cachedDimension;
	private List<Point> points;

	public PathFigureAnchor(IFigure owner) {
		super(owner);

		this.cachedDimension = new Dimension();
	}

	private void computePoints() {
		Rectangle bounds = getOwner().getBounds();
		int width = bounds.width;
		int height = bounds.height;

		if (cachedDimension.width == width && cachedDimension.height == height) {
			return;
		}

		cachedDimension = new Dimension(width, height);
		PaletteData palette = new PaletteData(new RGB[] { new RGB(0, 0, 0), new RGB(255, 255, 255) });
		Image image = new Image(Display.getDefault(), new ImageData(width, height, 1, palette));
		System.out.println(image.getImageData().depth);

		GC gc = new GC(image);
		Path path = ((IPathFigure) getOwner()).computePath(new Point(0, 0));
		gc.setForeground(SWTResourceManager.getColor(255, 255, 255));
		gc.drawPath(path);
		path.dispose();
		gc.dispose();
		ImageData data = image.getImageData();
		int[] pixels = new int[width * height];
		data.getPixels(0, 0, width * height, pixels, 0);
		image.dispose();

		points = new ArrayList<Point>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];
				System.out.println(pixel);
				if (pixel != 0) {
					points.add(new Point(x, y));
				}
			}
		}
		System.out.println("end");
	}

	@Override
	public Point getLocation(Point reference) {
		Rectangle bounds = getOwner().getBounds().getCopy();
		if (bounds.width == 0 && bounds.height == 0) {
			return bounds.getLocation();
		}

		getOwner().translateToAbsolute(bounds);
		computePoints();

		Point closest = null;
		double closestDistance = Double.MAX_VALUE;
		int offsetX = bounds.x;
		int offsetY = bounds.y;

		for (Point point : points) {
			int x = point.x + offsetX;
			int y = point.y + offsetY;
			int differenceX = x - reference.x;
			int differenceY = y - reference.y;

			double distance = Math.sqrt(Math.pow(differenceX, 2) + Math.pow(differenceY, 2));
			if (distance < closestDistance) {
				closestDistance = distance;
				closest = new Point(x, y);
			}
		}

		return closest;
	}

}
