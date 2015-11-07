/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package similarity;

import index.features.Fingerprint;

import java.util.HashMap;

public class CacheFingerprintSimilarityComparator extends
		FingerprintSimilarityComparator {
	private HashMap<Fingerprint, Double> cache = new HashMap<Fingerprint, Double>();

	public CacheFingerprintSimilarityComparator(Fingerprint paramFingerprint,
			FingerprintSimilarityMeasure paramFingerprintSimilarityMeasure) {
		super(paramFingerprint, paramFingerprintSimilarityMeasure);
	}

	@Override
	public int compare(Fingerprint paramFingerprint1,
			Fingerprint paramFingerprint2) {
		Double localDouble1 = this.cache.get(paramFingerprint1);
		if (localDouble1 == null) {
			localDouble1 = Double.valueOf(this.sm.calculateSimilarity(
					this.reference, paramFingerprint1));
			this.cache.put(paramFingerprint1, localDouble1);
		}
		Double localDouble2 = this.cache.get(paramFingerprint2);
		if (localDouble2 == null) {
			localDouble2 = Double.valueOf(this.sm.calculateSimilarity(
					this.reference, paramFingerprint2));
			this.cache.put(paramFingerprint2, localDouble2);
		}
		if (localDouble1.doubleValue() < localDouble2.doubleValue())
			return -1;
		if (localDouble1.doubleValue() > localDouble2.doubleValue())
			return 1;
		return 0;
	}
}