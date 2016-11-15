package at.zugal.fitnesse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Display display = Display.getDefault();
		GC gc = new GC(display);
		int width = display.getBounds().width;
		int height = display.getBounds().height;
		Image image = new Image(display, width, height);
		int factor = 5;
		Image scaled = new Image(display, new ImageData(width / factor, height / factor, 8, new PaletteData(0xff, 0x00ff, 0x000ff)));
		scaled.getImageData().depth = 3;
		GC scaledGC = new GC(scaled);

		while (!display.isDisposed()) {
			gc.copyArea(image, 0, 0);
			ImageLoader loader = new ImageLoader();
			scaledGC.drawImage(image, 0, 0, width, height, 0, 0, width / factor, height / factor);
			loader.data = new ImageData[] { scaled.getImageData() };
			loader.save("C:\\out.png", SWT.IMAGE_PNG);

			display.sleep();
			Thread.sleep(1000);
		}
	}
}
