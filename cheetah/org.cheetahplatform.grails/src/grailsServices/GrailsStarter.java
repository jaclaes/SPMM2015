package grailsServices;

import commonServices.StringTools;

import constants.CommonConstants;
import constants.GrailsConstants;

public class GrailsStarter {

	/**
	 * Calls each grails target, which are necessary for a comlete project creates everything and starts the server
	 * 
	 * @param pathToXmlFile
	 */
	public static void makeProjectFile() {
		// setVars();
		// GrailsTargets.createProject();
		// GrailsTargets.createDomainFiles();
		// GrailsTargets.generateAll();
		// GrailsTargets.setGspFileLimiter();
		// GrailsTargets.setExportGsp();
		// GrailsTargets.setExportController();
		// GrailsTargets.setExportButtonName();
		// GrailsTargets.installExportPlugin();
		// GrailsTargets.startServer();

		GrailsTargets.openForm("Hotel");
		System.out.println(GrailsTargets.getData("Hotel"));
	}

	/**
	 * Set the path to grails bin - grails must be in the root of the HOME dir of the user Set the projectname - just taking the name of the
	 * xml file
	 * 
	 * @param inputFormat
	 */
	public static void setVars() {
		if (CommonConstants.OUTPUT) {
			System.out.println("Now setting global VARS!");
		}
		String OS_NAME = System.getProperty("os.name");
		if (OS_NAME.equals("Linux")) {
			GrailsConstants.PATHTOGRAILSBIN = System.getProperty("user.home") + "/" + "grails/bin/";
		} else {
			// not Linux maybe Windows
			GrailsConstants.PATHTOGRAILSBIN = "C:\\grails\\bin\\";
		}

		// TODO: check if path is closen with a slash "/" Linux
		// TODO: check if the given order is NOT empty

		if (CommonConstants.OUTPUT) {
			System.out.println("PATHTOGRAILSBIN is set to: " + GrailsConstants.PATHTOGRAILSBIN);
		}
		GrailsConstants.PROJECTNAME = StringTools.readProjectName(GrailsConstants.xmlFileName);
		if (CommonConstants.OUTPUT) {
			System.out.println("ProjectName is set to: " + GrailsConstants.PROJECTNAME);
		}
	}

	public static void main(String[] args) {
		// System.out.println(GrailsConstants.xmlFileName);
		makeProjectFile();
		// GrailsTargets.installExportPlugin(GrailsConstants.xmlFileName);
		// GrailsTargets.installExportPlugin(GrailsConstants.xmlFileName);
	}
}
