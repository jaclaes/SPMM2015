package at.zugal.fitnesse.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

import at.zugal.fitnesse.Activator;
import at.zugal.fitnesse.editor.action.ContentAssistAction;
import at.zugal.fitnesse.editor.action.FormatSourceAction;
import at.zugal.fitnesse.editor.action.JumpToNextCellAction;
import at.zugal.fitnesse.editor.action.JumpToPreviousCellAction;

public class WikiEditor extends AbstractDecoratedTextEditor {
	private Map<Integer, Action> keyBinding;

	public WikiEditor() {
		this.keyBinding = new HashMap<Integer, Action>();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		initializeKeyBindings();
		IEditorInput input = getEditorInput();
		Object inputFile = input.getAdapter(IFile.class);
		if (inputFile != null) {
			IContainer folder = ((IFile) inputFile).getParent();
			setPartName(folder.getName());
		}
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		performSaveActions();

		super.doSave(progressMonitor);
	}

	protected void handleKeyPressed(KeyEvent event) {
		Action action = keyBinding.get(event.keyCode | event.stateMask);
		if (action != null) {
			action.run();
		}
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();

		setSourceViewerConfiguration(new WikiSourceViewerConfiguration());
	}

	private void initializeKeyBindings() {
		getSourceViewer().getTextWidget().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPressed(e);
			}
		});

		// register the action via the eclipse key binding system as it handles this event
		Action jumpToNextCellAction = new JumpToNextCellAction(getSourceViewer());
		jumpToNextCellAction.setActionDefinitionId(ITextEditorActionDefinitionIds.WORD_NEXT);
		setAction(ITextEditorActionDefinitionIds.WORD_NEXT, jumpToNextCellAction);
		getSourceViewer().getTextWidget().setKeyBinding(SWT.CTRL | SWT.ARROW_RIGHT, SWT.NULL);

		Action jumpToPreviousCellAction = new JumpToPreviousCellAction(getSourceViewer());
		jumpToPreviousCellAction.setActionDefinitionId(ITextEditorActionDefinitionIds.WORD_PREVIOUS);
		setAction(ITextEditorActionDefinitionIds.WORD_PREVIOUS, jumpToPreviousCellAction);
		getSourceViewer().getTextWidget().setKeyBinding(SWT.CTRL | SWT.ARROW_LEFT, SWT.NULL);

		// register all other actions
		keyBinding.put(SWT.CTRL | SWT.SHIFT | 'f', new FormatSourceAction((SourceViewer) getSourceViewer()));
		keyBinding.put(SWT.CTRL | ' ', new ContentAssistAction((SourceViewer) getSourceViewer()));
	}

	private void performSaveActions() {
		try {
			new FormatSourceAction((SourceViewer) getSourceViewer()).run();

			// append empty line, cf. bug#60
			IDocument document = getSourceViewer().getDocument();
			if (document.getLength() != 0) {
				boolean endsWithEmptyLine = true;
				for (int i = document.getLength() - 1; i >= 0; i--) {
					char currentChar = document.getChar(i);
					if (currentChar == '\n') {
						break;
					}
					if (currentChar != ' ' && currentChar != '\t') {
						endsWithEmptyLine = false;
					}
				}
				if (!endsWithEmptyLine) {
					document.replace(document.getLength(), 0, "\n");
				}
			}
		} catch (BadLocationException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not append an empty line.");
			Activator.getDefault().getLog().log(status);
		}
	}
}
