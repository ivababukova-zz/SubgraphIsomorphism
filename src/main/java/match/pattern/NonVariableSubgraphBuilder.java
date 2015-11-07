/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.DefaultGraph;
import graph.DefaultNode;
import graph.Edge;
import graph.Graph;
import graph.MoleculeEdge;
import graph.MoleculeGraph;
import graph.MoleculeNode;
import graph.Node;

import java.util.HashMap;
import java.util.Iterator;

import org.openscience.cdk.interfaces.IAtomContainer;

public class NonVariableSubgraphBuilder {
	public static Graph create(SearchPattern paramSearchPattern) {
		Graph localGraph = paramSearchPattern.getGraph();
		if (!(paramSearchPattern.getUseWildcards()))
			return localGraph;
		Object localObject5;
		if (localGraph instanceof MoleculeGraph) {
			MoleculeGraph localObject1 = ((MoleculeGraph) localGraph).clone();
			IAtomContainer localObject2 = localObject1.getAtomContainer();
			Iterator<Node> localIterator = localObject1.nodes().iterator();
			while (localIterator.hasNext()) {
				Node localObject3 = localIterator.next();
				if (localObject3.getLabel() instanceof GraphQueryLabel) {
					localObject2
							.removeAtom(((MoleculeNode) localObject3).getAtom());
					Iterator<Edge> localObject4 = localObject3.getEdges().iterator();
					while ((localObject4).hasNext()) {
						localObject5 = (localObject4).next();
						localObject2
								.removeBond(((MoleculeEdge) localObject5)
										.getBond());
					}
				}
			}
			Iterator<Edge> elocalIterator = localObject1.edges().iterator();
			while (elocalIterator.hasNext()) {
				Edge localObject3 = elocalIterator.next();
				if (localObject3.getLabel() instanceof GraphQueryLabel)
					localObject2
							.removeBond(((MoleculeEdge) localObject3).getBond());
			}
			return new MoleculeGraph(localObject2);
		}
		Graph localObject1 = new DefaultGraph();
		HashMap<Object, Object> localObject2 = new HashMap<Object, Object>();
		Iterator<Node> localIterator = localGraph.nodes().iterator();
		while (localIterator.hasNext()) {
			Node localObject3 = localIterator.next();
			if (!(localObject3.getLabel() instanceof GraphQueryLabel)) {
				Object localObject4 = ((DefaultGraph) localObject1)
						.addNode(localObject3.getLabel());
				(localObject2).put(localObject3, localObject4);
			}
		}
		Iterator<Edge> _localIterator = localGraph.edges().iterator();
		while (_localIterator.hasNext()) {
			Edge localObject3 = _localIterator.next();
			if (!(localObject3.getLabel() instanceof GraphQueryLabel)) {
				Object localObject4 = (localObject2).get(localObject3.getFirstNode());
				localObject5 = (localObject2)
						.get(localObject3.getSecondNode());
				if ((localObject4 != null) && (localObject5 != null))
					((DefaultGraph) localObject1).addEdge(
							(DefaultNode) localObject4,
							(DefaultNode) localObject5,
							localObject3.getLabel());
			}
		}
		return (localObject1);
	}
}