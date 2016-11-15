package org.cheetahplatform.modeler.decserflow;

import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_COEXISTENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_EXCLUSIVE_CHOICE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_INIT;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_LAST;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_PRECENDENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MULTI_SUCCESSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_MUTUAL_EXCLUSION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_NEGATION_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_PRECEDENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_RESPONDED_EXISTENCE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_RESPONSE;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SELECTION;
import static org.cheetahplatform.modeler.EditorRegistry.DECSERFLOW_SUCCESSION;

import java.util.List;

import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.decserflow.descriptor.AbstractConstraintDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.AuxiliaryNodeDescriptor;
import org.cheetahplatform.modeler.decserflow.descriptor.MultiActivityConstraintDescriptor;
import org.cheetahplatform.modeler.graph.DefaultGraphicalGraphViewerAdvisor;
import org.cheetahplatform.modeler.graph.GraphElementCreationFactory;
import org.cheetahplatform.modeler.graph.GraphNodeToolEntry;
import org.cheetahplatform.modeler.graph.GraphicalGraphViewerWithFlyoutPalette;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.INodeDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.jface.resource.ImageDescriptor;

public class DecSerFlowGraphAdvisor extends DefaultGraphicalGraphViewerAdvisor {

	public DecSerFlowGraphAdvisor(List<INodeDescriptor> nodeDescriptors, List<IEdgeDescriptor> edgeDescriptors) {
		super(nodeDescriptors, edgeDescriptors);
	}

	public DecSerFlowGraphAdvisor(List<INodeDescriptor> nodeDescriptors, List<IEdgeDescriptor> edgeDescriptors, Graph graph) {
		super(nodeDescriptors, edgeDescriptors, graph);
	}

	public void addCreationToolEntry(PaletteDrawer creationTools, String id, String description) {
		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(id);
		String name = descriptor.getName();
		ImageDescriptor icon = descriptor.getIconDescriptor();
		GraphNodeToolEntry entry = new GraphNodeToolEntry(name, icon, new GraphElementCreationFactory(descriptor, getGraph()));
		entry.setDescription(description);
		creationTools.add(entry);
	}

	@Override
	public void fillPalette(GraphicalGraphViewerWithFlyoutPalette viewer, PaletteRoot palette) {
		PaletteGroup creationTools = new PaletteGroup("Objects");
		palette.add(creationTools);

		Graph graph = getGraph();
		for (INodeDescriptor descriptor : getNodeDescriptors()) {
			if (descriptor instanceof AuxiliaryNodeDescriptor) {
				continue;
			}

			ImageDescriptor image = descriptor.getIconDescriptor();
			creationTools.add(new GraphNodeToolEntry(descriptor.getName(), image, new GraphElementCreationFactory(descriptor, graph)));
		}

		IDecSerFlowConfigurator configurator = (IDecSerFlowConfigurator) CheetahPlatformConfigurator
				.getObject(IConfiguration.DECSERFLOW_CONFIGURATOR);
		if (configurator != null) {
			configurator.configure(this, viewer, palette);
			return;
		}

		// otherwise create the default configuration
		PaletteDrawer basicConstraints = new PaletteDrawer("Basic");
		palette.add(basicConstraints);
		PaletteDrawer negationConstraints = new PaletteDrawer("Negation");
		palette.add(negationConstraints);
		PaletteDrawer otherConstraints = new PaletteDrawer("Other");
		palette.add(otherConstraints);

		for (IEdgeDescriptor descriptor : getEdgeDescriptors()) {
			String id = descriptor.getId();
			ImageDescriptor image = descriptor.getIconDescriptor();
			ConnectionCreationToolEntry constraintEntry = new ConnectionCreationToolEntry(descriptor.getName(), descriptor.getName(),
					new GraphElementCreationFactory(descriptor, graph), image, image);
			String description = ((AbstractConstraintDescriptor) descriptor).getConstraint().getDescription();
			constraintEntry.setDescription(description);

			if (id.equals(DECSERFLOW_SELECTION) || id.equals(DECSERFLOW_INIT) || id.equals(DECSERFLOW_LAST)) {
				if (id.equals(DECSERFLOW_SELECTION)) {
					description = "Activity 'A' must be executed at least M and at most N times";
				}

				addCreationToolEntry(basicConstraints, id, description);
			} else if (id.equals(DECSERFLOW_COEXISTENCE) || id.equals(DECSERFLOW_PRECEDENCE) || id.equals(DECSERFLOW_RESPONSE)
					|| id.equals(DECSERFLOW_SUCCESSION) || id.equals(DECSERFLOW_RESPONDED_EXISTENCE)) {
				basicConstraints.add(constraintEntry);
			} else if (id.equals(DECSERFLOW_MUTUAL_EXCLUSION) || id.equals(DECSERFLOW_NEGATION_RESPONSE)) {
				negationConstraints.add(constraintEntry);
			} else {
				if (id.equals(DECSERFLOW_MULTI_PRECENDENCE) || id.equals(DECSERFLOW_MULTI_SUCCESSION)
						|| id.equals(DECSERFLOW_MULTI_RESPONSE) || id.equals(DECSERFLOW_MULTI_EXCLUSIVE_CHOICE)) {
					MultiActivityToolEntry entry = new MultiActivityToolEntry(viewer, (MultiActivityConstraintDescriptor) descriptor);
					entry.setDescription(description);
					otherConstraints.add(entry);
				} else {
					otherConstraints.add(constraintEntry);
				}
			}
		}
	}
}