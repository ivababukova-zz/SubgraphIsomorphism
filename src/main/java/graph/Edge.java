/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

public abstract interface Edge {
	public abstract Object getLabel();

	public abstract void setLabel(Object paramObject);

	public abstract Node getFirstNode();

	public abstract Node getSecondNode();

	public abstract Node getOppositeNode(Node paramNode);
}