package grailsServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import constants.CommonConstants;
import constants.GrailsConstants;

public class GrailsUtilsUnix {
	/**
	 * 
	 * @param cmd
	 * @param pathToGrailsBin
	 * @return
	 */
	public static boolean execGrailsUnixCommand(String cmd, String pathToGrailsBin) {
		try {
			String line;
			
			if (CommonConstants.OUTPUT) System.out.println("Trying to execute command: " + pathToGrailsBin + cmd);
			
			ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", pathToGrailsBin + cmd);
			processBuilder.directory(new File(GrailsConstants.MYPATH + "/"));
			Process p = processBuilder.start();			
						
			BufferedReader input =
				new BufferedReader
				(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
			return true;
		}
		catch (Exception err) {
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
	public static boolean execGrailsUnixCommandTarget(String cmd, String pathToGrailsBin, String projectName) {
		System.out.println(cmd);
		try {
			//ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", cmd);
//			Map<String, String> env = processBuilder.environment();
//			env.put("GRAILS_HOME", "/home/keule/Desktop/UNI/QE_bakk/grails/grails-1.0.3/");
//			env.put("JAVA_HOME", "/opt/sun-jdk-1.6.0.11/");

			ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", pathToGrailsBin + cmd);
			processBuilder.directory(new File(GrailsConstants.MYPATH + "/" + projectName + "/"));
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
	}
	
	/**
	 * 
	 * @param cmd
	 * @param pathToGrailsBin
	 * @param projectName
	 * @return
	 */
	public static boolean execGrailsUnixCommandTargetParam(String cmd, String pathToGrailsBin, String projectName) {
		try {
			cmd += " ";
			cmd += projectName;
			ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", pathToGrailsBin + cmd);			
			processBuilder.directory(new File(GrailsConstants.MYPATH + "/" + projectName));
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
	}
	
	public static boolean execGrailsUnixCommandTargetParam(String cmd, String pathToGrailsBin, String projectName, String param) {
		try {
			cmd += " ";
			cmd += param;
			//ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", cmd);
			ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", pathToGrailsBin + cmd);
			processBuilder.directory(new File(GrailsConstants.MYPATH + "/" + projectName));
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
	}
	
	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public static boolean execUnixCommand(String cmd) {
		if (CommonConstants.OUTPUT) System.out.println("\nTrying to execute command: \n" + cmd + "\n");
		try {
			String[] execCmd = {"/bin/sh", "-c", cmd};
			String line;
			Process p = Runtime.getRuntime().exec(execCmd);

			BufferedReader input =
				new BufferedReader
				(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
			return true;
		}
		catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}
}
