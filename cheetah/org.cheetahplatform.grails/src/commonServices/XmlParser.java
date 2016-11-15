package commonServices;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import constants.CommonConstants;

import dataTypes.Attribute;
import dataTypes.BusinessObject;
import dataTypes.BusinessObjectReference;
import dataTypes.InputObject;
import dataTypes.Reference;

/**
 * Parses a given xml-file with the given objectname
 * search for:
 * name of the object, 
 * attribute name and type
 * references name
 * 
 * @author szabo
 *
 */
public class XmlParser {
	
	/**
	 * Creates a DOM from the given xml-file
	 * @param filename
	 * @return DOM
	 */
	public static Document parseXmlFile(String filename){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;;
		try {			
			DocumentBuilder db = dbf.newDocumentBuilder(); //Using factory get an instance of document builder
			dom = db.parse(new File( filename ) ); //parse using builder to get DOM representation of the XML file
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return dom;
	}
	
	/**
	 * Creates a DOM from the given URI
	 * @param uri
	 * @return
	 */
	public static Document parseXmlUri(String uri){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;;
		try {			
			DocumentBuilder db = dbf.newDocumentBuilder(); //Using factory get an instance of document builder
			dom = db.parse(uri);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return dom;
	}
	
	public static List <BusinessObject> parseObjects(String filename, String objectName){
		List <BusinessObject> myList = new ArrayList <BusinessObject> ();
		
		if (CommonConstants.OUTPUT) System.out.println("Now Parsing Xml File : " + filename);
		
		Document dom = parseXmlFile(filename);
		//Document dom = parseXmlUri(filename);
		
		//Document dom = parseUri(filename);
		Element docEle = dom.getDocumentElement(); //get the root elememt
		NodeList nl = docEle.getElementsByTagName(objectName);
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				BusinessObject BO = getBusinessObject(el);
				myList.add(BO);
			}
		}		
		return myList;
	}
	
	private static BusinessObject getBusinessObject(Element el){		
		BusinessObject BO = new BusinessObject();		
		BO.setAttributes(getAttributeName(el));
		BO.setReferences(getReferenceName(el));
		BO.setName(getName(el));
		return BO;
	}
	
	/**
	 * Returns a list of BOReference - Names
	 * @param filename
	 * @param objectName
	 * @return
	 */
	public static List <BusinessObjectReference> parseBOReference(String filename, String objectName){
		List <BusinessObjectReference> myList = new ArrayList <BusinessObjectReference> ();
		Document dom = parseXmlFile(filename);
		Element docEle = dom.getDocumentElement(); //get the root elememt
		NodeList nl = docEle.getElementsByTagName(objectName);
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				BusinessObjectReference BORef = getBusinessObjectReference(el);
				myList.add(BORef);
			}
		}		
		return myList;
	}
	
	/**
	 * Returns one BOReference
	 * @param el
	 * @return
	 */
	private static BusinessObjectReference getBusinessObjectReference(Element el){		
		BusinessObjectReference BORef = new BusinessObjectReference();		
		BORef.setReferences(getReferenceName(el));
		return BORef;
	}	
	
	private static String getName(Element ele){
		return ele.getAttributeNode("name").getValue().replaceAll("-", "");
	}
	
	private static List <Attribute> getAttributeName(Element ele) {
		List <Attribute> attribute = new ArrayList <Attribute> ();
		NodeList nl = ele.getElementsByTagName("attributes");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				attribute.add(new Attribute(el.getAttribute("name").replaceAll("-", ""), el.getAttribute("type"), el.getAttribute("max")));
			}
		}
		return attribute;
	}
	
	/**
	 * Returns List of ReferenceNames which are the Attributes
	 * @param ele
	 * @return
	 */
	private static List <Reference> getReferenceName(Element ele) {
		List <Reference> attribute = new ArrayList <Reference> ();
		NodeList nl = ele.getElementsByTagName("references");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				attribute.add(new Reference(el.getAttribute("name").replaceAll("-", ""), el.getAttribute("min"), el.getAttribute("max"), el.getAttribute("businessObject")));
			}
		}
		return attribute;
	}
	
	/*
	<referenceRepository iD="1227296676242">
    <references name="PaperReference" iD="1227296676267" min="1" max="1" businessObject="//@businessObjectRepository/@businessObjects.0"
    */
	
	/**
	 * <activityRepository name="Activity Repository" iD="1236262404446">          
     *      <activities name="newActivity" iD="1236262404463" inputObjects="//@objectRepository/@processTypeRepository/@processTypes.0/@processSchemas.0/@referenceRepository/@references.0"/>
     *      <activities name="szaboActivity" iD="1239220076342" inputObjects="//@objectRepository/@processTypeRepository/@processTypes.0/@processSchemas.0/@referenceRepository/@references.1"/>
     * </activityRepository> 
	 * 
	 * Returns an inputObjectList to a given activity
	 * inputObjectList holds the names of BusinessObjectReferences, i.E PaperReference
	 * 
	 * i.E 
	 * <referenceRepository iD="1227296676242">
     *     <references name="PaperReference" 
	 * 
	 * 
	 * @param filename
	 * @param activityName
	 * @return InputObjectList
	 */
	public static List <InputObject> search4ActivityReturnInputObjectList(String filename, String activityName){
		List <BusinessObjectReference> BORefList = parseBOReference(filename, "referenceRepository");
		
		List <InputObject> myList = new ArrayList <InputObject> ();
		myList = null;
		
		Document dom = parseXmlFile(filename);
		Element docEle = dom.getDocumentElement(); //get the root elememt
		NodeList nl = docEle.getElementsByTagName("activityRepository");
		
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				NodeList nl2 = el.getElementsByTagName("activities");
				
				if(nl2 != null && nl2.getLength() > 0) {
					for(int j = 0 ; j < nl2.getLength();j++) {
						Element el2 = (Element)nl2.item(j);
						if (activityName.equals(el2.getAttribute("name")))
							if (el2.getAttribute("inputObjects") != "") {
								myList = parseInputObjectPath(el2.getAttribute("inputObjects"), BORefList);
								return myList;
							}
					}
				}
			}
		}		
		return myList;
	}
	
	/**
	 * Parses the given path and extracts the BusinessObjectName
	 * @param path Examle: //@objectRepository/@processTypeRepository/@processTypes.0/@processSchemas.0/@referenceRepository/@references.0
	 * @param BORefList 
	 * @return
	 */
	public static List <InputObject> parseInputObjectPath(String path, List <BusinessObjectReference> BORefList){
		List <InputObject> myList = new ArrayList <InputObject> ();
		
		//create array with delemiter "//@"
		String [] temp = path.split("//@");
		for (int i = 0 ; i < temp.length ; i++) {
			if (temp[i].length() > 0){
				//take the last character of this string its the number of the reference
				String [] temp2 = temp[i].split("@referenceRepository/@references.");
				int zahl = Integer.parseInt(temp2[temp2.length - 1].trim());
				
				myList.add(new InputObject(	BORefList.get(0).getReferences().get(zahl).getName(), 
											BORefList.get(0).getReferences().get(zahl).getMin(), 
											BORefList.get(0).getReferences().get(zahl).getMax(),
											BORefList.get(0).getReferences().get(zahl).getBusinnesObjectLink()
										));
			}
		}
		return myList;
	}
	
	/*
	public static void main( String[] args ) {
		List <InputObject> IOL = null;
		IOL = search4ActivityReturnInputObjectList(GrailsConstants.xmlFileName, "sendThesisToSupervisor");
		
		if (IOL != null) {
			for (Iterator <InputObject> i = IOL.iterator(); i.hasNext();) {
				InputObject bo = i.next();
				bo.print();
			}
		}
		/*
		makeProject(args[0]);
		makeProject(GrailsConstants.xmlFileName);
		List <BusinessObject> BOList = parseObjects(GrailsConstants.xmlFileName, "businessObjects");
		List <BusinessObjectReference> BOList = parseBOReference(GrailsConstants.xmlFileName, "referenceRepository");
		
		
		for (Iterator <BusinessObjectReference> i = BOList.iterator(); i.hasNext();) {
			BusinessObjectReference bo = i.next();
			bo.print();
		}
	}*/
}
