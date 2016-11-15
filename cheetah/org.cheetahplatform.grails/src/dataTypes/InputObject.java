package dataTypes;

import java.util.List;

import commonServices.XmlParser;

import constants.GrailsConstants;

public class InputObject {
	
	String name;
	String min;
	String max;
	String BONr;
	String BOName;
	
	public String getBOName() {
		return BOName;
	}

	public InputObject(String name, String min, String max, String nr) {
		this.name = name.substring(0,1).toUpperCase() + name.substring(1);
		this.min = min;
		this.max = max;
		
		String myNumber = "";

		String [] temp = nr.split("/@");
		if (temp.length > 0){
			String [] temp2 = temp[temp.length - 1].split("businessObjects.");
			if (temp2.length > 0){
				myNumber = temp2[temp2.length - 1].trim();
			}
		}
		
		this.BONr = myNumber;
		
		int zahl = Integer.parseInt(myNumber);		
		List <BusinessObject> BOList = XmlParser.parseObjects(GrailsConstants.xmlFileName, "businessObjects");
		
		String myName = BOList.get(zahl).getName();
		this.BOName = myName.substring(0,1).toUpperCase() + myName.substring(1);
		
		//this.BOName = BOList.get(zahl).getName();
	}
	
	public InputObject(String name){
		this.name = name;
	}

	public String getMin() {
		return min;
	}

	public String getMax() {
		return max;
	}

	public String getBONr() {
		return BONr;
	}

	public String getName() {
		return name;
	}

	public void print(){
		//System.out.println("InputObjectName: " + name + min + max + BONr);
		System.out.println("InputObjectReferencedName:" + BOName);
	}
}
