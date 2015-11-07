/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class PatternOrder {
	public abstract int[] createOrder(Graph paramGraph);

	public static int[] createSimpleOrder(Graph paramGraph) {
		int[] arrayOfInt = new int[paramGraph.getNodeCount()];
		for (int i = 0; i < arrayOfInt.length; ++i)
			arrayOfInt[i] = i;
		return arrayOfInt;
	}

	public static int[] createBFSOrder(Graph paramGraph) {
		int[] arrayOfInt = new int[paramGraph.getNodeCount()];
		boolean[] arrayOfBoolean = new boolean[paramGraph.getNodeCount()];
		int i = 0;
		LinkedList<Node> localLinkedList = new LinkedList<Node>();
		Iterator<?> localIterator1 = paramGraph.nodes().iterator();
		while (localIterator1.hasNext()) {
			Node localNode1 = (Node) localIterator1.next();
			if (arrayOfBoolean[localNode1.getIndex()] == false) {
				localLinkedList.add(localNode1);
				arrayOfBoolean[localNode1.getIndex()] = true;
				while (!(localLinkedList.isEmpty())) {
					Node localNode2 = localLinkedList.poll();
					arrayOfInt[(i++)] = localNode2.getIndex();
					Iterator<?> localIterator2 = localNode2.getEdges().iterator();
					while (localIterator2.hasNext()) {
						Edge localEdge = (Edge) localIterator2.next();
						Node localNode3 = localEdge.getOppositeNode(localNode2);
						if (arrayOfBoolean[localNode3.getIndex()] == false) {
							localLinkedList.add(localNode3);
							arrayOfBoolean[localNode3.getIndex()] = true;
						}
					}
				}
			}
		}
		return arrayOfInt;
	}

	public static class DFSPatternOrder extends PatternOrder {
		private int[] order;
		private boolean[] found;
		int c;

		@Override
		public int[] createOrder(Graph paramGraph) {
			this.order = new int[paramGraph.getNodeCount()];
			this.found = new boolean[paramGraph.getNodeCount()];
			Iterator<?> localIterator = paramGraph.nodes().iterator();
			while (localIterator.hasNext()) {
				Node localNode = (Node) localIterator.next();
				if (this.found[localNode.getIndex()] == false)
					doDFS(localNode);
			}
			return this.order;
		}

		private void doDFS(Node paramNode) {
			this.found[paramNode.getIndex()] = true;
			this.order[(this.c++)] = paramNode.getIndex();
			Iterator<?> localIterator = paramNode.getEdges().iterator();
			while (localIterator.hasNext()) {
				Edge localEdge = (Edge) localIterator.next();
				Node localNode = localEdge.getOppositeNode(paramNode);
				if (this.found[localNode.getIndex()] == false)
					doDFS(localNode);
			}
		}
	}
}