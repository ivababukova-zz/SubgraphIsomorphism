/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Node;

public class Pair {
	public Node patternNode;
	public Node hostNode;

	public Pair(Node paramNode1, Node paramNode2) {
		this.patternNode = paramNode1;
		this.hostNode = paramNode2;
	}
}