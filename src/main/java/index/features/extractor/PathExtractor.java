/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.extractor;

import graph.Edge;
import graph.Graph;
import graph.Node;
import index.features.storage.FeatureStorage;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class PathExtractor extends FeatureExtractor<String> {
	public static final int PATHS = 0;
	public static final int SINGLE_CYCLE_SIMPLE_PATHS = 1;
	public static final int SIMPLE_PATHS = 2;
	private int maxSize;
	private int type;

	public PathExtractor(Graph paramGraph,
			FeatureStorage<? super String, ?> paramFeatureStorage,
			int paramInt1, int paramInt2) {
		super(paramGraph, paramFeatureStorage);
		this.maxSize = paramInt1;
		this.type = paramInt2;
	}

	@Override
	public void extractFeatures() {
		Object localObject;
		Iterator<?> localIterator;
		Node localNode;
		switch (this.type) {
		case 0:
			localObject = new Path();
			localIterator = this.graph.nodes().iterator();
			while (localIterator.hasNext()) {
				localNode = (Node) localIterator.next();
				pathSearch(localNode, this.maxSize, (Path) localObject);
			}
			break;
		case 2:
			localObject = new Path();
			localIterator = this.graph.nodes().iterator();
			while (localIterator.hasNext()) {
				localNode = (Node) localIterator.next();
				simplePathSearch(localNode, this.maxSize, (Path) localObject);
			}
			break;
		case 1:
			localObject = new SingleCyclePath();
			localIterator = this.graph.nodes().iterator();
			while (localIterator.hasNext()) {
				localNode = (Node) localIterator.next();
				singleCyclePathSearch(localNode, this.maxSize,
						(SingleCyclePath) localObject);
			}
		}
	}

	private void simplePathSearch(Node paramNode, int paramInt, Path paramPath) {
		paramPath.push(paramNode);
		this.featureStorage.processFeature(paramPath.getLexSmallerLabelPath());
		Iterator<?> localIterator = paramNode.getEdges().iterator();
		while (localIterator.hasNext()) {
			Edge localEdge = (Edge) localIterator.next();
			Node localNode = localEdge.getOppositeNode(paramNode);
			if ((!(paramPath.contains(localNode))) && (paramInt > 0))
				simplePathSearch(localNode, paramInt - 1, paramPath);
		}
		paramPath.pop();
	}

	private void pathSearch(Node paramNode, int paramInt, Path paramPath) {
		Node localNode1 = paramPath.peekLast();
		paramPath.push(paramNode);
		this.featureStorage.processFeature(paramPath.getLexSmallerLabelPath());
		Iterator<?> localIterator = paramNode.getEdges().iterator();
		while (localIterator.hasNext()) {
			Edge localEdge = (Edge) localIterator.next();
			Node localNode2 = localEdge.getOppositeNode(paramNode);
			if ((localNode2 != localNode1) && (paramInt > 0))
				pathSearch(localNode2, paramInt - 1, paramPath);
		}
		paramPath.pop();
	}

	private void singleCyclePathSearch(Node paramNode, int paramInt,
			SingleCyclePath paramSingleCyclePath) {
		Node localNode1 = paramSingleCyclePath.peekLast();
		paramSingleCyclePath.push(paramNode);
		this.featureStorage.processFeature(paramSingleCyclePath
				.getLexSmallerLabelPath());
		Iterator<?> localIterator = paramNode.getEdges().iterator();
		while (localIterator.hasNext()) {
			Edge localEdge = (Edge) localIterator.next();
			Node localNode2 = localEdge.getOppositeNode(paramNode);
			if ((localNode2 != localNode1)
					&& (paramInt > 0)
					&& (((!(paramSingleCyclePath.contains(localNode2))) || ((!(paramSingleCyclePath
							.containsCycle())) && (localNode2 != paramSingleCyclePath
							.peekFirst())))))
				singleCyclePathSearch(localNode2, paramInt - 1,
						paramSingleCyclePath);
		}
		paramSingleCyclePath.pop();
	}

	public class SingleCyclePath {
		PathExtractor extractor = PathExtractor.this;
		PathExtractor.Path part1 = new PathExtractor.Path();
		PathExtractor.Path cycle = new PathExtractor.Path();
		PathExtractor.Path part2 = new PathExtractor.Path();
		PathExtractor.Path currentPath = this.part1;

		public void push(Node paramNode) {
			if (this.part1.contains(paramNode)) {
				this.cycle.push(paramNode);
				Node localNode;
				do {
					localNode = this.part1.pop();
					this.cycle.push(localNode);
				} while (localNode != paramNode);
				this.currentPath = this.cycle;
			} else {
				if (this.currentPath == this.cycle)
					this.currentPath = this.part2;
				this.currentPath.push(paramNode);
			}
		}

		public Node pop() {
			if (this.currentPath == this.cycle) {
				while (!(this.cycle.isEmpty()))
					this.part1.push(this.cycle.pop());
				this.currentPath = this.part1;
				return this.part1.pop();
			}
			Node localNode = this.currentPath.pop();
			if ((this.currentPath.isEmpty())
					&& (this.currentPath == this.part2))
				this.currentPath = this.cycle;
			return localNode;
		}

		public Node peekLast() {
			return this.currentPath.peekLast();
		}

		public Node peekFirst() {
			return this.currentPath.peekFirst();
		}

		public boolean contains(Node paramNode) {
			return ((this.part1.contains(paramNode))
					|| (this.cycle.contains(paramNode)) || (this.part2
						.contains(paramNode)));
		}

		public boolean containsCycle() {
			return (!(this.cycle.isEmpty()));
		}

		public boolean isEmpty() {
			return ((this.part1.isEmpty()) && (this.cycle.isEmpty()));
		}

		private String getLabelPath(boolean paramBoolean) {
			String str1 = "";
			String str2 = "";
			String str3 = "";
			Edge localEdge;
			if (containsCycle()) {
				if (!(this.part1.isEmpty())) {
					localEdge = PathExtractor.this.graph.getEdge(
							this.part1.peekLast(), this.cycle.peekFirst());
					str1 = FeatureExtractor.getLabel(localEdge);
				}
				str2 = "!";
			}
			if (!(this.part2.isEmpty())) {
				localEdge = PathExtractor.this.graph.getEdge(
						this.cycle.peekLast(), this.part2.peekFirst());
				str3 = FeatureExtractor.getLabel(localEdge);
			}
			if (paramBoolean)
				return this.part2.getReverseLabelPath() + str3 + str2
						+ this.cycle.getLexSmallerLabelPath() + str2 + str1
						+ this.part1.getReverseLabelPath();
			return this.part1.getLabelPath() + str1 + str2
					+ this.cycle.getLexSmallerLabelPath() + str2 + str3
					+ this.part2.getLabelPath();
		}

		public String getLabelPath() {
			return getLabelPath(false);
		}

		public String getReverseLabelPath() {
			return getLabelPath(true);
		}

		public String getLexSmallerLabelPath() {
			String str1 = getLabelPath();
			String str2 = getReverseLabelPath();
			return ((str1.compareTo(str2) < 0) ? str1 : str2);
		}
	}

	public class Path {
		LinkedList<Node> nodePath = new LinkedList<Node>();
		LinkedList<Edge> edgePath = new LinkedList<Edge>();
		int[] pathMember = new int[PathExtractor.this.graph.getNodeCount()];

		public void push(Node paramNode) {
			if (!(this.nodePath.isEmpty()))
				this.edgePath.addLast(PathExtractor.this.graph.getEdge(
						this.nodePath.getLast(), paramNode));
			this.nodePath.addLast(paramNode);
			if (this.pathMember[paramNode.getIndex()] != 0)
				return;
			this.pathMember[paramNode.getIndex()] = this.nodePath.size();
		}

		public Node pop() {
			Node localNode = this.nodePath.removeLast();
			if (!(this.nodePath.isEmpty()))
				this.edgePath.removeLast();
			if (this.pathMember[localNode.getIndex()] > this.nodePath.size())
				this.pathMember[localNode.getIndex()] = 0;
			return localNode;
		}

		public Node peekLast() {
			return (this.nodePath.peekLast());
		}

		public Node peekFirst() {
			return (this.nodePath.peekFirst());
		}

		public boolean contains(Node paramNode) {
			return (this.pathMember[paramNode.getIndex()] != 0);
		}

		public int indexOf(Node paramNode) {
			return (this.pathMember[paramNode.getIndex()] - 1);
		}

		public boolean isEmpty() {
			return this.nodePath.isEmpty();
		}

		private String getLabelPath(LinkedList<Node> paramLinkedList,
				LinkedList<Edge> paramLinkedList1, boolean paramBoolean) {
			if (isEmpty())
				return "";
			StringBuilder localStringBuilder = new StringBuilder();
			Iterator<Node> localIterator1;
			Iterator<Edge> localIterator2;
			if (paramBoolean) {
				localIterator1 = paramLinkedList.descendingIterator();
				localIterator2 = paramLinkedList1.descendingIterator();
			} else {
				localIterator1 = paramLinkedList.iterator();
				localIterator2 = paramLinkedList1.iterator();
			}
			localStringBuilder.append(FeatureExtractor
					.getLabel(localIterator1.next()));
			while (localIterator1.hasNext()) {
				localStringBuilder.append(FeatureExtractor
						.getLabel(localIterator2.next()));
				localStringBuilder.append(FeatureExtractor
						.getLabel(localIterator1.next()));
			}
			return localStringBuilder.toString();
		}

		public String getLabelPath() {
			return getLabelPath(this.nodePath, this.edgePath, false);
		}

		public String getReverseLabelPath() {
			return getLabelPath(this.nodePath, this.edgePath, true);
		}

		public String getLexSmallerLabelPath() {
			String str1 = getLabelPath();
			String str2 = getReverseLabelPath();
			return ((str1.compareTo(str2) < 0) ? str1 : str2);
		}

		public Path subPath(int paramInt1, int paramInt2) {
			int _paramInt1 = paramInt1;
			Path localPath = new Path(); //XXX: PathExtractor.this);
			ListIterator<Node> localListIterator = this.nodePath
					.listIterator(paramInt1);
			while (_paramInt1++ < paramInt2)
				localPath.push(localListIterator.next());
			return localPath;
		}

		public int size() {
			return this.nodePath.size();
		}

		public String getEdgeLabel(int paramInt) {
			return FeatureExtractor.getLabel(this.edgePath
					.get(paramInt - 1));
		}
	}
}
