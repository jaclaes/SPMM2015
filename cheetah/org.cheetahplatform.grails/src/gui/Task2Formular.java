package gui;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import dataTypes.InputObject;
import constants.GrailsConstants;
import constants.GuiConstants;

@SuppressWarnings("serial")
public class Task2Formular extends JPanel implements ActionListener {
    
    public static JButton [] buttons;
    public static JFrame frame;
    public static String myActivity = null;
    public static boolean GOAHEAD = false;
    
    public static String StartFormular(final LinkedList <String> myList) {    	
    	try {
            UIManager.setLookAndFeel(GuiConstants.SwingLookAndFeel);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(myList);
            }
        });
        
        while (!GOAHEAD){
        	
     
        }
        
        String returnValue = myActivity;
        GOAHEAD = false;
        myActivity = null;
        
        return returnValue;
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI(LinkedList <String> myList) {
        //Create and set up the window.
        frame = new JFrame("Task2Formular");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        JComponent newContentPane = new Task2Formular(myList);
        
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public Task2Formular(LinkedList <String> myList) {
        super(new GridLayout(0,1));
        
        int range = myList.size();
        
        buttons = new JButton[range];
        
        for (int i=0;i < buttons.length;i++)
        	buttons[i] = null;
		
        for (int i=0;i < buttons.length;i++) {
        	buttons[i] = new JButton(myList.get(i));
        	add(buttons[i]);
        	buttons[i].addActionListener(this);
		}
        
        setPreferredSize(new Dimension(GuiConstants.WINDOWSIZE_X, GuiConstants.WINDOWSIZE_Y));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }
    
    public void actionPerformed(ActionEvent e) {

    	//e.getActionCommand() hold the name of the button = the name of theactivity

    	List <InputObject> IOL = null;
    	IOL = commonServices.XmlParser.search4ActivityReturnInputObjectList( GrailsConstants.xmlFileName, e.getActionCommand());

    	if (IOL != null) { //if IOL not empty
    		try {
    			InputObject io = null;
    			for (Iterator <InputObject> i = IOL.iterator(); i.hasNext();) {
    				io = i.next();
    				System.out.println("Try to open browser with : " + GuiConstants.browserUriPrefix + io.getBOName().toLowerCase());
    				Desktop.getDesktop().browse(new URI( GuiConstants.browserUriPrefix + io.getBOName().toLowerCase()));
    			}
    			

    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (URISyntaxException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    	}
    	
    	myActivity = e.getActionCommand();
    	frame.dispose();    	
    	//GOAHEAD = true;

    	
    	if (buttons != null){
    		for (int i=0;i < buttons.length;i++) {                
    			buttons[i].removeActionListener(this);
    			remove(buttons[i]);
    		}
    		buttons = null;
    	}

   	
    	
    	
    	if (!e.getActionCommand().equals("EXECUTED")){
    		System.out.println("starting new");
    		JButton execute = new JButton("EXECUTED");
    		add(execute);
    		execute.addActionListener(this);
    		frame.pack();
    		frame.setVisible(true);
    	}
    	else {
    		System.out.println("clicked executed");
    		frame.dispose();
    		Task2Formular.GOAHEAD = true;
    	}
    }
}
