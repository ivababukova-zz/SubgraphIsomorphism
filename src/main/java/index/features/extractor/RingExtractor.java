/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.extractor;

import graph.DefaultGraph;
import graph.Edge;
import graph.Graph;
import graph.Node;
import index.features.storage.FeatureStorage;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class RingExtractor extends FeatureExtractor<String> {
	private int maxSize;

	public RingExtractor(Graph paramGraph,
			FeatureStorage<? super String, ?> paramFeatureStorage, int paramInt) {
		super(paramGraph, paramFeatureStorage);
		this.maxSize = paramInt;
	}

	@Override
	public void extractFeatures() {
		boolean[] arrayOfBoolean = new boolean[this.graph.getNodeCount()];
		Stack<Node> localStack = new Stack<Node>();
		Iterator<?> localIterator = this.graph.nodes().iterator();
		while (localIterator.hasNext()) {
			Node localNode = (Node) localIterator.next();
			search(localNode, arrayOfBoolean, localStack, localNode, 1);
		}
	}

	private void search(Node paramNode1, boolean[] paramArrayOfBoolean,
			Stack<Node> paramStack, Node paramNode2, int paramInt) {
		paramStack.push(paramNode2);
		paramArrayOfBoolean[paramNode2.getIndex()] = true;
		Iterator<?> localIterator = paramNode2.getEdges().iterator();
		while (localIterator.hasNext()) {
			Edge localEdge = (Edge) localIterator.next();
			Node localNode = localEdge.getOppositeNode(paramNode2);
			if ((paramArrayOfBoolean[localNode.getIndex()] == false)
					&& (paramInt < this.maxSize)
					&& (localNode.getIndex() > paramNode1.getIndex()))
				search(paramNode1, paramArrayOfBoolean, paramStack, localNode,
						paramInt + 1);
			else if ((localNode == paramNode1)
					&& (paramStack.size() > 2)
					&& (paramNode2.getIndex() > paramStack.get(1)
							.getIndex()))
				processCircle(paramStack);
		}
		paramStack.pop();
		paramArrayOfBoolean[paramNode2.getIndex()] = false;
	}

	private void processCircle(Stack<Node> paramStack) {
		DefaultGraph localDefaultGraph = new DefaultGraph(paramStack.size());
		Iterator<Node> localIterator = paramStack.iterator();
		while (localIterator.hasNext()) {
			Node localObject2 = localIterator.next();
			localDefaultGraph.addNode(localObject2.getLabel());
		}
		for (int localObject1 = 0; localObject1 < paramStack.size(); ++localObject1) {
			int localObject2 = localObject1;
			int i = localObject1 + 1;
			if (i == paramStack.size())
				i = 0;
			Edge localEdge = this.graph.getEdge(
					paramStack.get(localObject2),
					paramStack.get(i));
			localDefaultGraph.addEdge(localDefaultGraph.getNode(localObject2),
					localDefaultGraph.getNode(i), localEdge.getLabel());
		}
		String str = "$" + getLabel(localDefaultGraph) + "$";
		this.featureStorage.processFeature(str);
		Object localObject2 = new StringBuilder(str);
		this.featureStorage.processFeature(((StringBuilder) localObject2)
				.reverse().toString());
	}

	private String getLabel(Graph paramGraph) {
		Object localObject = "";
		Iterator<?> localIterator1 = paramGraph.nodes().iterator();
		while (localIterator1.hasNext()) {
			Node localNode = (Node) localIterator1.next();
			Iterator<?> localIterator2 = localNode.getEdges().iterator();
			while (localIterator2.hasNext()) {
				Edge localEdge = (Edge) localIterator2.next();
				StringBuilder localStringBuilder = new StringBuilder();
				traverseRing(localNode, localNode, localEdge,
						localStringBuilder);
				String str = localStringBuilder.toString();
				if (str.compareTo((String) localObject) > 0)
					localObject = str;
			}
		}
		return ((String) localObject);
	}

	private void traverseRing(Node paramNode1, Node paramNode2, Edge paramEdge,
			StringBuilder paramStringBuilder) {
		paramStringBuilder.append(getLabel(paramNode2));
		paramStringBuilder.append(getLabel(paramEdge));
		Node localNode = paramEdge.getOppositeNode(paramNode2);
		if (localNode == paramNode1)
			return;
		List<?> localList = localNode.getEdges();
		Edge localEdge = (Edge) localList.get(0);
		if (localEdge == paramEdge)
			localEdge = (Edge) localList.get(1);
		traverseRing(paramNode1, localNode, localEdge, paramStringBuilder);
	}
}