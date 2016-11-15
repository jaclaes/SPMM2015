package org.cheetahplatform.modeler.graph.mapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.RGB;

public class EdgeCondition {
	private Set<String> processes;

	private String name;

	private long id;

	private RGB color;

	public EdgeCondition(long id, String name, RGB color, String... processes) {
		this.id = id;
		this.processes = new HashSet<String>(Arrays.asList(processes));
		this.name = name;
		this.color = color;
	}

	public boolean belongsTo(String process) {
		return processes.contains(process);
	}

	public RGB getColor() {
		return color;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
