package gui;

import java.util.LinkedList;

public class myTestClass {
	
	//public static boolean GOAHEAD = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LinkedList <String> myList = new LinkedList<String>();				
		myList.add("button1");
		myList.add("button2");
		myList.add("newActivity");
		myList.add("szaboActivity");
		myList.add("button5");
		myList.add("button6");
		
		
		//Task2Formular.StartFormular(myList);
		
		//while (!GOAHEAD){}
		
		System.out.println(Task2Formular.StartFormular(myList));

	}

}
