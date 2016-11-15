package dataTypes;

public class Reference {
	String name;
	String min;
	String max;
	String BusinnesObjectLink;
	
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.substring(0,1).toUpperCase() + name.substring(1);
	}
	public Reference(String name, String min, String max, String link){
		this.name = name.substring(0,1).toUpperCase() + name.substring(1);
		this.min = min;
		this.max = max;
		this.BusinnesObjectLink = link;
	}
	public void print(){
		System.out.print("Reference: " + name + " Min: " + min + " Max: " + max + " "+ BusinnesObjectLink + "\n");
	}
	public String getBusinnesObjectLink() {
		return BusinnesObjectLink;
	}
	public void setBusinnesObjectLink(String businnesObjectLink) {
		BusinnesObjectLink = businnesObjectLink;
	}
}
