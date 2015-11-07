/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.Iterator;
import java.util.LinkedList;

import match.pattern.EdgeMatcher;
import match.pattern.NodeMatcher;
import match.pattern.SearchPattern;

public class PartialMapping {
	public static long stateCount = 0L;
	public static long pmCount = 0L;
	public static final boolean DEBUG = false;
	protected Graph pattern;
	protected Graph host;
	protected NodeMatcher nodeMatcher;
	protected EdgeMatcher edgeMatcher;
	protected int[] mapPH;
	protected int[] mapHP;
	protected int size;

	public PartialMapping(SearchPattern paramSearchPattern, Graph paramGraph) {
		pmCount += 1L;
		this.pattern = paramSearchPattern.getGraph();
		this.host = paramGraph;
		this.nodeMatcher = paramSearchPattern.getNodeMatcher();
		this.edgeMatcher = paramSearchPattern.getEdgeMatcher();
		this.mapPH = new int[this.pattern.getNodeCount()];
		this.mapHP = new int[this.host.getNodeCount()];
		for (int i = 0; i < this.mapPH.length; ++i)
			this.mapPH[i] = -1;
		for (int i = 0; i < this.mapHP.length; ++i)
			this.mapHP[i] = -1;
		this.size = 0;
	}

	public boolean isComplete() {
		return (this.size == this.pattern.getNodeCount());
	}

	public Node mapToHostNode(Node paramNode) {
		return this.host.getNode(this.mapPH[paramNode.getIndex()]);
	}

	public Node mapToPatternNode(Node paramNode) {
		return this.pattern.getNode(this.mapHP[paramNode.getIndex()]);
	}

	public LinkedList<Pair> getMapping() {
		LinkedList<Pair> localLinkedList = new LinkedList<Pair>();
		for (int i = 0; i < this.mapPH.length; ++i)
			localLinkedList.add(new Pair(this.pattern.getNode(i), this.host
					.getNode(this.mapPH[i])));
		return localLinkedList;
	}

	public int getSize() {
		return this.size;
	}

	public void extend(Node paramNode1, Node paramNode2) {
		stateCount += 1L;
		this.mapPH[paramNode1.getIndex()] = paramNode2.getIndex();
		this.mapHP[paramNode2.getIndex()] = paramNode1.getIndex();
		this.size += 1;
	}

	public void remove(Node paramNode1, Node paramNode2) {
		this.mapPH[paramNode1.getIndex()] = -1;
		this.mapHP[paramNode2.getIndex()] = -1;
		this.size -= 1;
	}

	public boolean isFeasibleNodePair(Node paramNode1, Node paramNode2) {
		if ((this.mapHP[paramNode2.getIndex()] != -1)
				|| (this.mapPH[paramNode1.getIndex()] != -1))
			return false;
		if (!(this.nodeMatcher.match(paramNode1, paramNode2)))
			return false;
		return (paramNode1.getDegree() <= paramNode2.getDegree());
	}

	public boolean isFeasibleConnectedPair(Node paramNode1, Node paramNode2,
			Iterable<Edge> paramIterable) {
		Iterator<Edge> localIterator = paramIterable.iterator();
		while (localIterator.hasNext()) {
			Edge localEdge1 = localIterator.next();
			Node localNode = localEdge1.getOppositeNode(paramNode1);
			int i = this.mapPH[localNode.getIndex()];
			if (i != -1) {
				Edge localEdge2 = this.host.getEdge(this.host.getNode(i),
						paramNode2);
				if ((localEdge2 == null)
						|| (!(this.edgeMatcher.match(localEdge1, localEdge2))))
					return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < this.mapPH.length; ++i)
			localStringBuffer.append(i + " -> " + this.mapPH[i] + "\n");
		return localStringBuffer.toString();
	}
}