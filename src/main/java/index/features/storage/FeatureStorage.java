/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.storage;

public abstract interface FeatureStorage<I, O> {
	public abstract void processFeature(I paramI);

	public abstract O getResult();

	public abstract int getFeatureCount();
}