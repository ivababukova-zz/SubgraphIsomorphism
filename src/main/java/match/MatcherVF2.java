/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Graph;
import graph.Node;

import java.util.LinkedList;

import match.pattern.SearchPattern;
import match.pattern.SearchPlan;

public class MatcherVF2 extends Matcher {
	PartialMappingTerminal pmt;

	public MatcherVF2(Graph paramGraph1, Graph paramGraph2) {
		super(paramGraph1, paramGraph2);
		this.pmt = new PartialMappingTerminal(this.searchPattern, this.host);
	}

	public MatcherVF2(SearchPattern paramSearchPattern, Graph paramGraph) {
		super(paramSearchPattern, paramGraph);
		this.pmt = new PartialMappingTerminal(paramSearchPattern, this.host);
	}

	@Override
	public LinkedList<Pair> getMatch() {
		if (!(this.pmt.isComplete()))
			return null;
		return this.pmt.getMapping();
	}

	private Node nextPatternCandidate() {
		SearchPlan.Extension localExtension = this.searchPlan
				.getExtension(this.pmt.getSize());
		return localExtension.getNode();
	}

	@Override
	public boolean match() {
		if (this.pmt.isComplete())
			return handleMatch(this.pmt);
		Node localNode1 = nextPatternCandidate();
		for (Node localNode2 = this.pmt.nextTerminalHostNode(localNode1, null); localNode2 != null; localNode2 = this.pmt
				.nextTerminalHostNode(localNode1, localNode2)) {
			if (!(this.pmt.isFeasibleCandidate(localNode1, localNode2)))
				continue;
			this.pmt.extend(localNode1, localNode2);
			if (match())
				return true;
			this.pmt.remove(localNode1, localNode2);
		}
		return false;
	}
}