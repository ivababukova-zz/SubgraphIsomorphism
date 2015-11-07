/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index;

import graph.Graph;
import index.features.FingerprintBuilder;

import java.util.List;

public abstract class Index {
	protected FingerprintBuilder fpBuilder;

	public Index(FingerprintBuilder paramFingerprintBuilder) {
		this.fpBuilder = paramFingerprintBuilder;
	}

	public FingerprintBuilder getFingerprintBuilder() {
		return this.fpBuilder;
	}

	public abstract void open();

	public abstract void close();

	public abstract void create();

	public abstract void clear();

	public abstract void addGraph(int paramInt, Graph paramGraph);

	public abstract List<Integer> findCandidates(Graph paramGraph);

	public String getStatistics() {
		return "Statistics not available.";
	}
}