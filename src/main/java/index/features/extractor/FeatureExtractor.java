/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.extractor;

import graph.Edge;
import graph.Graph;
import graph.Node;
import index.features.storage.FeatureStorage;

public abstract class FeatureExtractor<I> {
	protected FeatureStorage<? super I, ?> featureStorage;
	protected Graph graph;

	public FeatureExtractor() {
	}

	public FeatureExtractor(Graph paramGraph,
			FeatureStorage<? super I, ?> paramFeatureStorage) {
		this.graph = paramGraph;
		this.featureStorage = paramFeatureStorage;
	}

	public FeatureStorage<? super I, ?> getFeatureStorage() {
		return this.featureStorage;
	}

	public abstract void extractFeatures();

	public static String getLabel(Node paramNode) {
		return paramNode.getLabel().toString();
	}

	public static String getLabel(Edge paramEdge) {
		return paramEdge.getLabel().toString();
	}
}