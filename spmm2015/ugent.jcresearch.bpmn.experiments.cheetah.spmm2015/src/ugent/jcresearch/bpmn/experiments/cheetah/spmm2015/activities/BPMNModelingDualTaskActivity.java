package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.activities;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.cheetahplatform.common.PartListenerAdapter;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.Process;
import org.cheetahplatform.common.logging.xml.XMLLogHandler;
import org.cheetahplatform.modeler.bpmn.BPMNModelingActivity;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;


public class BPMNModelingDualTaskActivity extends BPMNModelingActivity {

	private String caseName;
	// 200 numbers
	private static final int[] numbers = { 67, 92, 56, 88, 62, 50, 97, 75, 54, 91, 39, 40, 86, 91, 55, 43, 62, 23, 99, 77, 
										   48, 83, 23, 63, 19, 70, 36, 65, 77, 98, 55, 87, 89, 60, 56, 79, 24, 66, 38, 92, 
										   22, 65, 20, 63, 26, 97, 74, 64, 36, 85, 59, 13, 23, 27, 78, 95, 10, 36, 75, 69, 
										   43, 59, 19, 83, 52, 63, 53, 35, 10, 24, 62, 16, 92, 60, 79, 28, 33, 56, 87, 69, 
										   75, 54, 76, 74, 19, 28, 40, 67, 49, 44, 10, 31, 30, 17, 44, 43, 94, 42, 36, 73,
										   45, 96, 27, 50, 75, 63, 62, 17, 12, 42, 55, 10, 41, 67, 70, 79, 52, 29, 50, 98, 
										   25, 30, 37, 17, 74, 33, 46, 28, 60, 93, 91, 68, 32, 43, 30, 87, 25, 91, 76, 31, 
										   32, 18, 95, 75, 16, 31, 96, 36, 89, 23, 63, 55, 57, 86, 68, 67, 58, 68, 29, 48, 
										   14, 75, 29, 66, 33, 63, 31, 96, 23, 47, 72, 63, 52, 77, 65, 48, 37, 23, 52, 20, 
										   40, 81, 36, 78, 82, 40, 79, 61, 25, 21, 61, 73, 49, 20, 11, 85, 45, 77, 72, 62 };
	private static int numCount = (int)Math.round(Math.random()*(numbers.length-1));
	private int nextCnt() {
//sync threads?
		numCount++;
		numCount = numCount % numbers.length;
		return numCount;
	}
	private int getPrevCnt() {
		return (numCount + numbers.length - 1) % numbers.length;
	}
	private static final String DUAL_TASK = "DUAL TASK";
	
	public BPMNModelingDualTaskActivity(Graph initialGraph, Process process, String caseName) {
		super(initialGraph, process);
		this.caseName = caseName;
	}

	public BPMNModelingDualTaskActivity(Graph initialGraph, Process process, boolean optional, String caseName) {
		super(initialGraph, process, optional);
		this.caseName = caseName;
	}

	public BPMNModelingDualTaskActivity(InputStream initialProcess, Graph initialGraph, Process process, String caseName) throws Exception {
		super(initialProcess, initialGraph, process);
		this.caseName = caseName;
	}

	public BPMNModelingDualTaskActivity(InputStream initialProcess, Graph initialGraph, Process process, boolean optional, String caseName) throws Exception {
		super(initialProcess, initialGraph, process, optional);
		this.caseName = caseName;
	}
	
	private static boolean started = false;
	private static boolean pause = false;
	protected void doExecute() {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {				
			LoggingValidator logValidator = new LoggingValidator(new Process("DUAL TASK - " + caseName), "DUAL TASK - " + caseName);
			@Override
			public void run() {
				if (pause)
					return;

				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							pause = true;
							Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
							if (!started) {
								String message = "\n\nDuring modeling you will have to remember numbers, one at a time. \n"
						        		+ "At regular times the tool will ask for these numbers. \n\n"
						        		+ "The first number to remember is     " + numbers[numCount] + "\n\n";
								DualTaskDialog.openDialog(shell, message, numbers[numCount], false);
						        nextCnt();
						        started = true;
							}
							else {
						        String message = "\n\nThe next number to remember is     " + numbers[numCount] + "\n";
								int number = DualTaskDialog.openDialog(shell, message, numbers[numCount], true);
								AuditTrailEntry entry = new AuditTrailEntry(DUAL_TASK);
								entry.setAttribute("correct_number", numbers[getPrevCnt()]);
								entry.setAttribute("provided_number", number);
								if (number != -1 && number == numbers[getPrevCnt()])
									entry.setAttribute("dual_task_correct", "TRUE");
								else
									entry.setAttribute("dual_task_correct", "FALSE");
								logValidator.log(entry);
						        nextCnt();
							}
							pause = false;
						}
						catch(Exception ex) {
							ex.printStackTrace();
							pause = false;
						}
					}
				});
			};

		}, 2 * 60 * 1000, 2 * 60 * 1000); // 2 min
		XMLLogHandler.setFilenameBase(XMLLogHandler.getFilenameBase() + "-MODEL");
		String msg = org.cheetahplatform.modeler.Messages.AbstractModelingActivity_5;
		org.cheetahplatform.modeler.Messages.AbstractModelingActivity_5 = "Now open the case description for CASE " + caseName + ".";
		super.doExecute();
		org.cheetahplatform.modeler.Messages.AbstractModelingActivity_5 = msg;

		final WorkbenchWindow window = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		window.getActivePage().addPartListener(new PartListenerAdapter() {

			@Override
			public void partClosed(IWorkbenchPart part) {
				timer.cancel();
			}
		});
	}
	
	private static class DualTaskDialog extends TitleAreaDialog {
		private static final Color BACKGROUND = new Color(null, 75, 172, 198);
		private StyledText txtText;
		private Text txtNumber;
		
		private String text;
		private String boldText;
		private boolean showNumber;
		private int number;
		
		  public DualTaskDialog(Shell parentShell) {
		    super(parentShell);
		    text = "text";
		    boldText = null;
		    showNumber = false;
		  }

		  @Override
		  public void create() {
		    super.create();
		    setTitle("Attention");
		    //setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION);
		    getButton(IDialogConstants.CANCEL_ID).setVisible(false);
		    getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
		    getShell().setDefaultButton(getButton(IDialogConstants.CANCEL_ID));
		    getShell().addListener(SWT.Traverse, new Listener() {
		    	public void handleEvent(Event e) {
		    		if (e.detail == SWT.TRAVERSE_ESCAPE) {
		    			e.doit = false;
	    			}
	    		}
	    	});
		  }

		  @Override
		  protected Control createDialogArea(Composite parent) {
		    Composite area = (Composite) super.createDialogArea(parent);
		    Composite container = new Composite(area, SWT.NONE);
		    container.setBackground(BACKGROUND);
		    container.setLayoutData(new GridData(GridData.FILL_BOTH));
		    GridLayout layout = new GridLayout(2, false);
		    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		    container.setLayout(layout);

		    createMessageArea(container);
		    createAskNumberArea(container);

		    if (txtNumber != null)
		    	txtNumber.setFocus();
		    
		    return area;
		  }
		  private void createMessageArea(Composite container) {
			  txtText = new StyledText(container, SWT.TRANSPARENT | SWT.READ_ONLY);
			  txtText.setBackground(BACKGROUND);
			  GridData gridData = new GridData();
			  gridData.horizontalSpan = 2;
			  txtText.setLayoutData(gridData);
			  txtText.setText(text);
			  if (boldText != null && !boldText.equals("")) {
				StyleRange styleRangeBold = new StyleRange();
				styleRangeBold.start = text.indexOf(boldText);
				styleRangeBold.length = boldText.length();
				styleRangeBold.fontStyle = SWT.BOLD;
				Font f = txtText.getFont();
				FontData[] fDs = f.getFontData();
				for (FontData fD : fDs)
					fD.setHeight(24);
				styleRangeBold.font = new Font(f.getDevice(), fDs);
				txtText.setStyleRange(styleRangeBold);
			  }
		  }
		  private void createAskNumberArea(Composite container) {
			  if (!showNumber) 
				  return;
			  
			    Label askNumber = new Label(container, SWT.NORMAL);
			    askNumber.setText("Please enter the previous number which you were asked to remember: ");
			    askNumber.setBackground(BACKGROUND);
			    txtNumber = new Text(container, SWT.BORDER);
			    txtNumber.addListener(SWT.Verify, new Listener() {
			        public void handleEvent(Event e) {
			          String string = e.text;
			          char[] chars = new char[string.length()];
			          string.getChars(0, chars.length, chars, 0);
			          for (int i = 0; i < chars.length; i++) {
			            if (!('0' <= chars[i] && chars[i] <= '9')) {
			              e.doit = false;
			              return;
			            }
			          }
			        }
			      });
			      Label empty = new Label(container, SWT.NORMAL);
			      empty.setText("\n");
			      empty.setBackground(BACKGROUND);
		  }
		  private void setText(String text, String boldText) {
			  this.text = text;
			  this.boldText = boldText;
		  }
		  private void showNumber(boolean showNumber) {
			  this.showNumber = showNumber;
		  }

		  @Override
		  protected void okPressed() {
		    saveInput();
		    super.okPressed();
		  }
		  private void saveInput() {
			  try {
				  number = Integer.parseInt(txtNumber.getText());
			  }
			  catch (Exception ex) {
				  number = -1;
			  }
		  }
		  public int getNumber() {
			  return number;
		  }
		  
		  public static void openDialog(Shell shell, String message) {
			  DualTaskDialog dialog = new DualTaskDialog(shell);
			  dialog.setText(message, null);
			  dialog.showNumber(false);
			  dialog.create();
			  dialog.open();
		  }
		  public static int openDialog(Shell shell, String message, int num, boolean askNumber) {
			  DualTaskDialog dialog = new DualTaskDialog(shell);
			  dialog.setText(message, "" + num);
			  dialog.showNumber(askNumber);
			  dialog.create();
			  if (dialog.open() == Window.OK && askNumber)
				  return dialog.getNumber();
			  else
				  return -1;
		  }
	}
}