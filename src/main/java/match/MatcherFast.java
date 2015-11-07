/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.Iterator;
import java.util.List;

import match.pattern.SearchPattern;
import match.pattern.SearchPlan;

public class MatcherFast extends Matcher {
	PartialMapping pm;

	public MatcherFast(Graph paramGraph1, Graph paramGraph2) {
		super(paramGraph1, paramGraph2);
		this.pm = new PartialMapping(this.searchPattern, this.host);
	}

	public MatcherFast(SearchPattern paramSearchPattern, Graph paramGraph) {
		super(paramSearchPattern, paramGraph);
		this.pm = new PartialMapping(paramSearchPattern, this.host);
	}

	@Override
	public List<Pair> getMatch() {
		if (!(this.pm.isComplete()))
			return null;
		return this.pm.getMapping();
	}

	private Node getJoinHostNode(SearchPlan.Extension paramExtension,
			Node paramNode) {
		Edge localEdge = paramExtension.getJoinEdges().getFirst();
		Node localNode = this.pm.mapToHostNode(localEdge
				.getOppositeNode(paramNode));
		return localNode;
	}

	@Override
	public boolean match() {
		if (this.pm.isComplete())
			return handleMatch(this.pm);
		SearchPlan.Extension localExtension = this.searchPlan
				.getExtension(this.pm.getSize());
		Node localNode1 = localExtension.getNode();
		Object localObject1;
		Object localObject2;
		if (!(localExtension.hasJoinEdges())) {
			localObject1 = this.host.nodes().iterator();
			while (((Iterator<?>) localObject1).hasNext()) {
				localObject2 = ((Iterator<?>) localObject1).next();
				if (this.pm.isFeasibleNodePair(localNode1, (Node) localObject2)) {
					this.pm.extend(localNode1, (Node) localObject2);
					if (match())
						return true;
					this.pm.remove(localNode1, (Node) localObject2);
				}
			}
		} else {
			localObject1 = getJoinHostNode(localExtension, localNode1);
			localObject2 = ((Node) localObject1).getEdges().iterator();
			while (((Iterator<?>) localObject2).hasNext()) {
				Edge localEdge = (Edge) ((Iterator<?>) localObject2).next();
				Node localNode2 = localEdge
						.getOppositeNode((Node) localObject1);
				if ((this.pm.isFeasibleNodePair(localNode1, localNode2))
						&& (this.pm.isFeasibleConnectedPair(localNode1,
								localNode2, localExtension.getJoinEdges()))) {
					this.pm.extend(localNode1, localNode2);
					if (match())
						return true;
					this.pm.remove(localNode1, localNode2);
				}
			}
		}
		return false;
	}
}