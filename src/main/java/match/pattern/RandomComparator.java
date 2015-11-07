/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Graph;
import graph.Node;

import java.util.Comparator;
import java.util.Random;

public class RandomComparator implements Comparator<Node> {
	private static final long seed = 88686765L;
	private int[] randomValues;

	public RandomComparator(Graph paramGraph) {
		this.randomValues = new int[paramGraph.getNodeCount()];
		Random localRandom = new Random(seed);
		for (int i = 0; i < this.randomValues.length; ++i)
			this.randomValues[i] = localRandom.nextInt();
	}

	@Override
	public int compare(Node paramNode1, Node paramNode2) {
		int i = this.randomValues[paramNode1.getIndex()];
		int j = this.randomValues[paramNode2.getIndex()];
		if (i < j)
			return -1;
		if (i > j)
			return 1;
		return 0;
	}
}