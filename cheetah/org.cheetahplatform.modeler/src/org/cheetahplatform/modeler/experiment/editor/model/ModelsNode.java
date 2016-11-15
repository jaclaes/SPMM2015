package org.cheetahplatform.modeler.experiment.editor.model;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.core.service.Services;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.NodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.Node;

public class ModelsNode extends Node {

	public static class ModelContainer {

		private Graph graph;
		private byte[] image;
		private String type;
		private String name;
		private Long id;

		public ModelContainer(byte[] image, String name) {
			this(image, name, Services.getIdGenerator().generateId());
		}

		public ModelContainer(byte[] image, String name, Long id) {
			this.image = image;
			this.type = "IMAGE";
			this.name = name;
			this.id = id;
		}

		public ModelContainer(String type, Graph graph, String name) {
			this(type, graph, name, Services.getIdGenerator().generateId());
		}

		public ModelContainer(String type, Graph graph, String name, Long id) {
			this.graph = graph;
			this.type = type;
			this.name = name;
			this.id = id;
		}

		public Graph getGraph() {
			return graph;
		}

		public Long getId() {
			return id;
		}

		public byte[] getImage() {
			return image;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public void setGraph(Graph graph) {
			this.graph = graph;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	private List<ModelContainer> models;

	public ModelsNode(Graph parent, INodeDescriptor descriptor) {
		super(parent, descriptor);
		this.models = new ArrayList<ModelContainer>();
	}

	public ModelsNode(Graph graph, NodeDescriptor descriptor, long id) {
		super(graph, descriptor, id);
		this.models = new ArrayList<ModelContainer>();
	}

	public void addModel(ModelContainer model) {
		models.add(model);
	}

	public List<ModelContainer> getModels() {
		return models;
	}

	public void setModels(List<ModelContainer> models) {
		this.models = models;
	}

}
