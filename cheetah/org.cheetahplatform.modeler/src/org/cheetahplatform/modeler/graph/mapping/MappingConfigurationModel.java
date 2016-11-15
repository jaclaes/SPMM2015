package org.cheetahplatform.modeler.graph.mapping;

import java.util.List;
import java.util.Set;

import com.swtdesigner.SWTResourceManager;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * @author Jakob Pinggera <Jakob.Pinggera@uibk.ac.at>
 * 
 *         20.10.2009
 */
public class MappingConfigurationModel {

	private static class ParagraphLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
		private static final int LINE_LENGTH = 50;

		@Override
		public Color getBackground(Object element, int columnIndex) {
			Paragraph paragraph = (Paragraph) element;
			return SWTResourceManager.getColor(paragraph.getColor());
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			Paragraph paragraph = (Paragraph) element;

			if (paragraph.getDescription().length() > LINE_LENGTH)
				return paragraph.getDescription().substring(0, LINE_LENGTH) + " ...";

			return paragraph.getDescription();
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			return null;
		}

	}

	public IBaseLabelProvider createParagraphLabelProvider() {
		return new ParagraphLabelProvider();
	}

	/**
	 * @param process
	 * @return
	 */
	public List<Paragraph> getParagraphs(String process) {
		return ParagraphProvider.getParagraphs(process);
	}

	public Set<String> getProcesses() {
		return ParagraphProvider.getProcesses();
	}

}
