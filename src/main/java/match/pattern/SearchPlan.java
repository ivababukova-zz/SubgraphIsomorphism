/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SearchPlan {
	public static final boolean DEBUG = false;
	public static final int NO_PRIORITY = 0;
	public static final int RANDOM_PRIORITY = 1;
	public static final int ATOM_FREQUENCY_PRIORITY = 2;
	Extension[] order;

	public SearchPlan(Graph paramGraph, int paramInt) {
		Collection<Node> localObject1;
		List<Node> localObject2;
		Comparator<Node> _localObject3;
		switch (paramInt) {
		case 1:
			_localObject3 = new RandomComparator(paramGraph);
			localObject1 = new PriorityQueue<Node>(paramGraph.getNodeCount(),
					_localObject3);
			localObject2 = new LinkedList<Node>(paramGraph.nodes());
			Collections.sort(localObject2, _localObject3);
			break;
		case 2:
			_localObject3 = new AtomFrequencyComparator(paramGraph);
			localObject1 = new PriorityQueue<Node>(paramGraph.getNodeCount(),
					_localObject3);
			localObject2 = new LinkedList<Node>(paramGraph.nodes());
			Collections.sort(localObject2, _localObject3);
			break;
		default:
			localObject1 = new LinkedList<Node>();
			localObject2 = paramGraph.nodes();
		}
		this.order = new Extension[paramGraph.getNodeCount()];
		Extension[] localObject3 = new Extension[paramGraph.getNodeCount()];
		boolean[] arrayOfBoolean = new boolean[paramGraph.getNodeCount()];
		int i = 0;
		Iterator<?> localIterator1 = ((List<?>) localObject2).iterator();
		while (localIterator1.hasNext()) {
			Node localNode1 = (Node) localIterator1.next();
			if (arrayOfBoolean[localNode1.getIndex()] == false) {
				localObject1.add(localNode1);
				arrayOfBoolean[localNode1.getIndex()] = true;
				while (!(((Queue<?>) localObject1).isEmpty())) {
					Node localNode2 = (Node) ((Queue<?>) localObject1).poll();
					Extension localExtension = new Extension(localNode2);
					localObject3[localNode2.getIndex()] = localExtension;
					this.order[(i++)] = localExtension;
					Iterator<?> localIterator2 = localNode2.getEdges().iterator();
					while (localIterator2.hasNext()) {
						Edge localEdge = (Edge) localIterator2.next();
						Node localNode3 = localEdge.getOppositeNode(localNode2);
						if (arrayOfBoolean[localNode3.getIndex()] == false) {
							localObject1.add(localNode3);
							arrayOfBoolean[localNode3.getIndex()] = true;
						} else if (localObject3[localNode3.getIndex()] != null) {
							localObject3[localNode2.getIndex()]
									.addEdge(localEdge);
						}
					}
				}
			}
		}
	}

	public SearchPlan(Graph paramGraph) {
		this(paramGraph, 0);
	}

	public Extension getExtension(int paramInt) {
		return this.order[paramInt];
	}

	public class Extension {
		Node node;
		LinkedList<Edge> joinEdges;

		public Extension(Node paramNode) {
			this.node = paramNode;
			this.joinEdges = new LinkedList<Edge>();
		}

		public void addEdge(Edge paramEdge) {
			this.joinEdges.add(paramEdge);
		}

		public Node getNode() {
			return this.node;
		}

		public boolean hasJoinEdges() {
			return (!(this.joinEdges.isEmpty()));
		}

		public LinkedList<Edge> getJoinEdges() {
			return this.joinEdges;
		}
	}
}