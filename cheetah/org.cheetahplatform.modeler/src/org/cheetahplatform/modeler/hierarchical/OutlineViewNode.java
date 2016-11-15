package org.cheetahplatform.modeler.hierarchical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

public class OutlineViewNode<T> {

	private T data;
	private List<OutlineViewNode> children;
	private OutlineViewNode<?> parent;
	private String label;
	private String id;
	private Map<Long, RGB> backgrounds;

	public OutlineViewNode(String label, String id, T data, OutlineViewNode<?> parent) {
		this(label, id, data, parent, new HashMap<Long, RGB>(0));
	}

	public OutlineViewNode(String label, String id, T data, OutlineViewNode<?> parent, Map<Long, RGB> backgrounds) {
		this.data = data;
		this.backgrounds = backgrounds;
		children = new ArrayList<OutlineViewNode>();
		this.parent = parent;
		this.label = label;
		this.id = id;
	}

	public void addChild(OutlineViewNode<?> child) {
		children.add(child);
	}

	public OutlineViewNode<T> findNode(String id) {
		if (this.id.equals(id)) {
			return this;
		}

		for (OutlineViewNode<T> child : children) {
			OutlineViewNode<T> candidate = child.findNode(id);
			if (candidate != null) {
				return candidate;
			}
		}

		return null;
	}

	public Map<Long, RGB> getBackgrounds() {
		return Collections.unmodifiableMap(backgrounds);
	}

	public List<OutlineViewNode> getChildren() {
		return children;
	}

	public T getData() {
		return data;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public OutlineViewNode<?> getParent() {
		return parent;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public void removeChild(String id) {
		for (OutlineViewNode<T> child : children) {
			if (child.getId().equals(id)) {
				children.remove(child);
				break;
			}
		}
	}

	public void renameChild(String id, String label) {
		for (OutlineViewNode<T> child : children) {
			if (child.getId().equals(id)) {
				child.setLabel(label);
				break;
			}
		}

	}

	public void setBackground(long id, RGB color) {
		backgrounds.put(id, color);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setParent(OutlineViewNode<?> parent) {
		this.parent = parent;
	}

}
