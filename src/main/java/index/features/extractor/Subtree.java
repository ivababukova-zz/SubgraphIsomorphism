/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.extractor;

import graph.DefaultEdge;
import graph.DefaultGraph;
import graph.DefaultNode;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Subtree extends DefaultGraph {
	int maxSize = 5;
	int n;
	int m;
	ArrayList<IndexEdge> edges;
	LinkedList<Node> activeNodes;
	BitSet iActiveNodes;
	LinkedList<IndexEdge> activeEdges;
	BitSet iActiveEdges;
	int[] activeDegree;
	final String[] canonicalLabelings;
	final Comparator<Node> nodeLabelingComparator;
	ArrayList<ArrayList<Node>> children;
	ArrayList<Node> level;
	ArrayList<Node> nextLevel;
	public boolean[] forbid;

	public Subtree(Graph paramGraph, int paramInt) {
		this.maxSize = paramInt;
		List<?> localList1 = paramGraph.nodes();
		List<?> localList2 = paramGraph.edges();
		this.n = localList1.size();
		this.m = localList2.size();
		this.edges = new ArrayList<IndexEdge>(localList2.size());
		Iterator<?> localIterator = localList1.iterator();
		Object localObject;
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			addNode(localObject);
		}
		localIterator = localList2.iterator();
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			addEdge(getNode(((Edge) localObject).getFirstNode().getIndex()),
					getNode(((Edge) localObject).getSecondNode().getIndex()),
					localObject);
		}
		this.activeNodes = new LinkedList<Node>();
		this.iActiveNodes = new BitSet(this.n);
		this.activeEdges = new LinkedList<IndexEdge>();
		this.iActiveEdges = new BitSet(this.m);
		this.activeDegree = new int[this.n];
		this.forbid = new boolean[this.m];
		this.canonicalLabelings = new String[this.n];
		this.nodeLabelingComparator = new Comparator<Node>() {
			@Override
			public int compare(Node paramNode1, Node paramNode2) {
				return Subtree.this.canonicalLabelings[paramNode1.getIndex()]
						.compareTo(Subtree.this.canonicalLabelings[paramNode2
								.getIndex()]);
			}
		};
		this.children = new ArrayList<ArrayList<Node>>(this.n);
		for (int i = 0; i < this.n; ++i)
			this.children.add(new ArrayList<Node>());
		this.level = new ArrayList<Node>();
		this.nextLevel = new ArrayList<Node>();
		clearLists();
	}

	@Override
	public void addEdge(DefaultNode paramDefaultNode1,
			DefaultNode paramDefaultNode2, Object paramObject) {
		IndexEdge localIndexEdge = new IndexEdge(paramDefaultNode1,
				paramDefaultNode2, paramObject, this.edges.size());
		paramDefaultNode1.addEdge(localIndexEdge);
		paramDefaultNode2.addEdge(localIndexEdge);
		this.edges.add(localIndexEdge);
		this.edgeCount += 1;
	}

	public void addActiveNode(Node paramNode) {
		this.iActiveNodes.set(paramNode.getIndex());
		this.activeNodes.push(paramNode);
	}

	public void removeLastActiveNode() {
		Node localNode = this.activeNodes.pop();
		this.iActiveNodes.clear(localNode.getIndex());
	}

	public void addActiveEdge(IndexEdge paramIndexEdge) {
		this.iActiveEdges.set(paramIndexEdge.getIndex());
		this.activeEdges.push(paramIndexEdge);
		Node localNode1 = paramIndexEdge.getFirstNode();
		Node localNode2 = paramIndexEdge.getSecondNode();
		if (this.iActiveNodes.get(localNode1.getIndex()))
			addActiveNode(localNode2);
		else
			addActiveNode(localNode1);
		this.activeDegree[localNode1.getIndex()] += 1;
		this.activeDegree[localNode2.getIndex()] += 1;
	}

	public void removeLastActiveEdge() {
		IndexEdge localIndexEdge = this.activeEdges.pop();
		this.iActiveEdges.clear(localIndexEdge.getIndex());
		removeLastActiveNode();
		this.activeDegree[localIndexEdge.getFirstNode().getIndex()] -= 1;
		this.activeDegree[localIndexEdge.getSecondNode().getIndex()] -= 1;
	}

	public void forbidEdge(IndexEdge paramIndexEdge) {
		this.forbid[paramIndexEdge.getIndex()] = true;
	}

	public void allowEdges(Collection<IndexEdge> paramCollection) {
		Iterator<IndexEdge> localIterator = paramCollection.iterator();
		while (localIterator.hasNext()) {
			IndexEdge localIndexEdge = localIterator.next();
			this.forbid[localIndexEdge.getIndex()] = false;
		}
	}

	public List<IndexEdge> getExtensions() {
		LinkedList<IndexEdge> localLinkedList = new LinkedList<IndexEdge>();
		if (this.activeNodes.size() > this.maxSize)
			return localLinkedList;
		Iterator<Node> localIterator1 = this.activeNodes.iterator();
		while (localIterator1.hasNext()) {
			Node localNode = localIterator1.next();
			Iterator<?> localIterator2 = localNode.getEdges().iterator();
			while (localIterator2.hasNext()) {
				Edge localEdge = (Edge) localIterator2.next();
				int i = ((IndexEdge) localEdge).getIndex();
				if ((!(this.iActiveEdges.get(i)))
						&& (!(this.iActiveNodes.get(localEdge.getOppositeNode(
								localNode).getIndex())))
						&& (this.forbid[i] == false))
					localLinkedList.add((IndexEdge) localEdge);
			}
		}
		return localLinkedList;
	}

	private Iterable<IndexEdge> getActiveEdges(final Node paramNode)
  {
    return new Iterable<IndexEdge>()
    {
    	private Node _paramNode = paramNode;
      @Override
	public Iterator<Subtree.IndexEdge> iterator()
      {
        return new Iterator<IndexEdge>()
        {
          Iterator<Edge> i = get_paramNode().getEdges().iterator();
          Subtree.IndexEdge current;

          @Override
		public boolean hasNext()
          {
            while (this.i.hasNext())
              if (Subtree.this.iActiveEdges.get((this.current = (Subtree.IndexEdge)this.i.next()).getIndex()))
                return true;
            return false;
          }

          @Override
		public Subtree.IndexEdge next()
          {
            return this.current;
          }

          @Override
		public void remove()
          {
            throw new UnsupportedOperationException();
          }
        };
      }
	public Node get_paramNode() {
		return this._paramNode;
	}
    };
  }

	public String getCanonicalLabeling() {
		Iterator<Node> iterator = this.activeNodes.iterator();
		do {
			if (!iterator.hasNext())
				break;
			Node node = iterator.next();
			if (this.activeDegree[node.getIndex()] <= 1)
				this.nextLevel.add(node);
		} while (true);
		int ai[] = this.activeDegree.clone();
		while (!this.nextLevel.isEmpty()) {
			ArrayList<Node> arraylist = this.level;
			this.level = this.nextLevel;
			this.nextLevel = arraylist;
			this.nextLevel.clear();
			Iterator<Node> iterator1 = this.level.iterator();
			while (iterator1.hasNext()) {
				Node node1 = iterator1.next();
				Iterator<?> iterator2 = getActiveEdges(node1).iterator();
				while (iterator2.hasNext()) {
					IndexEdge indexedge = (IndexEdge) iterator2.next();
					Node node2 = indexedge.getOppositeNode(node1);
					if (ai[node2.getIndex()] != 1 || !this.nextLevel.isEmpty()
							&& node2 == this.nextLevel.get(0)) {
						int i = --ai[node2.getIndex()];
						this.children.get(node2.getIndex()).add(node1);
						if (i == 1)
							this.nextLevel.add(node2);
					}
				}
			}
		}
		ArrayList<Node> arraylist1 = this.level;
		String s;
		if (arraylist1.size() == 1) {
			s = getCanonicalLabeling(arraylist1.get(0), this.children);
		} else {
			String s1 = getCanonicalLabeling(arraylist1.get(0), this.children);
			String s2 = getCanonicalLabeling(arraylist1.get(1), this.children);
			String s3 = FeatureExtractor.getLabel((Edge) getEdge(
					arraylist1.get(0), arraylist1.get(1))
					.getLabel());
			if (s1.compareTo(s2) < 0)
				s = (new StringBuilder()).append(s1).append(s3).append(s2)
						.toString();
			else
				s = (new StringBuilder()).append(s2).append(s3).append(s1)
						.toString();
		}
		clearLists();
		return s;
	}

	private String getCanonicalLabeling(Node paramNode,
			ArrayList<? extends List<Node>> paramArrayList) {
		int i = ((List<?>) paramArrayList.get(paramNode.getIndex())).size();
		if (i == 0)
			return FeatureExtractor.getLabel((Node) paramNode.getLabel()) + "$";
		if (i == 1) {
			Node localObject1 = (Node) ((List<?>) paramArrayList.get(paramNode
					.getIndex())).get(0);
			Edge localObject2 = getEdge(paramNode, localObject1);
			String localObject3 = FeatureExtractor
					.getLabel((Edge) localObject2.getLabel());
			this.canonicalLabelings[localObject1.getIndex()] = (localObject3)
					+ getCanonicalLabeling(localObject1, paramArrayList);
			return FeatureExtractor.getLabel((Node) paramNode.getLabel())
					+ this.canonicalLabelings[localObject1.getIndex()] + "$";
		}
		List<Node> localObject1 = paramArrayList.get(paramNode.getIndex());
		Iterator<Node> localObject2 = localObject1.iterator();
		while (localObject2.hasNext()) {
			Node localObject3 = localObject2.next();
			Edge localObject4 = getEdge(paramNode, localObject3);
			String str = FeatureExtractor.getLabel((Edge) localObject4
					.getLabel());
			this.canonicalLabelings[localObject3.getIndex()] = str
					+ getCanonicalLabeling(localObject3, paramArrayList);
		}
		Collections.sort(localObject1, this.nodeLabelingComparator);
		StringBuilder _localObject2 = new StringBuilder(
				FeatureExtractor.getLabel((Node) paramNode.getLabel()));
		Object localObject3 = localObject1.iterator();
		while (((Iterator<?>) localObject3).hasNext()) {
			Node localObject4 = (Node) ((Iterator<?>) localObject3).next();
			_localObject2
					.append(this.canonicalLabelings[localObject4.getIndex()]);
		}
		_localObject2.append("$");
		return (_localObject2
				.toString());
	}

	private void clearLists() {
		for (int i = 0; i < this.n; ++i)
			((ArrayList<?>) this.children.get(i)).clear();
		this.level.clear();
		this.nextLevel.clear();
	}

	@Override
	public String toString() {
		StringBuilder localStringBuilder = new StringBuilder();
		if (this.activeEdges.isEmpty()) {
			localStringBuilder.append(this.activeNodes.getFirst()
					.getIndex());
		} else {
			Iterator<IndexEdge> localIterator = this.activeEdges.iterator();
			while (localIterator.hasNext()) {
				IndexEdge localIndexEdge = localIterator.next();
				localStringBuilder.append(localIndexEdge.getFirstNode()
						.getIndex()
						+ " -("
						+ localIndexEdge.getIndex()
						+ ")- "
						+ localIndexEdge.getSecondNode().getIndex() + "\n");
			}
		}
		return localStringBuilder.toString();
	}

	public class IndexEdge extends DefaultEdge {
		private int index;

		protected IndexEdge(Node paramNode1, Node paramNode2,
				Object paramObject, int paramInt) {
			super(paramNode1, paramNode2, paramObject);
			this.index = paramInt;
		}

		public int getIndex() {
			return this.index;
		}
	}
}
