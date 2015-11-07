/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package similarity;

import index.features.Fingerprint;

public class Tanimoto implements FingerprintSimilarityMeasure {
	@Override
	public double calculateSimilarity(Fingerprint paramFingerprint1,
			Fingerprint paramFingerprint2) {
		Fingerprint localFingerprint = new Fingerprint(paramFingerprint1);
		localFingerprint.and(paramFingerprint2);
		double d1 = paramFingerprint1.cardinality();
		double d2 = paramFingerprint2.cardinality();
		double d3 = localFingerprint.cardinality();
		return (d3 / (d1 + d2 - d3));
	}
}