/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import java.util.List;

public abstract interface Graph {
	public abstract List<Node> nodes();

	public abstract List<Edge> edges();

	public abstract Node getNode(int paramInt);

	public abstract int getNodeCount();

	public abstract int getEdgeCount();

	public abstract boolean hasEdge(Node paramNode1, Node paramNode2);

	public abstract Edge getEdge(Node paramNode1, Node paramNode2);
}