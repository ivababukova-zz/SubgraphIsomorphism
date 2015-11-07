/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CountHashTable<F> implements
		FeatureStorage<F, HashMap<F, Integer>> {
	private HashMap<F, Integer> features = new HashMap<F, Integer>();

	@Override
	public void processFeature(F paramF) {
		Integer localInteger = this.features.get(paramF);
		if (localInteger == null)
			this.features.put(paramF, Integer.valueOf(1));
		else
			this.features.put(paramF,
					Integer.valueOf(localInteger.intValue() + 1));
	}

	@Override
	public int getFeatureCount() {
		return this.features.size();
	}

	@Override
	public HashMap<F, Integer> getResult() {
		return this.features;
	}

	@Override
	public String toString() {
		StringBuilder localStringBuilder = new StringBuilder();
		Iterator<Map.Entry<F, Integer>> localIterator = this.features.entrySet().iterator();
		while (localIterator.hasNext()) {
			Map.Entry<F, Integer> localEntry = localIterator.next();
			localStringBuilder.append(localEntry.getKey());
			localStringBuilder.append(" ");
			localStringBuilder.append(localEntry.getValue());
			localStringBuilder.append("\n");
		}
		return localStringBuilder.toString();
	}
}