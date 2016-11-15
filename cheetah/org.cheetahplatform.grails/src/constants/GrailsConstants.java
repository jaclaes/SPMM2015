package constants;

public class GrailsConstants {

	public static String MYPATH = "C:";
	// public static String MYPATH = System.getProperty("user.dir");

	// public static final String xmlFileName = MYPATH + "\\" + "Desktop\\deployment.acmodel";
	public static final String xmlFileName = "C:/initial.acmodel";
	// public static final String xmlFileName = "\"" + System.getProperty("user.dir") + "\\initial.acmodel\"";

	public static String PATHTOGRAILSBIN = "C:\\grails\\bin";

	public static int PORT = 8080;

	public static String PROJECTNAME = "TestProject";

	public static String gspMenuPoint = "<span class=\"menuButton\"><g:link class=\"create\" action=\"create\">New DOMAINNAME</g:link></span>";
	public static String gspIfString = "<g:set var=\"anzahl\" value=\"${DOMAINNAME.count()}\" /><g:if test=\"${anzahl < myLimiter}\">";
	public static String gspIfStringEnd = "</g:if>";

	public static String gspExportInclude = "<export:resource />";
	public static String gspExportFormat = "<export:formats formats=\"['xml']\" />";

	public static String controllerExportDef = "def exportService";

	public static String exportDirName = "export";

	public static String controllerExportPath = (MYPATH + "\\" + PROJECTNAME + "\\" + exportDirName + "\\").replace("\\", "\\\\");

	public static String controllerExport = "if(params?.format && params.format != \"html\"){ "
			+ "OutputStream output = new FileOutputStream(\"" + controllerExportPath + "DOMAINNAME.${params.format}\"); "
			+ "exportService.export(params.format, output, DOMAINNAME.list(params), [:], [:]) }";

	public static String exportPluginPath = "\"C:\\" + "grails-export-0.2.zip\"";

	public static String exportButtonName = "FERTIG";

	public static int checkTimeInMs = 500;

	/**
	 * You also have to set ENVIROMENT variables: GRAILS_HOME=/path/to/your/grails/home/dir/ - i.E /home/user/grails-1.0.4
	 * JAVA_HOME=/path/to/your/java/home/dir/ - i.E. /opt/java...
	 */

}
