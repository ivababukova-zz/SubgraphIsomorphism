/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

public class DefaultEdge implements Edge {
	Object label;
	Node u;
	Node v;

	public DefaultEdge(Node paramNode1, Node paramNode2, Object paramObject) {
		this.u = paramNode1;
		this.v = paramNode2;
		this.label = paramObject;
	}

	@Override
	public Object getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(Object paramObject) {
		this.label = paramObject;
	}

	@Override
	public Node getFirstNode() {
		return this.u;
	}

	@Override
	public Node getSecondNode() {
		return this.v;
	}

	@Override
	public Node getOppositeNode(Node paramNode) {
		assert ((paramNode == this.u) || (paramNode == this.v));
		if (paramNode == this.u)
			return this.v;
		return this.u;
	}
}