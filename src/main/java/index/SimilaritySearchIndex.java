/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index;

import graph.Graph;

import java.util.List;

public abstract interface SimilaritySearchIndex {
	public abstract List<Integer> rank(Graph paramGraph);
}