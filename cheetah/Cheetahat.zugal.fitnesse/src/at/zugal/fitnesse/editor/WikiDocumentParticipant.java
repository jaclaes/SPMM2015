package at.zugal.fitnesse.editor;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

public class WikiDocumentParticipant implements IDocumentSetupParticipant {

	@Override
	public void setup(IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 casted = (IDocumentExtension3) document;
			IDocumentPartitioner oldPartitioner = ((IDocumentExtension3) document)
					.getDocumentPartitioner(IDocumentExtension3.DEFAULT_PARTITIONING);
			if (oldPartitioner != null) {
				oldPartitioner.disconnect();
			}

			FastPartitioner partitioner = new FastPartitioner(new WikiPartitionScanner(), WikiPartitionScanner.PARTITION_TYPES);
			casted.setDocumentPartitioner(IDocumentExtension3.DEFAULT_PARTITIONING, partitioner);
			partitioner.connect(document, false);
		}
	}

}
