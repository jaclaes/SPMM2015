package grailsServices;

import constants.CommonConstants;

/**
 * WrapperClass
 * to start Unix or Windows depended methods
 * @author szabo *
 */
public class GrailsUtils {	
	public static void execGrailsCommand(String cmd, String pathToGrailsBin) {
		String OS_NAME = System.getProperty("os.name");
		if (OS_NAME.equals("Linux")) {
			if (CommonConstants.OUTPUT) System.out.println("Now Using LinuxCommands!");
			GrailsUtilsUnix.execGrailsUnixCommand(cmd, pathToGrailsBin);
		}
		else
			GrailsUtilsWindows.execGrailsWindowsCommand(cmd, pathToGrailsBin);
	}
	
	public static void execGrailsCommandTarget(String cmd, String pathToGrailsBin, String projectName) {
		String OS_NAME = System.getProperty("os.name");
		if (OS_NAME.equals("Linux"))
			GrailsUtilsUnix.execGrailsUnixCommandTarget(cmd, pathToGrailsBin, projectName);
		else
			GrailsUtilsWindows.execGrailsWindowsCommandTarget(cmd, pathToGrailsBin, projectName);		
	}
	
	public static void execGrailsCommandTargetParam(String cmd, String pathToGrailsBin, String projectName) {
		String OS_NAME = System.getProperty("os.name");
		if (OS_NAME.equals("Linux"))
			GrailsUtilsUnix.execGrailsUnixCommandTargetParam(cmd, pathToGrailsBin, projectName);
		else
			GrailsUtilsWindows.execGrailsWindowsCommandTargetParam(cmd, pathToGrailsBin, projectName); //noch zu fixen TODO
	}
	
	public static void execGrailsCommandTargetParam(String cmd, String pathToGrailsBin, String projectName, String param) {
		String OS_NAME = System.getProperty("os.name");
		if (OS_NAME.equals("Linux"))
			GrailsUtilsUnix.execGrailsUnixCommandTargetParam(cmd, pathToGrailsBin, projectName, param);
		else
			GrailsUtilsWindows.execGrailsWindowsCommandTargetParam(cmd, pathToGrailsBin, projectName, param);
	}
	
	public static void execCommand(String cmd) {
		String OS_NAME = System.getProperty("os.name");
		if (OS_NAME.equals("Linux"))
			GrailsUtilsUnix.execUnixCommand(cmd);
		else
			GrailsUtilsWindows.execWindowsCommand(cmd);
	}	
}
