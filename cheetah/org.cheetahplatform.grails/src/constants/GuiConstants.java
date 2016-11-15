package constants;

import commonServices.StringTools;

public class GuiConstants {
	public static int WINDOWSIZE_X = 300;
	public static int WINDOWSIZE_Y = 400;
	
	public static final String SwingLookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
	//public static final String SwingLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	//public static final String SwingLookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	
	public static final String browserUriPrefix = "http://localhost:"+ GrailsConstants.PORT + "/" + StringTools.readProjectName(GrailsConstants.xmlFileName) + "/";
	
}
