package org.cheetahplatform.modeler.admin.basic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class EPrintsGrabberShell extends Shell {

	private int item;
	private Browser browser;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			EPrintsGrabberShell shell = new EPrintsGrabberShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public EPrintsGrabberShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		item = 164;
		setText("SWT Application");
		setSize(776, 609);
		setLayout(new GridLayout());

		browser = new Browser(this, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		browser.setUrl("http://ref.q-e.at/164/");

		Button button = new Button(this, SWT.NONE);
		button.setText("Grab!");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					grab();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	protected void grab() throws IOException {
		try {
			String text = browser.getText();
			int indexOf = text
					.indexOf("<META name=DC.identifier content=http://ref.q-e.at/");
			if (indexOf != -1) {
				int endIndex = text.indexOf(">", indexOf);
				String pdfUrl = text.substring(indexOf + 33, endIndex );
				URL url = new URL(pdfUrl);

				InputStream stream = url.openStream();
				FileOutputStream out = new FileOutputStream(new File("C:\\out\\"
						+ item + ".pdf"));
				byte[] buffer = new byte[1024];
				int length = stream.read(buffer);

				while (length != -1) {
					out.write(buffer, 0, length);
					length = stream.read(buffer);
				}

				stream.close();
				out.close();
			}

			item++;
			if (item >= 500) {
				return;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						String rawUrl = "http://ref.q-e.at/{0}/";
						browser.setUrl(MessageFormat.format(rawUrl, item));
					}
				});

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						try {
							grab();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
