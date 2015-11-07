/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DefaultGraph implements Graph, Cloneable {
	ArrayList<Node> nodes;
	protected int edgeCount;

	public DefaultGraph() {
		this.nodes = new ArrayList<Node>();
		this.edgeCount = 0;
	}

	public DefaultGraph(int paramInt) {
		this.nodes = new ArrayList<Node>(paramInt);
	}

	public DefaultGraph(Graph paramGraph) {
		this(paramGraph.getNodeCount());
		addGraph(paramGraph);
	}

	/*
	public DefaultGraph clone() {
		return new DefaultGraph(this);
	}
	*/

	@Override
	public List<Node> nodes() {
		return this.nodes;
	}

	@Override
	public List<Edge> edges() {
		LinkedList<Edge> localLinkedList = new LinkedList<Edge>();
		Iterator<Node> localIterator1 = this.nodes.iterator();
		while (localIterator1.hasNext()) {
			Node localNode = localIterator1.next();
			Iterator<?> localIterator2 = localNode.getEdges().iterator();
			while (localIterator2.hasNext()) {
				Edge localEdge = (Edge) localIterator2.next();
				if (localNode.getIndex() < localEdge.getOppositeNode(localNode)
						.getIndex())
					localLinkedList.add(localEdge);
			}
		}
		return localLinkedList;
	}

	@Override
	public DefaultNode getNode(int paramInt) {
		return ((DefaultNode) this.nodes.get(paramInt));
	}

	@Override
	public int getNodeCount() {
		return this.nodes.size();
	}

	@Override
	public int getEdgeCount() {
		return this.edgeCount;
	}

	@Override
	public boolean hasEdge(Node paramNode1, Node paramNode2) {
		return (getEdge(paramNode1, paramNode2) != null);
	}

	@Override
	public DefaultEdge getEdge(Node paramNode1, Node paramNode2) {
		List<?> localList = paramNode1.getEdges();
		Iterator<?> localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			Edge localEdge = (Edge) localIterator.next();
			if (localEdge.getOppositeNode(paramNode1) == paramNode2)
				return ((DefaultEdge) localEdge);
		}
		return null;
	}

	public DefaultNode addNode(Object paramObject) {
		DefaultNode localDefaultNode = new DefaultNode(paramObject,
				this.nodes.size());
		this.nodes.add(localDefaultNode);
		return localDefaultNode;
	}

	public void addEdge(DefaultNode paramDefaultNode1,
			DefaultNode paramDefaultNode2, Object paramObject) {
		DefaultEdge localDefaultEdge = new DefaultEdge(paramDefaultNode1,
				paramDefaultNode2, paramObject);
		paramDefaultNode1.addEdge(localDefaultEdge);
		paramDefaultNode2.addEdge(localDefaultEdge);
		this.edgeCount += 1;
	}

	public void addGraph(Graph paramGraph) {
		int i = getNodeCount();
		for (int j = 0; j < paramGraph.getNodeCount(); ++j)
			addNode(paramGraph.getNode(j).getLabel());
		Iterator<?> localIterator = paramGraph.edges().iterator();
		while (localIterator.hasNext()) {
			Edge localEdge = (Edge) localIterator.next();
			DefaultNode localDefaultNode1 = getNode(localEdge.getFirstNode()
					.getIndex() + i);
			DefaultNode localDefaultNode2 = getNode(localEdge.getSecondNode()
					.getIndex() + i);
			addEdge(localDefaultNode1, localDefaultNode2, localEdge.getLabel());
		}
	}

	@Override
	public String toString() {
		StringBuilder localStringBuilder = new StringBuilder();
		localStringBuilder.append("V={");
		Iterator<?> localIterator = nodes().iterator();
		Object localObject;
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			localStringBuilder.append("(");
			localStringBuilder.append(((Node) localObject).getIndex());
			localStringBuilder.append(",");
			localStringBuilder.append(((Node) localObject).getLabel());
			localStringBuilder.append(") ");
		}
		if (!(nodes().isEmpty()))
			localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
		localStringBuilder.append("}\n");
		localStringBuilder.append("E={");
		localIterator = edges().iterator();
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			localStringBuilder.append("(");
			localStringBuilder.append(((Edge) localObject).getFirstNode()
					.getIndex());
			localStringBuilder.append(",");
			localStringBuilder.append(((Edge) localObject).getSecondNode()
					.getIndex());
			localStringBuilder.append(",");
			localStringBuilder.append(((Edge) localObject).getLabel());
			localStringBuilder.append(") ");
		}
		if (!(edges().isEmpty()))
			localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
		localStringBuilder.append("}");
		return (localStringBuilder.toString());
	}
}