package at.zugal.fitnesse.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import at.zugal.fitnesse.editor.contentAssist.WikiTableContentAssistProcessor;
import at.zugal.fitnesse.editor.formatting.WikiTableFormattingStrategy;

public class WikiSourceViewerConfiguration extends SourceViewerConfiguration {

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();

		assistant.setContentAssistProcessor(new WikiTableContentAssistProcessor(), WikiPartitionScanner.TABLE);
		assistant.setContentAssistProcessor(new WikiTableContentAssistProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(0);
		assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);

		return assistant;
	}

	@Override
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		ContentFormatter formatter = new ContentFormatter();
		IFormattingStrategy keyword = new WikiTableFormattingStrategy();
		formatter.setFormattingStrategy(keyword, WikiPartitionScanner.TABLE);
		return formatter;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer tableDamagerRepairer = new DefaultDamagerRepairer(new WikiTableTokenScanner());
		reconciler.setDamager(tableDamagerRepairer, WikiPartitionScanner.TABLE);
		reconciler.setRepairer(tableDamagerRepairer, WikiPartitionScanner.TABLE);

		DefaultDamagerRepairer textDamagerRepairer = new DefaultDamagerRepairer(new WikiTextTokenScanner());
		reconciler.setDamager(textDamagerRepairer, WikiPartitionScanner.TEXT);
		reconciler.setRepairer(textDamagerRepairer, WikiPartitionScanner.TEXT);

		return reconciler;
	}
}
