package org.cheetahplatform.literatemodeling.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.cheetahplatform.common.logging.AuditTrailEntry;
import org.cheetahplatform.common.logging.ILogListener;
import org.cheetahplatform.literatemodeling.LiterateInitialValues;
import org.cheetahplatform.literatemodeling.command.LiterateCommands;
import org.cheetahplatform.modeler.graph.command.AbstractGraphCommand;
import org.cheetahplatform.modeler.graph.descriptor.IGraphElementDescriptor;
import org.cheetahplatform.modeler.graph.model.Graph;
import org.cheetahplatform.modeler.graph.model.GraphElement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         22.04.2010
 */
public class LiterateModel extends Graph {
	public static final String PROP_NAME = "name";
	public static final String PROP_DESCRIPTION = "description";
	public static final String PROP_TEXT = "text";

	private final List<ILiterateModelingAssociation> associations;
	private List<IAssociationChangedListener> assocListeners;
	private String name;
	private String description;
	private IDocument document;

	public LiterateModel(List<IGraphElementDescriptor> descriptors) {
		this(descriptors, null);
	}

	public LiterateModel(List<IGraphElementDescriptor> descriptors, LiterateInitialValues lmInitials) {
		super(descriptors);

		associations = new ArrayList<ILiterateModelingAssociation>();
		assocListeners = new ArrayList<IAssociationChangedListener>();

		if (lmInitials != null) {
			this.document = new Document(lmInitials.getText());
			this.name = lmInitials.getName();
			this.description = lmInitials.getDescription();
		} else {
			this.document = new Document("");
			this.name = "";
			this.description = "";
		}

		addLogListener(new ILogListener() {
			@Override
			public void log(AuditTrailEntry entry) {
				if (AbstractGraphCommand.DELETE_NODE.equals(entry.getEventType())) {
					updateAssociations();
				}
				if (AbstractGraphCommand.DELETE_EDGE.equals(entry.getEventType())) {
					updateAssociations();
				}
			}
		});
	}

	public void addAssociation(ILiterateModelingAssociation association) {
		Assert.isNotNull(association);
		associations.add(association);
		fireAssociationChanged(association);
	}

	public void addAssociationChangedListener(IAssociationChangedListener listener) {
		assocListeners.add(listener);
	}

	/**
	 * @param offset
	 * @param deletedLength
	 */
	private void deleted(int offset, int deletedLength) {
		ListIterator<ILiterateModelingAssociation> listIterator = associations.listIterator();
		while (listIterator.hasNext()) {
			ILiterateModelingAssociation association = listIterator.next();
			if (association.isBefore(offset)) {
				if (!association.isBefore(offset + deletedLength)) {
					association.decreaseLength(offset + deletedLength - association.getOffset());
				}
				association.decreaseOffset(deletedLength);
			} else if (!association.isAfter(offset)) {
				if (!association.isAfter(offset + deletedLength)) {
					association.decreaseLength(deletedLength);
				} else {
					int endpoint = association.getEnd();
					int distanceAssociationStartDeletStart = offset - association.getOffset();
					association.decreaseLength(endpoint - (association.getOffset() + distanceAssociationStartDeletStart));
				}
			}
			if (association.getLength() <= 0) {
				listIterator.remove();
			}
		}
	}

	protected void fireAssociationChanged(ILiterateModelingAssociation assoc) {
		for (IAssociationChangedListener listener : assocListeners) {
			listener.associationChanged(assoc);
		}
	}

	public void fireDescriptionChanged() {
		firePropertyChanged(new PropertyChangeEvent(this, LiterateCommands.TEXT_DESCRIPTION, null, null));
	}

	public void fireNameChanged() {
		firePropertyChanged(new PropertyChangeEvent(this, LiterateCommands.TEXT_NAME, null, null));
	}

	/**
	 * @param element
	 * @return
	 */
	public ILiterateModelingAssociation getAssociation(GraphElement element) {
		for (ILiterateModelingAssociation association : associations) {
			if (association.matches(element)) {
				return association;
			}
		}
		throw new IllegalArgumentException("Could not find association for " + element.getName()
				+ ". Use isAssociationDefined first to check existence");
	}

	/**
	 * @return
	 */
	public List<ILiterateModelingAssociation> getAssociations() {
		return Collections.unmodifiableList(associations);
	}

	/**
	 * Returns the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public IDocument getDocument() {
		return document;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param element
	 * @param foreground
	 * @return
	 */
	public StyleRange getStyleRange(GraphElement element, Color foreground, Color background) {
		return getStyleRange(element, foreground, background, SWT.NORMAL);
	}

	/**
	 * @param element
	 * @param foreground
	 * @return
	 */
	public StyleRange getStyleRange(GraphElement element, Color foreground, Color background, int style) {
		for (ILiterateModelingAssociation association : associations) {
			if (association.matches(element)) {
				return association.getStyleRange(foreground, background, style);
			}
		}
		throw new IllegalArgumentException("Could not find association for " + element.getName()
				+ ". Use isAssociationDefined first to check existence");
	}

	/**
	 * @param association
	 */
	public String getTextForAssociation(ILiterateModelingAssociation association) {
		return getDocument().get().substring(association.getOffset(), association.getEnd());
	}

	/**
	 * @param offset
	 * @param insertedLength
	 */
	private void inserted(int offset, int insertedLength) {
		for (ILiterateModelingAssociation association : associations) {
			if (association.isBefore(offset)) {
				association.increaseOffset(insertedLength);
			} else if (!association.isAfter(offset)) {
				association.increaseLength(insertedLength);
			}
		}
	}

	/**
	 * @param element
	 * @return
	 */
	public boolean isAssociationDefined(GraphElement element) {
		for (ILiterateModelingAssociation association : associations) {
			if (association.matches(element)) {
				return true;
			}
		}
		return false;
	}

	public void removeAllAssociations(Collection<ILiterateModelingAssociation> associations) {
		this.associations.removeAll(associations);
	}

	public void removeAssociation(ILiterateModelingAssociation association) {
		Assert.isNotNull(association);
		associations.remove(association);
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		Assert.isNotNull(description);
		this.description = description;

	}

	public void setDocument(IDocument document) {
		this.document = document;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		Assert.isNotNull(name);
		this.name = name;

	}

	/**
	 * Updates all associations.
	 * 
	 * @param offset
	 *            the offset
	 * @param insertedLength
	 * @param deletedLength
	 */
	public void udpateAssociations(int offset, int insertedLength, int deletedLength) {
		Assert.isLegal(offset >= 0);
		Assert.isLegal(insertedLength >= 0);
		Assert.isLegal(deletedLength >= 0);

		if (deletedLength > 0) {
			deleted(offset, deletedLength);
		}

		if (insertedLength > 0) {
			inserted(offset, insertedLength);
		}
		fireAssociationChanged(null);
	}

	protected void updateAssociations() {
		ListIterator<ILiterateModelingAssociation> iterator = associations.listIterator();
		while (iterator.hasNext()) {
			ILiterateModelingAssociation association = iterator.next();
			List<GraphElement> graphElements = association.getGraphElements();
			for (GraphElement graphElement : graphElements) {
				if (!contains(graphElement)) {
					if (association.isSingleLiterateModelingAssociation()) {
						iterator.remove();
						break;
					}

					association.removeGraphElement(graphElement);
					if (association.getGraphElements().isEmpty()) {
						iterator.remove();
						break;
					}
				}
			}
		}
	}
}
