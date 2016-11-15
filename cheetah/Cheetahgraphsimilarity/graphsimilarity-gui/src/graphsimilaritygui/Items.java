package graphsimilaritygui;

import mappings.Mapping;

public class Items {
	private String reference;
	private String model;
	private double similarity;
	private Mapping mapping;
	private double time;
	public Items(String reference, String model, double similarity,
			Mapping mapping, double time) {
		super();
		this.reference = reference;
		this.model = model;
		this.similarity = similarity;
		this.mapping = mapping;
		this.time = time;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	public Mapping getMapping() {
		return mapping;
	}
	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
}
