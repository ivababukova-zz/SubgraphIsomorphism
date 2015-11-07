/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Edge;
import graph.Graph;
import graph.MoleculeGraph;
import graph.Node;

import java.util.Iterator;

public class SearchPattern {
	private Graph graph;
	private NodeMatcher nodeMatcher;
	private EdgeMatcher edgeMatcher;
	private SearchPlan searchPlan;
	private boolean useWildcards;

	public SearchPattern(Graph paramGraph, boolean paramBoolean, int paramInt) {
		this.graph = paramGraph;
		configure(paramBoolean);
		this.searchPlan = new SearchPlan(paramGraph, paramInt);
	}

	public SearchPattern(Graph paramGraph, boolean paramBoolean) {
		this(paramGraph, paramBoolean,
				(paramGraph instanceof MoleculeGraph) ? 2 : 0);
	}

	private void configure(boolean paramBoolean) {
		if ((paramBoolean) && (compileQueryLabel())) {
			this.nodeMatcher = new NodeMatcher.Wildcards();
			this.edgeMatcher = new EdgeMatcher.Wildcards();
			this.useWildcards = true;
		} else {
			this.nodeMatcher = new NodeMatcher.Label();
			this.edgeMatcher = new EdgeMatcher.Label();
			this.useWildcards = false;
		}
	}

	private boolean compileQueryLabel() {
		boolean i = false;
		Iterator<Node> localIterator = this.graph.nodes().iterator();
		String str;
		while (localIterator.hasNext()) {
			Node localObject1 = localIterator.next();
			if (localObject1.getLabel() instanceof String) {
				str = (String) localObject1.getLabel();
				if ((str.equals("*")) || (str.equals("R"))) {
					localObject1
							.setLabel(new GraphQueryLabel.Wildcard());
					i = true;
				} else if (str.contains("[")) {
					Object localObject2;
					if (str.startsWith("!")) {
						localObject2 = new GraphQueryLabel.NotList();
						str = str.substring(1);
					} else {
						localObject2 = new GraphQueryLabel.List();
					}
					str = str.substring(1, str.length() - 1);
					String[] arrayOfString = str.split(",");
					((GraphQueryLabel.AbstractList) localObject2)
							.setLabelList(arrayOfString);
					localObject1.setLabel(localObject2);
					i = true;
				}
			} else if (localObject1.getLabel() instanceof GraphQueryLabel) {
				i = true;
			}
		}
		Iterator<Edge> elocalIterator = this.graph.edges().iterator();
		while (elocalIterator.hasNext()) {
			Edge localObject1 = elocalIterator.next();
			if (localObject1.getLabel() instanceof String) {
				str = (String) localObject1.getLabel();
				if (str.equals("*")) {
					localObject1
							.setLabel(new GraphQueryLabel.Wildcard());
					i = true;
				}
			} else if (localObject1.getLabel() instanceof GraphQueryLabel) {
				i = true;
			}
		}
		return i;
	}

	public Graph getGraph() {
		return this.graph;
	}

	public NodeMatcher getNodeMatcher() {
		return this.nodeMatcher;
	}

	public EdgeMatcher getEdgeMatcher() {
		return this.edgeMatcher;
	}

	public SearchPlan getSearchPlan() {
		return this.searchPlan;
	}

	public boolean getUseWildcards() {
		return this.useWildcards;
	}
}