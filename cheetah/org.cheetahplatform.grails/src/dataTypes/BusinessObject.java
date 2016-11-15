package dataTypes;

import java.util.Iterator;
import java.util.List;

public class BusinessObject{
	List <Attribute> attributes;
	List <Reference> references;
	String name;
	String indexInXml;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.substring(0,1).toUpperCase() + name.substring(1);
	}

	public void addAttribute(Attribute a){
		attributes.add(a);
	}
	
	public void addReference(Reference r){
		references.add(r);
	}

	public List <Attribute> getAttributes(){
		return attributes;
	}

	public void setAttributes(List <Attribute> attributes){
		this.attributes = attributes;
	}

	public List <Reference> getReferences(){
		return references;
	}

	public void setReferences(List <Reference> references){
		this.references = references;
	}
	
	public void print() {
		System.out.println("Print an BusinessObject: ");
		for (Iterator <Attribute> i = attributes.iterator(); i.hasNext();) {
			Attribute a = i.next();
			a.print();
		}
		for (Iterator <Reference> i = references.iterator(); i.hasNext();) {
			Reference r = i.next();
			r.print();
		}
	}
}

