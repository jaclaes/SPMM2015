package org.cheetahplatform.modeler.graph.command;

import static org.cheetahplatform.common.CommonConstants.ATTRIBUTE_UNDO_EVENT;
import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_END;
import static org.cheetahplatform.common.logging.PromLogger.GROUP_EVENT_START;
import static org.cheetahplatform.modeler.ModelerConstants.ATTRIBUTE_COMPOUND_COMMAND_NAME;

import java.util.ArrayList;
import java.util.List;

import org.cheetahplatform.common.Assert;
import org.cheetahplatform.common.logging.Attribute;
import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.literatemodeling.command.LiterateCommands;
import org.cheetahplatform.literatemodeling.model.LiterateModel;
import org.cheetahplatform.modeler.EditorRegistry;
import org.cheetahplatform.modeler.configuration.CheetahPlatformConfigurator;
import org.cheetahplatform.modeler.configuration.IConfiguration;
import org.cheetahplatform.modeler.graph.descriptor.IEdgeDescriptor;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Edge;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.cheetahplatform.modeler.graph.model.Node;
import org.cheetahplatform.tdm.command.TDMCommand;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

public abstract class AbstractGraphCommand extends Command {
	private static String assembleEdgeName(String prefix, String name, String sourceName, String targetName) {
		if (sourceName == null && targetName == null) {
			if (prefix == null) {
				return name;
			}

			return prefix + " " + name;
		}

		if (targetName == null) {
			if (prefix == null) {
				return "Outgoing " + name + "  from " + sourceName;
			}

			return prefix + " outgoing " + name + " from " + sourceName;
		}

		if (sourceName == null) {
			if (prefix == null) {
				return "Incoming " + name + " to " + targetName;
			}
			return prefix + " incoming " + name + " to " + targetName;
		}

		if (prefix == null) {
			return name + " from " + sourceName + " to " + targetName;
		}

		return prefix + " " + name + " from " + sourceName + " to " + targetName;
	}

	/**
	 * Create the command for the given entry and graph.
	 *
	 * @param entry
	 *            the entry
	 * @param graph
	 *            the graph
	 * @return the corresponding command, <code>null</code> if the entry corresponds to a infrastructure command, e.g., grouping of events
	 */
	public static AbstractGraphCommand createCommand(AuditTrailEntry entry, Graph graph) {
		String type = entry.getEventType();

		if (VSCROLL.equals(type)) {
			int min = entry.getIntegerAttribute(SCROLL_MIN);
			int max = entry.getIntegerAttribute(SCROLL_MAX);
			int extent = entry.getIntegerAttribute(SCROLL_EXTENT);
			int value = entry.getIntegerAttribute(SCROLL_VALUE);
			return new VerticalScrollCommand(graph, null, min, max, extent, value);
		}
		if (HSCROLL.equals(type)) {
			int min = entry.getIntegerAttribute(SCROLL_MIN);
			int max = entry.getIntegerAttribute(SCROLL_MAX);
			int extent = entry.getIntegerAttribute(SCROLL_EXTENT);
			int value = entry.getIntegerAttribute(SCROLL_VALUE);
			return new HorizontalScrollCommand(graph, null, min, max, extent, value);
		}

		if (CREATE_EDGE.equals(type)) {
			String descriptorId = entry.getAttribute(AbstractGraphCommand.DESCRIPTOR);
			IEdgeDescriptor descriptor = (IEdgeDescriptor) graph.getDescriptor(descriptorId);

			return descriptor.createCreateEdgeCommand(graph, entry);
		} else if (CREATE_NODE.equals(type)) {
			Node node = (Node) createGraphElement(entry, graph);
			String name = entry.getAttribute(NAME);
			if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
				String add = " (" + entry.getAttribute(ID) + ")";
				if (name == null || name.equals("null")) {
					name = entry.getAttribute(ID);
				} else if (!name.equals(entry.getAttribute(ID)) && !name.contains(add)) {
					name += add;
				}
			} else if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)) {
				name = entry.getAttribute(ID);
			}
			int x = entry.getIntegerAttribute(X);
			int y = entry.getIntegerAttribute(Y);
			return new CreateNodeCommand(graph, node, new Point(x, y), name);
		} else if (DELETE_EDGE.equals(type)) {
			Edge edge = (Edge) graph.getGraphElement(entry.getLongAttribute(ID));
			return new DeleteEdgeCommand(edge);
		} else if (DELETE_NODE.equals(type)) {
			Node node = (Node) graph.getGraphElement(entry.getLongAttribute(ID));
			return new DeleteNodeCommand(node);
		} else if (MOVE_NODE.equals(type)) {
			Node node = (Node) graph.getGraphElement(entry.getLongAttribute(ID));
			int x = entry.getIntegerAttribute(X);
			int y = entry.getIntegerAttribute(Y);
			return new MoveNodeCommand(node, new Point(x, y));
		} else if (RECONNECT_EDGE.equals(type)) {
			Edge edge = (Edge) graph.getGraphElement(entry.getLongAttribute(ID));
			Node source = null;
			if (entry.isAttributeDefined(SOURCE_NODE_ID)) {
				source = (Node) graph.getGraphElement(entry.getLongAttribute(SOURCE_NODE_ID));
			}
			Node target = null;
			if (entry.isAttributeDefined(TARGET_NODE_ID)) {
				target = (Node) graph.getGraphElement(entry.getLongAttribute(TARGET_NODE_ID));
			}

			IEdgeDescriptor descriptor = edge.getDescriptor();
			return descriptor.createReconnectEdgeCommand(edge, source, target);
		} else if (RENAME.equals(type)) {
			GraphElement node = graph.getGraphElement(entry.getLongAttribute(ID));
			String name = entry.getAttribute(NEW_NAME);
			if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_AND_TASK_NAME)) {
				String add = " (" + entry.getAttribute(ID) + ")";
				if (name == null || name.equals("null")) {
					name = entry.getAttribute(ID);
				} else if (!name.equals(entry.getAttribute(ID)) && !name.contains(add)) {
					name += add;
				}
			} else if (CheetahPlatformConfigurator.getBoolean(IConfiguration.SHOW_ID_NOT_TASK_NAME)) {
				name = entry.getAttribute(ID);
			}
			return new RenameCommand(node, name);
		} else if (GROUP_EVENT_START.equals(type)) {
			return new CompoundGraphCommand(graph);
		} else if (GROUP_EVENT_END.equals(type)) {
			return null;
		} else if (MOVE_EDGE_LABEL.equals(type)) {
			Edge edge = (Edge) graph.getGraphElement(entry.getLongAttribute(ID));
			int x = entry.getIntegerAttribute(X);
			int y = entry.getIntegerAttribute(Y);
			return new MoveEdgeLabelCommand(edge.getLabel(), new Point(x, y));
		} else if (CREATE_EDGE_BENDPOINT.equals(type)) {
			Edge edge = (Edge) graph.getGraphElement(entry.getLongAttribute(ID));
			int x = entry.getIntegerAttribute(X);
			int y = entry.getIntegerAttribute(Y);
			int index = entry.getIntegerAttribute(INDEX);
			return new CreateEdgeBendPointCommand(edge, new Point(x, y), index);
		} else if (MOVE_EDGE_BENDPOINT.equals(type)) {
			Edge edge = (Edge) graph.getGraphElement(entry.getLongAttribute(ID));
			int x = entry.getIntegerAttribute(X);
			int y = entry.getIntegerAttribute(Y);
			int index = entry.getIntegerAttribute(INDEX);
			return new MoveEdgeBendPointCommand(edge, index, new Point(x, y));
		} else if (DELETE_EDGE_BENDPOINT.equals(type)) {
			Edge edge = (Edge) graph.getGraphElement(entry.getLongAttribute(ID));
			int index = entry.getIntegerAttribute(INDEX);
			return new DeleteEdgeBendPointCommand(edge, index);
		} else if (RESIZE_NODE.equals(type)) {
			int width = entry.getIntegerAttribute(WIDTH);
			int height = entry.getIntegerAttribute(HEIGHT);
			Node node = (Node) graph.getGraphElement(entry.getLongAttribute(ID));

			return new ResizeNodeCommand(graph, node, new Dimension(width, height));
		} else if (UNSUCCESSFUL_LAYOUT.equals(type)) {
			return new UnsuccesfulLayoutCommand(graph, null);
		} else if (type.startsWith(LiterateCommands.LM)) {
			return LiterateCommands.createCommand(entry, (LiterateModel) graph);
		} else {
			if (!entry.isAttributeDefined(DESCRIPTOR)) {
				AbstractGraphCommand command = TDMCommand.createCommand(entry, graph);
				if (command != null) {
					return command;
				}
			}

			String descriptorId = entry.getAttribute(DESCRIPTOR);
			Assert.isNotNull(descriptorId, "Unknown descriptor: " + descriptorId);
			IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(descriptorId);
			AbstractGraphCommand command = descriptor.createCommand(entry, graph);

			Assert.isNotNull(command, "Could not process the following event: " + descriptorId);
			return command;
		}
	}

	public static GraphElement createGraphElement(AuditTrailEntry entry, Graph graph) {
		String descriptorId = entry.getAttribute(AbstractGraphCommand.DESCRIPTOR);
		IGraphElementDescriptor descriptor = graph.getDescriptor(descriptorId);
		long id = entry.getLongAttribute(ID);

		return descriptor.createModel(graph, id, entry);
	}

	public static String getCommandLabel(AuditTrailEntry entry) {
		String type = entry.getEventType();
		if (CREATE_EDGE.equals(type)) {
			return getCreateEdgeLabel(entry);
		}
		if (CREATE_NODE.equals(type)) {
			return getCreateNodeLabel(entry);
		}
		if (DELETE_EDGE.equals(type)) {
			return getDeleteEdgeLabel(entry);
		}
		if (DELETE_NODE.equals(type)) {
			return getDeleteNodeLabel(entry);
		}
		if (MOVE_NODE.equals(type)) {
			return getMoveNodeLabel(entry);
		}
		if (RECONNECT_EDGE.equals(type)) {
			return getReconnectEdgeLabel(entry);
		}
		if (RENAME.equals(type)) {
			return getRenameLabel(entry);
		}
		if (GROUP_EVENT_START.equals(type)) {
			String undo = "";
			if (entry.isAttributeDefined(ATTRIBUTE_UNDO_EVENT)) {
				undo = " - undo";
			}

			if (entry.isAttributeDefined(ATTRIBUTE_COMPOUND_COMMAND_NAME)) {
				return "Grouped Event (" + entry.getAttribute(ATTRIBUTE_COMPOUND_COMMAND_NAME) + ")" + undo;
			}
			if (entry.isAttributeDefined(CHANGE_PATTERN_TYPE)) {
				return "Grouped Event (" + entry.getAttribute(CHANGE_PATTERN_TYPE) + ")" + undo;
			}

			return "Grouped Event" + undo;
		}
		if (MOVE_EDGE_LABEL.equals(type)) {
			return getMoveEdgeLabel(entry);
		}
		if (CREATE_EDGE_BENDPOINT.equals(type)) {
			return getCreateEdgeBendPointLabel(entry);
		}
		if (MOVE_EDGE_BENDPOINT.equals(type)) {
			return getMoveEdgeBendPointLabel(entry);
		}
		if (DELETE_EDGE_BENDPOINT.equals(type)) {
			return getDeleteEdgeBendPoint(entry);
		}
		if (RESIZE_NODE.equals(type)) {
			return getResizeNodeLabel(entry);
		}
		if (UNSUCCESSFUL_LAYOUT.equals(type)) {
			return "Unsuccesful layout";
		}
		if (VSCROLL.equals(type)) {
			return "Vertical Scroll";
		}
		if (HSCROLL.equals(type)) {
			return "Horizontal Scroll";
		}

		if (type.startsWith(LiterateCommands.LM)) {
			return LiterateCommands.getCommandLabel(entry);

		}

		String label = TDMCommand.getCommandLabel(entry);
		if (!label.trim().isEmpty()) {
			return label;
		}

		String descriptorId = entry.getAttribute(DESCRIPTOR);
		Assert.isNotNull(descriptorId, "Unknown descriptor: " + descriptorId);
		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(descriptorId);
		return descriptor.getCommandLabel(entry);
	}

	private static String getCreateEdgeBendPointLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Create Bendpoint for " + name;
	}

	public static String getCreateEdgeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		String sourceNode = getFullName(entry, SOURCE_NODE_DESCRIPTOR, SOURCE_NODE_NAME);
		String targetNode = getFullName(entry, TARGET_NODE_DESCRIPTOR, TARGET_NODE_NAME);

		return "Create " + name + " from " + sourceNode + " to " + targetNode;
	}

	public static String getCreateNodeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Create " + name;
	}

	private static String getDeleteEdgeBendPoint(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Delete Bendpoint of " + name;
	}

	public static String getDeleteEdgeLabel(AuditTrailEntry entry) {
		return getEdgeName(entry, "Delete");
	}

	public static String getDeleteNodeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Delete " + name;
	}

	public static String getEdgeName(AuditTrailEntry entry, String prefix) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		String sourceName = null;
		String targetName = null;

		if (entry.isAttributeDefined(TARGET_NODE_NAME)) {
			targetName = getFullName(entry, TARGET_NODE_DESCRIPTOR, TARGET_NODE_NAME);
		}
		if (entry.isAttributeDefined(SOURCE_NODE_NAME)) {
			sourceName = getFullName(entry, SOURCE_NODE_DESCRIPTOR, SOURCE_NODE_NAME);
		}

		return assembleEdgeName(prefix, name, sourceName, targetName);
	}

	public static String getEdgeName(Edge edge, String prefix) {
		String name = getFullName(edge);
		String sourceName = null;
		String targetName = null;

		if (edge.getSource() != null) {
			sourceName = getFullName(edge.getSource());
		}
		if (edge.getTarget() != null) {
			targetName = getFullName(edge.getTarget());
		}

		return assembleEdgeName(prefix, name, sourceName, targetName);
	}

	/**
	 * Determine the name of the entry, which includes the type and given name if available. If no custom name if available (e.g. for some
	 * edges names do not make sense), the type of the element is returned.
	 *
	 * @param entry
	 *            the entry
	 * @param descriptorAttribute
	 *            the attribute describing the descriptor id
	 * @param nameAttribute
	 *            the attribute describing the name
	 * @return the name
	 */
	public static String getFullName(AuditTrailEntry entry, String descriptorAttribute, String nameAttribute) {
		IGraphElementDescriptor descriptor = EditorRegistry.getDescriptor(entry.getAttribute(descriptorAttribute));
		String name = entry.getAttribute(nameAttribute);
		if (descriptor.getName().equals(name)) {
			return name;
		}

		return getFullName(descriptor, name);
	}

	protected static String getFullName(GraphElement element) {
		return getFullName(element.getDescriptor(), element.getName());
	}

	private static String getFullName(IGraphElementDescriptor descriptor, String name) {
		if (descriptor.hasCustomName() && name != null && name.trim().length() != 0) {
			name = quote(name);
			return name = descriptor.getName() + " " + name;
		}

		return descriptor.getName();
	}

	private static String getMoveEdgeBendPointLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Move Bendpoint of " + name;
	}

	public static String getMoveEdgeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Move label of " + name;
	}

	public static String getMoveNodeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Move " + name;
	}

	public static String getName(AuditTrailEntry entry) {
		return getFullName(entry, DESCRIPTOR, NAME);
	}

	/**
	 * Determine the name of the element, i.e., the custom name if given, otherwise the type of the element.
	 *
	 * @param element
	 *            the element
	 * @return the name
	 */
	protected static String getName(GraphElement element) {
		if (element.getDescriptor().hasCustomName() && element.getName() != null) {
			return element.getName();
		}

		return element.getDescriptor().getName();
	}

	public static String getReconnectEdgeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Reconnect " + name;
	}

	public static String getRenameLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		String newName = quote(entry.getAttribute(NEW_NAME));
		return "Rename " + name + " to " + newName;
	}

	private static String getResizeNodeLabel(AuditTrailEntry entry) {
		String name = getFullName(entry, DESCRIPTOR, NAME);
		return "Resize " + name;
	}

	private static String quote(String string) {
		if (string == null || string.isEmpty()) {
			return "''";
		}

		if (string.charAt(0) == '\'' && string.charAt(string.length() - 1) == '\'') {
			return string;
		}

		return "'" + string + "'";
	}

	public static final String CREATE_EDGE = "CREATE_EDGE";
	public static final String CREATE_NODE = "CREATE_NODE";
	public static final String DELETE_EDGE = "DELETE_EDGE";
	public static final String DELETE_NODE = "DELETE_NODE";
	public static final String MOVE_NODE = "MOVE_NODE";
	public static final String MOVE_EDGE_LABEL = "MOVE_EDGE_LABEL";
	public static final String RECONNECT_EDGE = "RECONNECT_EDGE";
	public static final String RENAME = "RENAME";
	public static final String CREATE_EDGE_BENDPOINT = "CREATE_EDGE_BENDPOINT";
	public static final String DELETE_EDGE_BENDPOINT = "DELETE_EDGE_BENDPOINT";
	public static final String MOVE_EDGE_BENDPOINT = "MOVE_EDGE_BENDPOINT";
	public static final String RESIZE_NODE = "RESIZE_NODE";
	public static final String UNSUCCESSFUL_LAYOUT = "UNSUCCESSFUL_LAYOUT";

	// all kinds of data properties
	public static final String SOURCE_NODE_ID = "source_node_id";

	public static final String TARGET_NODE_ID = "target_node_id";

	public static final String SOURCE_NODE_NAME = "source_node_name";

	public static final String TARGET_NODE_NAME = "target_node_name";

	public static final String SOURCE_NODE_DESCRIPTOR = "source_node_descriptor";

	public static final String TARGET_NODE_DESCRIPTOR = "target_node_descriptor";

	public static final String OLD_NODE_ID = "old_node_id";

	public static final String OLD_NODE_TYPE = "old_node_type";

	public static final String OLD_NODE_TYPE_SOURCE = "Source";

	public static final String OLD_NODE_TYPE_TARGET = "Target";

	public static final String DESCRIPTOR = "descriptor";

	public static final String X = "x";

	public static final String Y = "y";

	public static final String NAME = "name";

	public static final String NEW_NAME = "new_name";

	public static final String ID = "id";

	public static final String INDEX = "index";

	public static final String TO_MOVE_X = "to_move_x";

	public static final String TO_MOVE_Y = "to_move_y";

	public static final String WIDTH = "width";

	public static final String HEIGHT = "height";

	public static final String ADD_NODE_START_TIME = "add_node_start_time";

	public static final String RENAME_START_TIME = "rename_start_time";

	public static final String ASSIGNED_ID = "assigned_id";

	protected Graph graph;
	protected final GraphElement element;
	protected List<Attribute> attributes;
	public static final String VSCROLL = "VSCROLL";
	public static final String HSCROLL = "HSCROLL";
	public static final String SCROLL_MIN = "min";
	public static final String SCROLL_MAX = "max";
	public static final String SCROLL_EXTENT = "extent";
	public static final String SCROLL_VALUE = "value";

	public static final String CHANGE_PATTERN_TYPE = "CHANGE_PATTERN";

	public AbstractGraphCommand(Graph graph, GraphElement element) {
		this.graph = graph;
		this.element = element;
		this.attributes = new ArrayList<Attribute>();
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	protected void addOldSourceNodeAttribute(AuditTrailEntry entry, Node oldSource) {
		if (oldSource == null) {
			return;
		}

		entry.setAttribute(OLD_NODE_ID, oldSource.getId());
		entry.setAttribute(OLD_NODE_TYPE, OLD_NODE_TYPE_SOURCE);
	}

	protected void addOldTargetNodeAttribute(AuditTrailEntry entry, Node oldTarget) {
		if (oldTarget == null) {
			return;
		}

		entry.setAttribute(OLD_NODE_ID, oldTarget.getId());
		entry.setAttribute(OLD_NODE_TYPE, OLD_NODE_TYPE_TARGET);
	}

	protected void addSourceNodeAttributes(AuditTrailEntry entry, Node source) {
		if (source == null) {
			return;
		}

		entry.setAttribute(SOURCE_NODE_ID, source.getId());
		entry.setAttribute(SOURCE_NODE_NAME, getName(source));
		entry.setAttribute(SOURCE_NODE_DESCRIPTOR, source.getDescriptor().getId());
	}

	protected void addTargetNodeAttributes(AuditTrailEntry entry, Node target) {
		if (target == null) {
			return;
		}

		entry.setAttribute(TARGET_NODE_ID, target.getId());
		entry.setAttribute(TARGET_NODE_NAME, getName(target));
		entry.setAttribute(TARGET_NODE_DESCRIPTOR, target.getDescriptor().getId());
	}

	protected AuditTrailEntry createAuditrailEntry(String type) {
		String workflowModelElement = getAffectedElementName();
		AuditTrailEntry entry = new AuditTrailEntry(type, workflowModelElement);
		entry.setAttribute(ID, element.getId());
		entry.setAttribute(DESCRIPTOR, element.getDescriptor().getId());
		entry.setAttribute(NAME, getName(element));

		return entry;
	}

	/**
	 * Assemble the name of the affected graph element.
	 *
	 * @return the name
	 */
	protected abstract String getAffectedElementName();

	protected Graph getGraph() {
		return element.getGraph();
	}

	public GraphElement getGraphElement() {
		return element;
	}

	protected void log(AuditTrailEntry entry) {
		for (Attribute attribute : attributes) {
			entry.setAttribute(attribute);
		}

		graph.log(entry);
	}

	protected String nullSafeToString(Node node) {
		if (node == null) {
			return "";
		}

		return String.valueOf(node.getId());
	}

}
