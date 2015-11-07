/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.storage;

import index.util.Hashing;

import java.util.HashSet;

public class IntHashSet implements FeatureStorage<Object, HashSet<Integer>> {
	private HashSet<Integer> features = new HashSet<Integer>();

	@Override
	public void processFeature(Object paramObject) {
		int i = Hashing.hash(Integer.valueOf(paramObject.hashCode()),
				2147483647);
		this.features.add(Integer.valueOf(i));
	}

	@Override
	public int getFeatureCount() {
		return this.features.size();
	}

	@Override
	public HashSet<Integer> getResult() {
		return this.features;
	}
}