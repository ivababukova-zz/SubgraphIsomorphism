/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.Iterator;
import java.util.List;

import match.pattern.SearchPattern;

public class PartialMappingTerminal extends PartialMapping {
	protected int[] termP = new int[this.pattern.getNodeCount()];
	protected int[] termH = new int[this.host.getNodeCount()];

	public PartialMappingTerminal(SearchPattern paramSearchPattern,
			Graph paramGraph) {
		super(paramSearchPattern, paramGraph);
	}

	@Override
	public void extend(Node paramNode1, Node paramNode2) {
		super.extend(paramNode1, paramNode2);
		List<?> localList = paramNode1.getEdges();
		Iterator<?> localIterator = localList.iterator();
		Edge localEdge;
		Node localNode;
		while (localIterator.hasNext()) {
			localEdge = (Edge) localIterator.next();
			localNode = localEdge.getOppositeNode(paramNode1);
			if (this.termP[localNode.getIndex()] == 0)
				this.termP[localNode.getIndex()] = this.size;
		}
		localList = paramNode2.getEdges();
		localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			localEdge = (Edge) localIterator.next();
			localNode = localEdge.getOppositeNode(paramNode2);
			if (this.termH[localNode.getIndex()] == 0)
				this.termH[localNode.getIndex()] = this.size;
		}
	}

	@Override
	public void remove(Node paramNode1, Node paramNode2) {
		List<?> localList = paramNode1.getEdges();
		Iterator<?> localIterator = localList.iterator();
		Edge localEdge;
		Node localNode;
		while (localIterator.hasNext()) {
			localEdge = (Edge) localIterator.next();
			localNode = localEdge.getOppositeNode(paramNode1);
			if (this.termP[localNode.getIndex()] == this.size)
				this.termP[localNode.getIndex()] = 0;
		}
		localList = paramNode2.getEdges();
		localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			localEdge = (Edge) localIterator.next();
			localNode = localEdge.getOppositeNode(paramNode2);
			if (this.termH[localNode.getIndex()] == this.size)
				this.termH[localNode.getIndex()] = 0;
		}
		super.remove(paramNode1, paramNode2);
	}

	public boolean isFeasibleCandidate(Node paramNode1, Node paramNode2) {
		if (!(this.nodeMatcher.match(paramNode1, paramNode2)))
			return false;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		List<?> localList = paramNode1.getEdges();
		Iterator<?> localIterator = localList.iterator();
		Edge localEdge1;
		Node localNode;
		int i1;
		while (localIterator.hasNext()) {
			localEdge1 = (Edge) localIterator.next();
			localNode = localEdge1.getOppositeNode(paramNode1);
			i1 = this.mapPH[localNode.getIndex()];
			if (i1 != -1) {
				Edge localEdge2 = this.host.getEdge(this.host.getNode(i1),
						paramNode2);
				if ((localEdge2 == null)
						|| (!(this.edgeMatcher.match(localEdge1, localEdge2))))
					return false;
			} else if (this.termP[localNode.getIndex()] != 0) {
				++i;
			} else {
				++k;
			}
		}
		localList = paramNode2.getEdges();
		localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			localEdge1 = (Edge) localIterator.next();
			localNode = localEdge1.getOppositeNode(paramNode2);
			i1 = this.mapHP[localNode.getIndex()];
			if (i1 == -1)
				if (this.termH[localNode.getIndex()] != 0)
					++j;
				else
					++l;
		}
		return ((i <= j) && (i + k <= j + l));
	}

	public Node nextTerminalHostNode(Node paramNode1, Node paramNode2) {
		int i = (paramNode2 == null) ? 0 : paramNode2.getIndex() + 1;
		boolean skip = false;
		if (this.termP[paramNode1.getIndex()] != 0)
			while (true) {
				if ((i >= this.host.getNodeCount())
						|| ((this.termH[i] != 0) && (this.mapHP[i] == -1))) {
					skip = true;
					break;
				}
				++i;
			}
		if (!skip)
			while ((i < this.host.getNodeCount()) && (this.mapHP[i] != -1))
				++i;
		if (i < this.host.getNodeCount())
			return this.host.getNode(i);
		return null;
	}
}