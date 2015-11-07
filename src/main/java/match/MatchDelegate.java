/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import java.util.Iterator;
import java.util.LinkedList;

public abstract interface MatchDelegate {
	public abstract boolean handleMatch(PartialMapping paramPartialMapping);

	public static class CollectAll implements MatchDelegate {
		private LinkedList<LinkedList<Pair>> matches = new LinkedList<LinkedList<Pair>>();

		@Override
		public boolean handleMatch(PartialMapping paramPartialMapping) {
			this.matches.add(paramPartialMapping.getMapping());
			return false;
		}

		public LinkedList<LinkedList<Pair>> getMatches() {
			return this.matches;
		}
	}

	public static class PrintAll implements MatchDelegate {
		@Override
		public boolean handleMatch(PartialMapping paramPartialMapping) {
			Iterator<?> localIterator = paramPartialMapping.getMapping()
					.iterator();
			while (localIterator.hasNext()) {
				Pair localPair = (Pair) localIterator.next();
				System.out.println(localPair.patternNode.getIndex() + " -> "
						+ localPair.hostNode.getIndex());
			}
			System.out.println("-----------------------");
			return false;
		}
	}
}