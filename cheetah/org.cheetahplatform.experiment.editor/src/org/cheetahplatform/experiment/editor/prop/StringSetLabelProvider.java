package org.cheetahplatform.experiment.editor.prop;

import java.util.Iterator;
import java.util.Set;

import org.cheetahplatform.modeler.experiment.ChangePatternRegistry;
import org.eclipse.jface.viewers.LabelProvider;

public class StringSetLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		@SuppressWarnings("unchecked")
		Set<String> patterns = (Set<String>)element;
		StringBuffer buff = new StringBuffer();
		Iterator<String> it = patterns.iterator();
		while (it.hasNext()){
			String key = it.next();
			buff.append(ChangePatternRegistry.getDisplay(key));
			if (it.hasNext()) buff.append(", ");
		}
		return buff.toString();
	}
}
