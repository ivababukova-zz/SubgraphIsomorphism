/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features;

import graph.Graph;
import index.features.extractor.PathExtractor;
import index.features.extractor.RingExtractor;
import index.features.extractor.SubtreeExtractor;
import index.features.storage.CountHashTable;
import index.features.storage.FeatureStorage;
import index.features.storage.HashKeyFingerprint;
import index.features.storage.IntHashSet;

import java.util.HashMap;
import java.util.HashSet;

public class FingerprintBuilder {
	private boolean findPaths = false;
	private boolean findSubtrees = true;
	private boolean findRings = true;
	private int pathSize = 8;
	private int subtreeSize = 5;
	private int ringSize = 8;
	private int fingerprintSize;

	public FingerprintBuilder(int paramInt) {
		this.fingerprintSize = paramInt;
	}

	public FingerprintBuilder(int paramInt1, boolean paramBoolean1,
			boolean paramBoolean2, boolean paramBoolean3, int paramInt2,
			int paramInt3, int paramInt4) {
		this.fingerprintSize = paramInt1;
		this.findPaths = paramBoolean1;
		this.findSubtrees = paramBoolean2;
		this.findRings = paramBoolean3;
		this.pathSize = paramInt2;
		this.subtreeSize = paramInt3;
		this.ringSize = paramInt4;
	}

	public FingerprintBuilder(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
		this.fingerprintSize = paramInt1;
		this.findPaths = (paramInt2 >= 0);
		this.findSubtrees = (paramInt3 >= 0);
		this.findRings = (paramInt4 >= 3);
		this.pathSize = paramInt2;
		this.subtreeSize = paramInt3;
		this.ringSize = paramInt4;
	}

	private void extractFeatures(
			FeatureStorage<? super String, ?> paramFeatureStorage,
			Graph paramGraph) {
		if (this.findPaths) {
			PathExtractor localObject = new PathExtractor(paramGraph, paramFeatureStorage,
					this.pathSize, 2);
			localObject.extractFeatures();
		}
		if (this.findSubtrees) {
			SubtreeExtractor localObject = new SubtreeExtractor(paramGraph, paramFeatureStorage,
					this.subtreeSize);
			localObject.extractFeatures();
		}
		if (!(this.findRings))
			return;
		Object localObject = new RingExtractor(paramGraph, paramFeatureStorage,
				this.ringSize);
		((RingExtractor) localObject).extractFeatures();
	}

	public Fingerprint getFingerprint(Graph paramGraph) {
		HashKeyFingerprint localHashKeyFingerprint = new HashKeyFingerprint(
				this.fingerprintSize);
		extractFeatures(localHashKeyFingerprint, paramGraph);
		return localHashKeyFingerprint.getResult();
	}

	public int getFingerprintSize() {
		return this.fingerprintSize;
	}

	public HashSet<Integer> getIntHashSet(Graph paramGraph) {
		IntHashSet localIntHashSet = new IntHashSet();
		extractFeatures(localIntHashSet, paramGraph);
		return localIntHashSet.getResult();
	}

	public HashMap<String, Integer> getCountHashTable(Graph paramGraph) {
		CountHashTable<String> localCountHashTable = new CountHashTable<String>();
		extractFeatures(localCountHashTable, paramGraph);
		return localCountHashTable.getResult();
	}

	@Override
	public String toString() {
		return ((this.findPaths) ? "Paths " + this.pathSize + " " : "")
				+ ((this.findSubtrees) ? "Subtrees " + this.subtreeSize + " "
						: "")
				+ ((this.findRings) ? "Rings " + this.ringSize + " " : "");
	}

	public void setFindPaths(boolean paramBoolean) {
		this.findPaths = paramBoolean;
	}

	public boolean isFindPaths() {
		return this.findPaths;
	}

	public void setFindSubtrees(boolean paramBoolean) {
		this.findSubtrees = paramBoolean;
	}

	public boolean isFindSubtrees() {
		return this.findSubtrees;
	}

	public void setFindRings(boolean paramBoolean) {
		this.findRings = paramBoolean;
	}

	public boolean isFindRings() {
		return this.findRings;
	}

	public void setPathSize(int paramInt) {
		this.pathSize = paramInt;
	}

	public int getPathSize() {
		return this.pathSize;
	}

	public void setSubtreeSize(int paramInt) {
		this.subtreeSize = paramInt;
	}

	public int getSubtreeSize() {
		return this.subtreeSize;
	}

	public void setRingSize(int paramInt) {
		this.ringSize = paramInt;
	}

	public int getRingSize() {
		return this.ringSize;
	}

	public void setFingerprintSize(int paramInt) {
		this.fingerprintSize = paramInt;
	}
}