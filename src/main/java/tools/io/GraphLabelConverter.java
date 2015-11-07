/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package tools.io;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.HashMap;
import java.util.Iterator;

public class GraphLabelConverter {
	private HashMap<Object, Character> nLabels = new HashMap<Object, Character>();
	private HashMap<Object, Character> eLabels = new HashMap<Object, Character>();
	private char nLabel = '%';
	private char eLabel = 65535;

	public void convert(Graph paramGraph) {
		Iterator<?> localIterator = paramGraph.nodes().iterator();
		Object localObject;
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			((Node) localObject).setLabel(Character
					.valueOf(getNodeLabel(((Node) localObject).getLabel())));
		}
		localIterator = paramGraph.edges().iterator();
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			((Edge) localObject).setLabel(Character
					.valueOf(getEdgeLabel(((Edge) localObject).getLabel())));
		}
		if (this.eLabel > this.nLabel)
			return;
		throw new RuntimeException("Maximum number of labels exceeded!");
	}

	private char getNodeLabel(Object paramObject) {
		Character localCharacter = this.nLabels.get(paramObject);
		if (localCharacter == null) {
			GraphLabelConverter tmp17_16 = this;
			char tmp21_18 = tmp17_16.nLabel;
			tmp17_16.nLabel = (char) (tmp21_18 + '\1');
			localCharacter = Character.valueOf(tmp21_18);
			this.nLabels.put(paramObject, localCharacter);
		}
		return localCharacter.charValue();
	}

	private char getEdgeLabel(Object paramObject) {
		Character localCharacter = this.eLabels.get(paramObject);
		if (localCharacter == null) {
			GraphLabelConverter tmp17_16 = this;
			char tmp21_18 = tmp17_16.eLabel;
			tmp17_16.eLabel = (char) (tmp21_18 - '\1');
			localCharacter = Character.valueOf(tmp21_18);
			this.eLabels.put(paramObject, localCharacter);
		}
		return localCharacter.charValue();
	}
}