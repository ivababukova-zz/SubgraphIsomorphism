/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Node;

public abstract interface NodeMatcher {
	public abstract boolean match(Node paramNode1, Node paramNode2);

	public static class Wildcards implements NodeMatcher {
		@Override
		public boolean match(Node paramNode1, Node paramNode2) {
			Object localObject = paramNode1.getLabel();
			if (localObject instanceof GraphQueryLabel)
				return ((GraphQueryLabel) localObject).matches(paramNode2
						.getLabel());
			return localObject.equals(paramNode2.getLabel());
		}
	}

	public static class Label implements NodeMatcher {
		@Override
		public boolean match(Node paramNode1, Node paramNode2) {
			return paramNode1.getLabel().equals(paramNode2.getLabel());
		}
	}

	public static class IgnoreLabel implements NodeMatcher {
		@Override
		public boolean match(Node paramNode1, Node paramNode2) {
			return true;
		}
	}
}