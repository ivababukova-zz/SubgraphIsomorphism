/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.extractor;

import graph.Graph;
import graph.Node;
import index.features.extractor.Subtree.IndexEdge;
import index.features.storage.FeatureStorage;

import java.util.Iterator;
import java.util.List;

public class SubtreeExtractor extends FeatureExtractor<String> {
	int maxSize;
	public int features = 0;

	public SubtreeExtractor(Graph paramGraph,
			FeatureStorage<? super String, ?> paramFeatureStorage, int paramInt) {
		super(paramGraph, paramFeatureStorage);
		this.maxSize = paramInt;
	}

	@Override
	public void extractFeatures() {
		Subtree localSubtree = new Subtree(this.graph, this.maxSize);
		Iterator<?> localIterator1 = localSubtree.nodes().iterator();
		while (localIterator1.hasNext()) {
			Node localNode = (Node) localIterator1.next();
			localSubtree.addActiveNode(localNode);
			this.featureStorage.processFeature(localSubtree
					.getCanonicalLabeling());
			this.features += 1;
			Iterator<?> localIterator2 = localSubtree.getExtensions().iterator();
			while (localIterator2.hasNext()) {
				Subtree.IndexEdge localIndexEdge = (Subtree.IndexEdge) localIterator2
						.next();
				if (localNode.getIndex() > localIndexEdge.getOppositeNode(
						localNode).getIndex())
					extendSubtreeByEdge(localSubtree, localIndexEdge);
			}
			localSubtree.removeLastActiveNode();
		}
	}

	private void extendSubtreeByEdge(Subtree paramSubtree,
			Subtree.IndexEdge paramIndexEdge) {
		paramSubtree.addActiveEdge(paramIndexEdge);
		this.featureStorage.processFeature(paramSubtree.getCanonicalLabeling());
		this.features += 1;
		List<IndexEdge> localList = paramSubtree.getExtensions();
		Iterator<IndexEdge> localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			Subtree.IndexEdge localIndexEdge = localIterator
					.next();
			extendSubtreeByEdge(paramSubtree, localIndexEdge);
		}
		paramSubtree.allowEdges(localList);
		paramSubtree.removeLastActiveEdge();
		paramSubtree.forbidEdge(paramIndexEdge);
	}
}