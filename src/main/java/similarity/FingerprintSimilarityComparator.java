/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package similarity;

import index.features.Fingerprint;

import java.util.Comparator;

public class FingerprintSimilarityComparator implements Comparator<Fingerprint> {
	protected Fingerprint reference;
	protected FingerprintSimilarityMeasure sm;

	public FingerprintSimilarityComparator(Fingerprint paramFingerprint,
			FingerprintSimilarityMeasure paramFingerprintSimilarityMeasure) {
		this.reference = paramFingerprint;
		this.sm = paramFingerprintSimilarityMeasure;
	}

	@Override
	public int compare(Fingerprint paramFingerprint1,
			Fingerprint paramFingerprint2) {
		double d1 = this.sm.calculateSimilarity(this.reference,
				paramFingerprint1);
		double d2 = this.sm.calculateSimilarity(this.reference,
				paramFingerprint2);
		if (d1 < d2)
			return -1;
		if (d1 > d2)
			return 1;
		return 0;
	}
}