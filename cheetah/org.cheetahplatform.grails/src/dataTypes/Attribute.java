package dataTypes;

public class Attribute{
	String name;
	String type;
	String range;
	
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}	
	public Attribute(String name, String type, String range){
		this.name = name.substring(0,1).toUpperCase() + name.substring(1);
		if (type.equals(""))
			this.type = "String";
		else
			this.type = type;
		this.range = range;
	}
	public void print(){
		System.out.println("Attribute: " + name + " Type: " + type);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.substring(0,1).toUpperCase() + name.substring(1);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
