/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package similarity;

import index.features.Fingerprint;

public abstract interface FingerprintSimilarityMeasure {
	public abstract double calculateSimilarity(Fingerprint paramFingerprint1,
			Fingerprint paramFingerprint2);
}