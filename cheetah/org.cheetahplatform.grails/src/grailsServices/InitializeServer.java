package grailsServices;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         15.01.2010
 */
public abstract class InitializeServer {
	public static void main(String[] args) {
		// delete all created data first in: workspace/org.cheetahplatform.grails/TestProject
		// delete all created data first in: C:/Users/user/.grails/1.0.4/projects
		GrailsStarter.setVars();
		GrailsTargets.createProject();
		GrailsTargets.createDomainFiles();
		GrailsTargets.generateAll();
		GrailsTargets.setGspFileLimiter();
		GrailsTargets.setExportGsp();
		GrailsTargets.setExportController();
		GrailsTargets.setExportButtonName();

		// additionally start server by hand (in workspace/org.cheetahplatform.grails/TestProject):
		// grails install-plugin ../grails-export-0.2.zip
		// grails run-app
	}
}
