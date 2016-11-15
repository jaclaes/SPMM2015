package grailsServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import constants.CommonConstants;
import constants.GrailsConstants;

public class GrailsUtilsWindows {
	/**
	 * 
	 * @param cmd
	 * @param pathToGrailsBin
	 * @return
	 */
	public static boolean execGrailsWindowsCommand(String cmd, String pathToGrailsBin) {
		try {
			if (cmd.contains("grails create-app")) {
				String[] execCmd = { pathToGrailsBin + "grails.bat", "create-app", GrailsConstants.PROJECTNAME };
				String line;
				// Process p = Runtime.getRuntime().exec(execCmd);

				ProcessBuilder processBuilder = new ProcessBuilder(execCmd);
				processBuilder.directory(new File("C:" + "\\"));
				Process p = processBuilder.start();

				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line);
				}
				input.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @param cmd
	 * @param pathToGrailsBin
	 * @param projectName
	 * @return
	 */
	public static boolean execGrailsWindowsCommandTarget(String cmd, String pathToGrailsBin, String projectName) {
		if (cmd.contains("grails install-plugin")) {
			try {
				String[] execCmd = { pathToGrailsBin + "grails.bat", "install-plugin", GrailsConstants.exportPluginPath };
				ProcessBuilder processBuilder = new ProcessBuilder(execCmd);
				processBuilder.directory(new File("C:" + "\\" + projectName + "\\"));
				Process p = processBuilder.start();

				InputStream is = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}

				return true;
			} catch (Exception err) {
				err.printStackTrace();
				return false;
			}
		} else if (cmd.contains("grails run-app")) {
			// grails -Dserver.port=9090 run-app
			try {
				// String[] execCmd = {pathToGrailsBin + "grails.bat", "-Dserver.port=" + GrailsConstants.PORT, "run-app"};
				// String[] execCmd = {"cmd.exe", "/C", "server.bat" };
				String[] execCmd = { "cmd.exe", "/C", "grails.bat", "-Dserver.port=" + GrailsConstants.PORT, "run-app" };
				ProcessBuilder processBuilder = new ProcessBuilder(execCmd);
				processBuilder.directory(new File("C:" + "\\" + projectName + "\\"));
				if (CommonConstants.OUTPUT) {
					System.out.println(processBuilder.command().toString());
				}
				Process p = processBuilder.start();

				InputStream is = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}

				return true;
			} catch (Exception err) {
				err.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean execGrailsWindowsCommandTargetParam(String cmd, String pathToGrailsBin, String projectName, String param) {
		if (cmd.contains("grails generate-all")) {
			try {
				String[] execCmd = { pathToGrailsBin + "grails.bat", "generate-all", param };
				ProcessBuilder processBuilder = new ProcessBuilder(execCmd);
				processBuilder.directory(new File("C:" + "\\" + projectName + "\\"));
				Process p = processBuilder.start();

				InputStream is = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;

				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				return true;
			}

			catch (Exception err) {
				err.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public static boolean execWindowsCommand(String cmd) {
		if (cmd.contains("echo")) {

			String[] cmdParts = cmd.split(" > ");
			// if (CommonConstants.OUTPUT) System.out.println("cmd0 : " + cmdParts[0]);
			// if (CommonConstants.OUTPUT) System.out.println("cmd1 : " + cmdParts[1]);

			String fileContent = cmdParts[0].substring(5);

			try {
				{
					final java.io.PrintWriter file = new java.io.PrintWriter(cmdParts[1]);
					file.println(fileContent);
					file.close();
				}

				return true;
			} catch (Exception err) {
				err.printStackTrace();
				return false;
			}
		} else {
			try {
				String[] execCmd = { cmd };
				String line;
				Process p = Runtime.getRuntime().exec(execCmd);

				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					System.out.println(line);
				}
				input.close();
				return true;
			} catch (Exception err) {
				err.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * 
	 * @param cmd
	 * @param pathToGrailsBin
	 * @param projectName
	 * @return
	 */

	public static boolean execGrailsWindowsCommandTargetParam(String cmd, String pathToGrailsBin, String projectName) {
		try {
			cmd += " ";
			cmd += projectName;
			ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", pathToGrailsBin + cmd);
			processBuilder.directory(new File(GrailsConstants.MYPATH + "\\" + projectName));
			Process p = processBuilder.start();

			InputStream is = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

			return true;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}
}
