/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Edge;

public abstract interface EdgeMatcher {
	public abstract boolean match(Edge paramEdge1, Edge paramEdge2);

	public static class Wildcards implements EdgeMatcher {
		@Override
		public boolean match(Edge paramEdge1, Edge paramEdge2) {
			Object localObject = paramEdge1.getLabel();
			if (localObject instanceof GraphQueryLabel)
				return ((GraphQueryLabel) localObject).matches(paramEdge2
						.getLabel());
			return localObject.equals(paramEdge2.getLabel());
		}
	}

	public static class Label implements EdgeMatcher {
		@Override
		public boolean match(Edge paramEdge1, Edge paramEdge2) {
			return paramEdge1.getLabel().equals(paramEdge2.getLabel());
		}
	}

	public static class IgnoreLabel implements EdgeMatcher {
		@Override
		public boolean match(Edge paramEdge1, Edge paramEdge2) {
			return true;
		}
	}
}