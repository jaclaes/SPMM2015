package org.cheetahplatform.modeler.graph.export.declare;

import org.eclipse.draw2d.geometry.Rectangle;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DeclareCellConverter extends AbstractMarshalOnlyConverter {

	public DeclareCellConverter() {
		super(DeclareCell.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DeclareCell cell = (DeclareCell) source;

		Rectangle bounds = cell.getBounds();
		writer.addAttribute("height", String.valueOf(bounds.height));
		writer.addAttribute("id", String.valueOf(cell.getId()));
		writer.addAttribute("width", String.valueOf(bounds.width));
		writer.addAttribute("x", String.valueOf(bounds.x));
		writer.addAttribute("y", String.valueOf(bounds.y));
	}

}
