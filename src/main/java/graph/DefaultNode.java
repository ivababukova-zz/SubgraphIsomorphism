/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import java.util.LinkedList;
import java.util.List;

public class DefaultNode implements Node {
	private int index;
	protected Object label;
	private LinkedList<Edge> edges;

	protected DefaultNode(Object paramObject, int paramInt) {
		this.label = paramObject;
		this.index = paramInt;
		this.edges = new LinkedList<Edge>();
	}

	@Override
	public List<Edge> getEdges() {
		return this.edges;
	}

	@Override
	public int getDegree() {
		return this.edges.size();
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public Object getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(Object paramObject) {
		this.label = paramObject;
	}

	public void addEdge(Edge paramEdge) {
		this.edges.addLast(paramEdge);
	}

	public boolean removeEdge(Edge paramEdge) {
		return this.edges.remove(paramEdge);
	}
}