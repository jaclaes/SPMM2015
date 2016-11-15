package grailsServices;

import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import commonServices.StringTools;
import commonServices.XmlParser;

import constants.CommonConstants;
import constants.GrailsConstants;
import constants.GuiConstants;
import dataTypes.Attribute;
import dataTypes.BusinessObject;
import dataTypes.Reference;

public class GrailsTargets {
	// private static String pathToGrailsBin = Main.PATHTOGRAILSBIN;

	/**
	 * 
	 * @param pathToXmlFile
	 */
	public static void createProject() {
		// if (!(new File(GrailsConstants.MYPATH+ "\\" + GrailsConstants.PROJECTNAME ).exists()))
		// GrailsUtils.execGrailsCommand("grails create-app " + GrailsConstants.PROJECTNAME, GrailsConstants.PATHTOGRAILSBIN);
		// else if (!(new File(GrailsConstants.MYPATH + "\\" + GrailsConstants.PROJECTNAME).isDirectory()))
		GrailsUtils.execGrailsCommand("grails create-app " + GrailsConstants.PROJECTNAME, GrailsConstants.PATHTOGRAILSBIN);
		// else
		// if (CommonConstants.OUTPUT) System.out.println("Project allready exists!");
	}

	/**
	 * Function searches for the BusinnesObjects in a given xml-file and create the domain files in the domain directory of grails simly
	 * using fileoutput and creating goovy-files
	 * 
	 * @param pathToXmlFile
	 */
	public static void createDomainFiles() {
		if (CommonConstants.OUTPUT) {
			System.out.println("\nBegin Creating Domain Files!\n");
		}

		List<BusinessObject> BOList = XmlParser.parseObjects(GrailsConstants.xmlFileName, "businessObjects");
		for (Iterator<BusinessObject> i = BOList.iterator(); i.hasNext();) {
			String code2 = "";
			String cmd = "";
			String cmd_additional = "";
			String name_additional = null;
			BusinessObject bo = i.next();

			String code = "class " + bo.getName() + " {\n";

			for (Iterator<Attribute> j = bo.getAttributes().iterator(); j.hasNext();) {
				Attribute a = j.next();
				// it have to be checked if the "max-value" is bigger then one of this attribute,
				// in that case an own domain have to be created.

				int max = 0;
				try {
					max = Integer.parseInt(a.getRange());
				} catch (Exception E) {
					if (CommonConstants.OUTPUT) {
						System.out.println("No Integer parsed for the max value in the given xml!");
					}
				}

				if (Integer.valueOf(max) > 1) {
					code2 += "class " + a.getName() + " {\n";
					code2 += "\t" + a.getType() + " " + a.getName() + "\n";
					code2 += "}\n";
					code += "\t" + a.getName() + " " + a.getName().toLowerCase() + "\n";
					name_additional = a.getName();
				} else {
					code += "\t" + a.getType() + " " + a.getName() + "\n";
				}
			}

			for (Iterator<Reference> j = bo.getReferences().iterator(); j.hasNext();) {
				Reference r = j.next();
				code += "\t" + r.getName() + " " + r.getName().toLowerCase() + "\n";
			}
			code += "}\n";
			cmd = "echo " + code + "" + " > " + GrailsConstants.MYPATH + "\\" + GrailsConstants.PROJECTNAME + "\\grails-app\\domain\\"
					+ bo.getName() + ".groovy";

			if (name_additional != null) {
				cmd_additional = "echo " + code2 + " > " + GrailsConstants.MYPATH + "\\" + GrailsConstants.PROJECTNAME
						+ "\\grails-app\\domain\\" + name_additional + ".groovy";
				if (CommonConstants.OUTPUT) {
					System.out.println(cmd_additional);
				}
				GrailsUtils.execCommand(cmd_additional);
			}

			if (CommonConstants.OUTPUT) {
				System.out.println(cmd);
			}
			GrailsUtils.execCommand(cmd);

		}
		if (CommonConstants.OUTPUT) {
			System.out.println("Searching for References!");
		}

		if (CommonConstants.OUTPUT) {
			System.out.println("\nEnd Creating Domain Files!\n");
		}
	}

	/**
	 * Generate all necessary things for the project like wiev an controller files
	 */
	public static void generateAll() {
		List<BusinessObject> BOList = XmlParser.parseObjects(GrailsConstants.xmlFileName, "businessObjects");
		for (Iterator<BusinessObject> i = BOList.iterator(); i.hasNext();) {
			BusinessObject bo = i.next();

			for (Iterator<Attribute> j = bo.getAttributes().iterator(); j.hasNext();) {
				Attribute a = j.next();
				if (a.getRange() != null) {
					int max = 0;
					try {
						max = Integer.parseInt(a.getRange());
					} catch (Exception E) {
						if (CommonConstants.OUTPUT) {
							System.out.println("No Integer parsed for the max value in the given xml!");
						}
					}

					if (Integer.valueOf(max) > 1) {
						GrailsUtils.execGrailsCommandTargetParam("grails generate-all", GrailsConstants.PATHTOGRAILSBIN,
								GrailsConstants.PROJECTNAME, a.getName());
					}
				}
			}

			GrailsUtils.execGrailsCommandTargetParam("grails generate-all", GrailsConstants.PATHTOGRAILSBIN, GrailsConstants.PROJECTNAME,
					bo.getName());
		}
	}

	/**
	 * Add export necessary things to the list.gsp files
	 * 
	 * @param pathToXmlFile
	 */
	public static void setExportGsp() {
		if (CommonConstants.OUTPUT) {
			System.out.println("\nBegin To Set Export-Things in GSP Files!\n");
		}
		String path = GrailsConstants.MYPATH + "\\" + StringTools.readProjectName(GrailsConstants.xmlFileName) + "\\grails-app\\views\\";
		File filesToLimit[] = new File(path).listFiles();
		if (filesToLimit != null) {
			try {
				for (int k = 0; k < filesToLimit.length; k++) {
					if (filesToLimit[k].isDirectory()) {
						File children[] = new File(path + "\\" + filesToLimit[k].getName()).listFiles(new ListNameFilter());
						if (children != null) {
							for (int l = 0; l < children.length; l++) {
								if (CommonConstants.OUTPUT) {
									System.out.println("changing file: " + children[l].getName());
								}

								// include the export functionality
								StringTools.setContents(children[l], // change this file
										StringTools.fillStringIntoStringBefore(StringTools.getContents(children[l]), "<html>", // delemiter
												GrailsConstants.gspExportInclude) // fill this in
										);

								// set the format of the export funtionality
								StringTools.setContents(children[l], // change this file
										StringTools.fillStringIntoStringBefore(StringTools.getContents(children[l]).replaceAll(
												"\\n[ \\t]+", ""), "</body>", // delemiter
												// "</div></body>", //delemiter
												GrailsConstants.gspExportFormat) // fill this in
										);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Add export necessary things to the list.gsp files
	 * 
	 * @param pathToXmlFile
	 */
	public static void setExportController() {
		if (CommonConstants.OUTPUT) {
			System.out.println("\nBegin To Set Export-Things in Controller Files!\n");
		}
		String path = GrailsConstants.MYPATH + "\\" + StringTools.readProjectName(GrailsConstants.xmlFileName)
				+ "\\grails-app\\controllers\\";
		File filesToLimit[] = new File(path).listFiles(new GroovyFilenameFilter());
		if (filesToLimit != null) {
			try {
				for (int k = 0; k < filesToLimit.length; k++) {
					if (CommonConstants.OUTPUT) {
						System.out.println("changing file: " + filesToLimit[k].getName());
					}

					// include the export functionality
					StringTools.setContents(filesToLimit[k], // change this file
							StringTools.fillStringIntoStringBefore(StringTools.getContents(filesToLimit[k]),
									"def index = { redirect(action:list,params:params) }", // delemiter
									GrailsConstants.controllerExportDef) // fill this in
							);

					// search for the controllerName
					String filename = filesToLimit[k].getName();
					filename = filename.substring(0, filename.indexOf("Controller"));

					String replace = GrailsConstants.controllerExport.replaceAll("PATH", GrailsConstants.MYPATH + "\\"
							+ StringTools.readProjectName(GrailsConstants.xmlFileName));
					replace = replace.replaceAll("DOMAINNAME", filename);

					// set the format of the export funtionality
					StringTools.setContents(filesToLimit[k], // change this file
							StringTools.fillStringIntoStringAfter(StringTools.getContents(filesToLimit[k]),
									"if(!params.max) params.max = 10", // delemiter
									replace) // fill this in
							);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void setExportButtonName() {
		File messageProperties = new File(GrailsConstants.MYPATH + "\\" + StringTools.readProjectName(GrailsConstants.xmlFileName)
				+ "\\grails-app\\i18n\\messages.properties");
		if (messageProperties.exists() && messageProperties.isFile()) {
			String fileContent = StringTools.getContents(messageProperties);
			try {
				StringTools.setContents(messageProperties, fileContent + "\ndefault.xml=" + GrailsConstants.exportButtonName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void installExportPlugin() {
		File exportDir = new File(GrailsConstants.MYPATH + "\\" + StringTools.readProjectName(GrailsConstants.xmlFileName) + "\\"
				+ GrailsConstants.exportDirName);
		if (!exportDir.exists()) {
			exportDir.mkdir();
		}
		GrailsUtils.execGrailsCommandTarget("grails install-plugin " + GrailsConstants.exportPluginPath, GrailsConstants.PATHTOGRAILSBIN,
				StringTools.readProjectName(GrailsConstants.xmlFileName));
	}

	/**
	 * Generate limiters in GSP Files
	 * 
	 * @param pathToXmlFile
	 */
	public static void setGspFileLimiter() {
		if (CommonConstants.OUTPUT) {
			System.out.println("\nBegin To Set Limits in GSP Files!\n");
		}
		List<BusinessObject> BOList = XmlParser.parseObjects(GrailsConstants.xmlFileName, "businessObjects");
		for (Iterator<BusinessObject> i = BOList.iterator(); i.hasNext();) {
			BusinessObject bo = i.next();
			for (Iterator<Attribute> j = bo.getAttributes().iterator(); j.hasNext();) {
				Attribute a = j.next();
				int max = 0;
				try {
					max = Integer.parseInt(a.getRange());
				} catch (Exception E) {
					if (CommonConstants.OUTPUT) {
						System.out.println("No Integer parsed for the max value in the given xml!");
					}
				}

				if (Integer.valueOf(max) > 1) {
					// NOW SET the LIMIT inside the GSP File
					String limiterString = (new Integer(max)).toString();
					String myGspIfString = GrailsConstants.gspIfString.replace("myLimiter", limiterString);
					myGspIfString = myGspIfString.replace("DOMAINNAME", a.getName());
					String myGspMenuPoint = GrailsConstants.gspMenuPoint.replace("DOMAINNAME", a.getName());

					if (CommonConstants.OUTPUT) {
						System.out.println("myGspIfString = " + myGspIfString);
					}
					if (CommonConstants.OUTPUT) {
						System.out.println("myGspMenuPoint = " + myGspMenuPoint);
					}
					if (CommonConstants.OUTPUT) {
						System.out.println(GrailsConstants.MYPATH + "\\" + GrailsConstants.PROJECTNAME + "\\grails-app\\views\\"
								+ a.getName().toLowerCase());
					}

					String myConvertedName = a.getName();
					String mysub = myConvertedName.substring(0, 1);
					String mysub2 = myConvertedName.substring(1, myConvertedName.length());
					mysub.toLowerCase();
					myConvertedName = mysub.toLowerCase() + mysub2;

					File filesToLimit[] = new File(GrailsConstants.MYPATH + "\\" + GrailsConstants.PROJECTNAME + "\\grails-app\\views\\"
							+ myConvertedName).listFiles(new GspFilenameFilter());
					if (filesToLimit != null) {
						try {
							for (int k = 0; k < filesToLimit.length; k++) {
								if (CommonConstants.OUTPUT) {
									System.out.println("changing file: " + filesToLimit[k].getName());
								}
								if (StringTools.getContents(filesToLimit[k]).contains(myGspMenuPoint)) {
									if (CommonConstants.OUTPUT) {
										System.out.println("content find now replace and add things!");
									}
									StringTools.setContents(filesToLimit[k], StringTools.fillStringIntoStringBefore(StringTools
											.getContents(filesToLimit[k]), myGspMenuPoint, myGspIfString));
									StringTools.setContents(filesToLimit[k], StringTools.fillStringIntoStringAfter(StringTools
											.getContents(filesToLimit[k]), myGspMenuPoint, GrailsConstants.gspIfStringEnd));
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}

		if (CommonConstants.OUTPUT) {
			System.out.println("\nEnd Set Limits in GSP Files!\n");
		}
	}

	/**
	 * 
	 * @param pathToXmlFile
	 */
	public static void generateViews() {
		GrailsUtils.execGrailsCommandTargetParam("grails generate-views", GrailsConstants.PATHTOGRAILSBIN, GrailsConstants.PROJECTNAME);
	}

	/**
	 * 
	 * @param pathToXmlFile
	 */
	public static void startServer() {
		GrailsUtils.execGrailsCommandTarget("grails run-app", GrailsConstants.PATHTOGRAILSBIN, GrailsConstants.PROJECTNAME);
	}

	public static void openForm(String MyObjectName) {
		try {
			Desktop.getDesktop().browse(new URI(GuiConstants.browserUriPrefix + MyObjectName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getData(String MyObjectName) {
		File myFile = new File(GrailsConstants.controllerExportPath + "\\" + MyObjectName + ".xml");

		if (myFile.exists()) {
			return StringTools.getContents(myFile);
		} else {
			return "";
		}
	}
}

/**
 * 
 * @author szabo
 * 
 */
class GspFilenameFilter implements FilenameFilter {
	public boolean accept(File f, String s) {
		return s.toLowerCase().endsWith(".gsp");
	}
}

class ListNameFilter implements FilenameFilter {
	public boolean accept(File f, String s) {
		return s.toLowerCase().contains("list");
	}
}

class GroovyFilenameFilter implements FilenameFilter {
	public boolean accept(File f, String s) {
		return s.toLowerCase().endsWith(".groovy");
	}
}