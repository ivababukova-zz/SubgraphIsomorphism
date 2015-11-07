/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Graph;
import graph.Node;

import java.util.LinkedList;

import match.pattern.SearchPattern;
import match.pattern.SearchPlan;

public class MatcherSimple extends Matcher {
	PartialMapping pm;

	public MatcherSimple(Graph paramGraph1, Graph paramGraph2) {
		super(paramGraph1, paramGraph2);
		this.pm = new PartialMapping(this.searchPattern, this.host);
	}

	public MatcherSimple(SearchPattern paramSearchPattern, Graph paramGraph) {
		super(paramSearchPattern, paramGraph);
		this.pm = new PartialMapping(paramSearchPattern, this.host);
	}

	@Override
	public LinkedList<Pair> getMatch() {
		if (!(this.pm.isComplete()))
			return null;
		return this.pm.getMapping();
	}

	private boolean hasNextHostCandidate(Node paramNode) {
		if (paramNode == null)
			return true;
		return (paramNode.getIndex() + 1 < this.host.getNodeCount());
	}

	private Node nextHostCandidate(Node paramNode) {
		if (paramNode == null)
			return this.host.getNode(0);
		int i = paramNode.getIndex() + 1;
		return this.host.getNode(i);
	}

	private Node nextPatternCandidate() {
		SearchPlan.Extension localExtension = this.searchPlan
				.getExtension(this.pm.getSize());
		return localExtension.getNode();
	}

	@Override
	public boolean match() {
		if (this.pm.isComplete())
			return handleMatch(this.pm);
		Node localNode1 = nextPatternCandidate();
		Node localNode2 = null;
		while (hasNextHostCandidate(localNode2)) {
			localNode2 = nextHostCandidate(localNode2);
			if ((!(this.pm.isFeasibleNodePair(localNode1, localNode2)))
					|| (!(this.pm.isFeasibleConnectedPair(localNode1,
							localNode2, localNode1.getEdges()))))
				continue;
			this.pm.extend(localNode1, localNode2);
			if (match())
				return true;
			this.pm.remove(localNode1, localNode2);
		}
		return false;
	}
}