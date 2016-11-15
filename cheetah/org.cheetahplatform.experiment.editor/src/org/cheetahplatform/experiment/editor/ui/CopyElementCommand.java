package org.cheetahplatform.experiment.editor.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cheetahplatform.modeler.graph.model.Node;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

public class CopyElementCommand extends Command {
	private List<Object> list = new ArrayList<Object>();
	
	public boolean addElement(Object element) {
		if (!list.contains(element)) {
			return list.add(element);
		}
		return false;
	}
	
	@Override
	public boolean canExecute() {
		if (list == null || list.isEmpty())
			return false;
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
			if (!isCopyable(it.next()))
				return false;
		}
		return true;
	}
	
	public boolean isCopyable(Object element) {
		return (element instanceof Node);
	}
	
	@Override
	public void execute() {
		if (canExecute())
			Clipboard.getDefault().setContents(list);
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}
}
