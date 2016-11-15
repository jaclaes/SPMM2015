package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import java.net.URL;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.modeler.Activator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public abstract class AbstractTreatmentWizardPage extends WizardPage {
	protected final static String PROCESS = "TREATMENT"; //$NON-NLS-1$
	protected final static Color WHITE = new Color(null, 255, 255, 255);
	protected final static Color LIGHT_GREY = new Color(null, 241, 241, 241);
	protected final static Color DARK_GREY = new Color(null, 195, 195, 195);
	protected final static Color BLACK = new Color(null, 0, 0, 0);
	protected final static Color GREEN = new Color(null, 119, 147, 60);
	protected final static Color RED = new Color(null, 192, 80, 77);
	protected final static Color BACKGROUND_COLOR = WHITE;
	
	protected final static String MSG = "message";
	protected final static String TXT = "text";

	private final static int EXTRA_FONT_SIZE = 3;
	private final static int EXTRA_SOURCE_FONT_SIZE = -3;
	private final static int EXTRA_HUGE_FONT_SIZE = 20;
	protected static Font FONT_NORMAL = null;
	protected static Font FONT_BOLD = null;
	protected static Font FONT_PLAIN_BOLD = null;
	protected static Font FONT_SMALL = null;
	protected static Font FONT_HUGE = null;
	
	protected final static int SEQ = 0;
	protected final static int GLOB = 1;
	protected final static int BETW = 2;
	
	protected final static int FD = 0;
	protected final static int FID = 1;
	
	protected final static int HNFS1 = 0;
	protected final static int LNFS1 = 1;
	
	private static int numErrors = 0;
	
	protected final LoggingValidator logValidator;
	
	protected AbstractTreatmentWizardPage(LoggingValidator logValidator, String pageName) {
		super(pageName);
		initializeFonts();
		this.logValidator = logValidator;
		setTitle(format(Messages.AbstractTreatmentWizardPage_Tutorial));
		setDescription(format(Messages.AbstractTreatmentWizardPage_Answer_Below));
	}
	private void initializeFonts() {
		if (FONT_NORMAL != null)
			return;
		
		Font f = getFont();
		FontData[] fDs = f.getFontData();
		for (FontData fD : fDs)
			fD.setStyle(SWT.BOLD);
		FONT_PLAIN_BOLD = new Font(f.getDevice(), fDs);
		
		fDs = f.getFontData();
		for (FontData fD : fDs)
			fD.setHeight(fD.getHeight() + EXTRA_FONT_SIZE);
		FONT_NORMAL = new Font(f.getDevice(), fDs);
		
		fDs = FONT_NORMAL.getFontData();
		for (FontData fD : fDs)
			fD.setStyle(SWT.BOLD);
		FONT_BOLD = new Font(f.getDevice(), fDs);
		
		fDs = f.getFontData();
		for (FontData fD : fDs)
			fD.setHeight(fD.getHeight() + EXTRA_SOURCE_FONT_SIZE);
		FONT_SMALL = new Font(f.getDevice(), fDs);
		
		fDs = f.getFontData();
		for (FontData fD : fDs)
			fD.setHeight(fD.getHeight() + EXTRA_HUGE_FONT_SIZE);
		FONT_HUGE = new Font(f.getDevice(), fDs);
	}
	
	protected Composite getMainControl(Composite parent) {
		final Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(GridLayoutFactory.fillDefaults().create());
		rootComposite.setBackground(BACKGROUND_COLOR);
		rootComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		
		ScrolledComposite scroll = new ScrolledComposite(rootComposite, SWT.V_SCROLL);
		scroll.setBackground(BACKGROUND_COLOR);
		int screenheight = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
		scroll.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, screenheight).create());
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		
		Composite filler = new Composite(scroll, SWT.NONE);
		GridLayout rootGrid = new GridLayout(3, false);
		rootGrid.marginHeight = 0;
		rootGrid.marginWidth = 0;
		filler.setLayout(rootGrid);
		filler.setBackground(BACKGROUND_COLOR);
		
		Label left = new Label(filler, SWT.NONE);
		left.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		left.setBackground(BACKGROUND_COLOR);

		Composite control = new Composite(filler, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		gl.marginTop = 10;
		gl.marginLeft = 10;
		gl.marginRight = 40;
		gl.marginBottom = 10;
		control.setLayout(gl);
		control.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));
		control.setBackground(BACKGROUND_COLOR);
		
		Label right = new Label(filler, SWT.NONE);
		right.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		right.setBackground(BACKGROUND_COLOR);


		control.addDisposeListener(new DisposeListener() {
		    public void widgetDisposed(DisposeEvent e) {
		    	try {
			    	FONT_NORMAL.dispose();
			    	FONT_BOLD.dispose();
			    	FONT_SMALL.dispose();
			    	FONT_HUGE.dispose();
		    	} catch (Exception ex) { /*do nothing */ }
		    }
		});
		
		return control;
	}

	protected StyledText makeText(Composite control, String content) {
		return makeText(control, content, false, 2);
	}
	protected StyledText makeSource(Composite control, String content) {
		return makeText(control, content, true, 2);
	}
	private StyledText makeText(Composite control, String content, boolean small, int span) {
		StyledText text = new StyledText(control, SWT.TRANSPARENT | SWT.READ_ONLY);
		text.setText(content);
		//text.setBackground(BACKGROUND_COLOR);
		text.setBottomMargin(6);

		StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = content.length();
		if (small)
			styleRange.font = FONT_SMALL;
		else
			styleRange.font = FONT_NORMAL;
		text.setStyleRange(styleRange);

		if (span > 1) {
			GridData gridData = new GridData();
			gridData.horizontalSpan = span;
			text.setLayoutData(gridData);
		}
		return text;
	}
	protected void setText(StyledText text, String newText) {
		setText(text, newText, false);
	}
	private void setText(StyledText text, String newText, boolean small) {
		text.setText(newText);

		StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = newText.length();
		if (small)
			styleRange.font = FONT_SMALL;
		else
			styleRange.font = FONT_NORMAL;
		text.setStyleRange(styleRange);
	}
	protected StyledText makeBulletedText(Composite control, String content) {
		addBullet(control);
		return makeText(control, content, false, 1);
	}
	private void addBullet(Composite control) {
		StyledText bullet = makeText(control, "-", false, 1); //$NON-NLS-1$
		bullet.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
	}
	protected void setBold(StyledText text, String boldText) {
		StyleRange styleRangeBold = new StyleRange();
		styleRangeBold.start = text.getText().indexOf(boldText);
		styleRangeBold.length = boldText.length();
		styleRangeBold.font = FONT_BOLD;
		text.setStyleRange(styleRangeBold);
	}
	
	protected Text makeMultiText(Composite control) {
		return makeMultiText(control, 2);
	}
	private Text makeMultiText(Composite control, int span) {
		Text multi = new Text(control, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);

		if (span > 1) {
			GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.horizontalSpan = span;
			multi.setLayoutData(gridData);
		}
		return multi;
	}
	

	protected Label makePic(Composite control, URL url) {
		return makePic(control, url, 2);
	}
	private Label makePic(Composite control, URL url, int span) {
		Label pic = new Label(control, SWT.NONE);
		try {
			pic.setImage(ImageDescriptor.createFromURL(url).createImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (span > 1) {
			GridData gridData = new GridData();
			gridData.horizontalSpan = span;
			pic.setLayoutData(gridData);
		}
		return pic;
	}
	
	protected Group makeButtonGroup(Composite control) {
		return makeButtonGroup(control, 1);
	}
	protected Group makeButtonGroup(Composite control, int numColumns) {
		Group buttonGroup = new Group(control, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		buttonGroup.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		buttonGroup.setLayoutData(gridData);
		return buttonGroup;
	}
	protected Button makeLikertButton(Group buttonGroup, String factor, String text) {
		SelectionListener selectionListener = new SelectionAdapter () {
	    	public void widgetSelected(SelectionEvent event) {
	    		Button button = ((Button) event.widget);
	    		AuditTrailEntry entry = new AuditTrailEntry(PROCESS);
				entry.setAttribute(button.getData().toString(), button.getText());
				setPageComplete(true);
				getControl().redraw();
				logValidator.log(entry);
	    	};
	    };
	      
	    Button btn = new Button(buttonGroup, SWT.RADIO);
	    btn.setText(text);
	    btn.setData(factor);
	    btn.addSelectionListener(selectionListener);
	    return btn;
	}
	protected Button makeButton(Group buttonGroup, String text, SelectionListener listener) {
		Composite container = new Composite(buttonGroup, NONE);
		GridLayout gl = new GridLayout(2, false);
		container.setLayout(gl);
		container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		Button btn = new Button(container, SWT.RADIO);
		btn.setData(MSG, text);
		btn.addSelectionListener(listener);
		
		Text txt = new Text(container, SWT.WRAP | SWT.TRANSPARENT | SWT.READ_ONLY);
		txt.setText(text);
		btn.setData(TXT, txt);

		return btn;
	}
	protected void markRight(Button btn, Text feedback) {
		Text btnTxt = (Text)btn.getData(TXT); 
		btnTxt.setForeground(GREEN);
		//btnTxt.setFont(FONT_PLAIN_BOLD);
		
		feedback.setForeground(GREEN);
		//feedback.setFont(FONT_PLAIN_BOLD);
		feedback.setVisible(true);
		((GridData)feedback.getLayoutData()).exclude = false;
		
		getShell().layout(true, true);
	}
	protected void markWrong(Button btn, Text feedback) {
		btn.setSelection(false);
		btn.setEnabled(false);
		
		Text btnTxt = (Text)btn.getData(TXT); 
		btnTxt.setEnabled(false);
		btnTxt.setForeground(RED);
		
		feedback.setForeground(RED);
		feedback.setVisible(true);
		((GridData)feedback.getLayoutData()).exclude = false;
		
		getShell().layout(true, true);
	}
	
	protected Text makeFeedback(Composite control, int numLines) {
		String txt = " ";
		for (int i=1; i<numLines; i++)
			txt += System.lineSeparator() + " ";
		return makeFeedback(control, format(txt), 2);
	}
	protected Text makeFeedback(Composite control, String text) {
		return makeFeedback(control, text, 2);
	}
	protected Text makeFeedback(Composite control, String text, int span) {
		Text txt = new Text(control, SWT.WRAP | SWT.TRANSPARENT | SWT.READ_ONLY);
		txt.setText(text);
	    txt.setVisible(false);
		GridData gridData = new GridData();
		gridData.exclude = false;
		if (span > 1)
			gridData.horizontalSpan = span;
		txt.setLayoutData(gridData);
		txt.pack();
	    return txt;
	}
	protected void showFeedback(Text feedback) {
		feedback.setVisible(true);
		((GridData)feedback.getLayoutData()).exclude = false;
		
		getShell().layout(true, true);
	}

	protected static String format(String s) {
		return s.replace("\n", System.lineSeparator());
	}
	protected URL getResourcePath(String file) {
		try {
			return new URL("platform:/plugin/ugent.jcresearch.bpmn.experiments.cheetah.spmm2015/resources/" + file); //$NON-NLS-1$
		}
		catch (Exception e) {
			return null;
		}
	}
	protected void showScreencast(Browser screencastBrowser, String screencastPath) {
		String path = screencastPath;
		try {
			URL url = new URL("platform:/plugin/ugent.jcresearch.bpmn.experiments.cheetah.spmm2015/resources/" + screencastPath); //$NON-NLS-1$
			path = FileLocator.resolve(url).toExternalForm();
			//does not work if files are within jar
			if (path.contains(".jar!")) {
				int jar = path.indexOf(".jar!");
				int before = jar - 1;
				while (before >= 0 && path.charAt(before) != '/' && path.charAt(before) != '\\')
					before--;
				int after = jar + 5;
				while (after < path.length() && path.charAt(after) != '/' && path.charAt(after) != '\\')
					after++;
				path = path.replace(path.substring(before, after), "");
			}
			path = path.replace("jar:file:/", "");

			screencastBrowser.setUrl(path);
		} catch (Exception e) {
			Activator.getDefault().getLog()
					.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to load screencast: " + path)); //$NON-NLS-1$
		}
		//MESSAGE Failed to load screencast: jar:file:/H:/win32.win32.x86-3/eclipse/plugins/ugent.jcresearch.bpmn.experiments.cheetah.spmm2015_1.0.0.201502061120.jar!/resources/screencasts/fo/fo.htm

	}
	
	public static void incrError(Shell shell) {
		numErrors++;
		if (numErrors == 4) {
			MessageDialog.openInformation(shell, 
					format(Messages.AbstractTreatmentWizardPage_Error_popup_title), 
					format(Messages.AbstractTreatmentWizardPage_Error_popup_message));
		}
	}
	
	protected int getLearningStyle(int score) {
		if (score > 1)
			return SEQ;
		else if (score < -1)
			return GLOB;
		else
			return BETW;
	}
	protected int getFieldDependency(double score) {
		if (score >= 50)
			return FD;
		else
			return FID;
	}
	protected int getNeedForStructure1(double score) {
		if (score >= 3.5)
			return HNFS1;
		else
			return LNFS1;
	}

	protected void validate() {
		setErrorMessage(null);
		setPageComplete(true);
	}
	public void setControl(Composite control) {
		control = control.getParent();
		ScrolledComposite scroll = (ScrolledComposite)control.getParent(); 
		scroll.setContent(control);
		scroll.setMinSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		super.setControl(scroll.getParent());
	}
}
