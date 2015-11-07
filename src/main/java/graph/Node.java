/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import java.util.List;

public abstract interface Node {
	public abstract int getIndex();

	public abstract Object getLabel();

	public abstract void setLabel(Object paramObject);

	public abstract List<Edge> getEdges();

	public abstract int getDegree();
}