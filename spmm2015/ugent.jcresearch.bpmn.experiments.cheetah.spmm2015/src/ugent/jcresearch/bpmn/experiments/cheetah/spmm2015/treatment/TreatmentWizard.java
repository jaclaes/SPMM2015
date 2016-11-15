package ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.treatment;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import ugent.jcresearch.bpmn.experiments.cheetah.spmm2015.LoggingValidator;

public class TreatmentWizard extends Wizard {
	public static final String DURATION = "_duration"; //$NON-NLS-1$
	public static final String ATTRIBUTE_CHOICE_SEPARATION = "_"; //$NON-NLS-1$

	private LoggingValidator logValidator;
	private boolean test;
	private int ls;
	private double fd, nfs1, nfs2;

	public TreatmentWizard(LoggingValidator logValidator, boolean test) {
		this.logValidator = logValidator;
		this.test = test;
		this.ls = 0;
		this.fd = 0;
		this.nfs1 = 0;
		this.nfs2 = 0;
		setWindowTitle("Tutorial");
	}
	public TreatmentWizard(LoggingValidator logValidator, int ls, double fd, double nfs1, double nfs2) {
		this.logValidator = logValidator;
		this.test = false;
		this.ls = ls;
		this.fd = fd;
		this.nfs1 = nfs1;
		this.nfs2 = nfs2;
		setWindowTitle("Tutorial");
	}

	@Override
	public void addPages() {
		addPage(new IntroductionWizardPage(logValidator, "INTRO"));
		
		addPage(new TitleWizardPage(logValidator, "LS00", "Learning Style"));
		
		addPage(new LearningStyle1WizardPage(logValidator, "LS01"));
		
		if (!test)
			addPage(new LearningStyle2WizardPage(logValidator, "LS02", ls));
		else {
			addPage(new LearningStyle2WizardPage(logValidator, "LS02", 7));
			addPage(new LearningStyle2WizardPage(logValidator, "LS02", 0));
			addPage(new LearningStyle2WizardPage(logValidator, "LS02", -7));
		}
		
		addPage(new LearningStyle3WizardPage(logValidator, "LS03"));
		
		if (!test)
			addPage(new LearningStyle4WizardPage(logValidator, "LS04", ls));
		else {
			addPage(new LearningStyle4WizardPage(logValidator, "LS04", 7));
			addPage(new LearningStyle4WizardPage(logValidator, "LS04", 0));
			addPage(new LearningStyle4WizardPage(logValidator, "LS04", -7));
		}
		
		addPage(new LearningStyle5WizardPage(logValidator, "LS05"));
		
		if (!test)
			addPage(new LearningStyle6WizardPage(logValidator, "LS06", ls));
		else {
			addPage(new LearningStyle6WizardPage(logValidator, "LS06", 7));
			addPage(new LearningStyle6WizardPage(logValidator, "LS06", 0));
			addPage(new LearningStyle6WizardPage(logValidator, "LS06", -7));
		}
		
		if (!test)
			addPage(new LearningStyle7WizardPage(logValidator, "LS07", ls));
		else {
			addPage(new LearningStyle7WizardPage(logValidator, "LS07", 7));
			addPage(new LearningStyle7WizardPage(logValidator, "LS07", 0));
			addPage(new LearningStyle7WizardPage(logValidator, "LS07", -7));
		}
		
		LearningStyle8WizardPage pageLS = new LearningStyle8WizardPage(logValidator, "LS08", ls); 
		addPage(pageLS);
		
		addPage(new TitleWizardPage(logValidator, "QUIZ00", "Quiz time!"));
		addPage(new Quiz1WizardPage(logValidator, "QUIZ01"));
		if (!test)
			addPage(new Quiz2WizardPage(logValidator, "QUIZ02", ls));
		else {
			addPage(new Quiz2WizardPage(logValidator, "QUIZ02", 7));
			addPage(new Quiz2WizardPage(logValidator, "QUIZ02", 0));
			addPage(new Quiz2WizardPage(logValidator, "QUIZ02", -7));
		}
		if (!test)
			addPage(new Quiz3WizardPage(logValidator, "QUIZ03", ls));
		else {
			addPage(new Quiz3WizardPage(logValidator, "QUIZ03", 7));
			addPage(new Quiz3WizardPage(logValidator, "QUIZ03", 0));
			addPage(new Quiz3WizardPage(logValidator, "QUIZ03", -7));
		}
		
		addPage(new TitleWizardPage(logValidator, "FD00", "Field dependency"));
		addPage(new FieldDependency1WizardPage(logValidator, "FD01"));
		if (!test)
			addPage(new FieldDependency2WizardPage(logValidator, "FD02", fd));
		else {
			addPage(new FieldDependency2WizardPage(logValidator, "FD02", 50));
			addPage(new FieldDependency2WizardPage(logValidator, "FD02", 1));
		}
		if (!test)
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", fd, ls));
		else {
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", 50, 7));
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", 50, 0));
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", 50, -7));
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", 1, 7));
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", 1, 0));
			addPage(new FieldDependency3WizardPage(logValidator, "FD03", 1, -7));
		}
		FieldDependency4WizardPage pageFD = new FieldDependency4WizardPage(logValidator, "FD04", fd); 
		addPage(pageFD);
		
		addPage(new TitleWizardPage(logValidator, "DFS00", "Desire for structure"));
		addPage(new NeedForStructure1WizardPage(logValidator, "DFS01"));
		if (!test)
			addPage(new NeedForStructure2WizardPage(logValidator, "DFS02", nfs1));
		else {
			addPage(new NeedForStructure2WizardPage(logValidator, "DFS02", 6));
			addPage(new NeedForStructure2WizardPage(logValidator, "DFS02", 1));
		}
		if (!test)
		addPage(new NeedForStructure3WizardPage(logValidator, "DFS03", nfs1));
		else {
			addPage(new NeedForStructure3WizardPage(logValidator, "DFS03", 6));
			addPage(new NeedForStructure3WizardPage(logValidator, "DFS03", 1));
		}
		NeedForStructure4WizardPage pageNFS1 = new NeedForStructure4WizardPage(logValidator, "DFS04"); 
		addPage(pageNFS1);
		
		addPage(new TitleWizardPage(logValidator, "QUIZ00b", "Quiz time!"));
		if (!test)
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", fd, ls));
		else {
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", 50, 7));
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", 50, 0));
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", 50, -7));
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", 1, 7));
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", 1, 0));
			addPage(new Quiz4WizardPage(logValidator, "QUIZ4", 1, -7));
		}
		
		addPage(new TitleWizardPage(logValidator, "S00", "Wrap up"));
		if (!test)
			addPage(new Summary1WizardPage(logValidator, "S01", ls, fd, nfs1));
		else {
			addPage(new Summary1WizardPage(logValidator, "S01", 7, 50, 6));
			addPage(new Summary1WizardPage(logValidator, "S01", 0, 50, 6));
			addPage(new Summary1WizardPage(logValidator, "S01", -7, 50, 6));
			addPage(new Summary1WizardPage(logValidator, "S01", 7, 1, 6));
			addPage(new Summary1WizardPage(logValidator, "S01", 0, 1, 6));
			addPage(new Summary1WizardPage(logValidator, "S01", -7, 1, 6));
			addPage(new Summary1WizardPage(logValidator, "S01", 7, 50, 1));
			addPage(new Summary1WizardPage(logValidator, "S01", 0, 50, 1));
			addPage(new Summary1WizardPage(logValidator, "S01", -7, 50, 1));
			addPage(new Summary1WizardPage(logValidator, "S01", 7, 1, 1));
			addPage(new Summary1WizardPage(logValidator, "S01", 0, 1, 1));
			addPage(new Summary1WizardPage(logValidator, "S01", -7, 1, 1));
		}
		addPage(new Summary2WizardPage(logValidator, "S02", pageLS, pageFD, pageNFS1));
	}
	public void setLS(int ls) {
		this.ls = ls;
	}
	public int getLS() {
		return ls;
	}
	public void setFD(double fd) {
		this.fd = fd;
	}
	public double getFD() {
		return fd;
	}
	public void setNFS1(double nfs1) {
		this.nfs1 = nfs1;
	}
	public double getNFS1() {
		return nfs1;
	}
	public void setNFS2(double nfs2) {
		this.nfs2 = nfs2;
	}
	public double getNFS2() {
		return nfs2;
	}
	
	public int getPageNumber(AbstractTreatmentWizardPage searchPage) {
		IWizardPage[] pages = getPages();
		for (int i=0; i<pages.length; i++) {
			IWizardPage page = pages[i];
			if (page.equals(searchPage))
				return i + 1;
		}
		return 1;
	}
	public int getTotalPages() {
		return getPageCount();
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}
	
	public void log(AuditTrailEntry entry) {
		logValidator.log(entry);
	}
}
