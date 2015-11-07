/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.storage;

import index.features.Fingerprint;
import index.util.Hashing;

public class HashKeyFingerprint implements FeatureStorage<Object, Fingerprint> {
	public static final int defaultSize = 1024;
	private int size;
	private Fingerprint fingerprint;
	private int featureCount;

	public HashKeyFingerprint() {
		this(1024);
	}

	public HashKeyFingerprint(int paramInt) {
		this.featureCount = 0;
		this.size = paramInt;
		this.fingerprint = new Fingerprint(paramInt);
	}

	@Override
	public void processFeature(Object paramObject) {
		int i = Hashing.hash(paramObject, this.size);
		this.fingerprint.set(i);
		this.featureCount += 1;
	}

	@Override
	public int getFeatureCount() {
		return this.featureCount;
	}

	@Override
	public Fingerprint getResult() {
		return this.fingerprint;
	}
}