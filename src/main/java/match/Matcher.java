/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Graph;

import java.util.List;

import match.pattern.SearchPattern;
import match.pattern.SearchPlan;

public abstract class Matcher {
	public static final boolean DEBUG = false;
	protected SearchPattern searchPattern;
	protected SearchPlan searchPlan;
	protected Graph host;
	protected MatchDelegate matchDelegate;

	public Matcher(SearchPattern paramSearchPattern, Graph paramGraph) {
		this.searchPattern = paramSearchPattern;
		this.searchPlan = paramSearchPattern.getSearchPlan();
		this.host = paramGraph;
	}

	public Matcher(Graph paramGraph1, Graph paramGraph2) {
		this(new SearchPattern(paramGraph1, false), paramGraph2);
	}

	public void setMatchDelegate(MatchDelegate paramMatchDelegate) {
		this.matchDelegate = paramMatchDelegate;
	}

	protected boolean handleMatch(PartialMapping paramPartialMapping) {
		if (this.matchDelegate != null)
			return this.matchDelegate.handleMatch(paramPartialMapping);
		return true;
	}

	public abstract List<Pair> getMatch();

	public abstract boolean match();
}