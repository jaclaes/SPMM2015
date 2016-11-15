package dataTypes;

import java.util.Iterator;
import java.util.List;

public class BusinessObjectReference {
	List <Reference> references;

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}
	
	public void print() {
		System.out.println("Print an BusinessObjectReference: ");
		
		for (Iterator <Reference> i = references.iterator(); i.hasNext();) {
			Reference r = i.next();
			r.print();
		}
	}
	
	public String getRefAtIndex(int index) {
		int counter = 0;
		for (Iterator <Reference> i = references.iterator(); i.hasNext();) {
			if (counter == index){
				Reference r = i.next();
				return r.getName();
			}
			counter++;
		}
		return null;
	}
}
