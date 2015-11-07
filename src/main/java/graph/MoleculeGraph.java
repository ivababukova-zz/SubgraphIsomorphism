/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import java.util.HashMap;
import java.util.Iterator;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class MoleculeGraph extends DefaultGraph {
	IAtomContainer atomContainer;
	HashMap<IAtom, MoleculeNode> atomToNode;

	public MoleculeGraph(IAtomContainer paramIAtomContainer) {
		this(paramIAtomContainer, false);
	}

	public MoleculeGraph(IAtomContainer paramIAtomContainer,
			boolean paramBoolean) {
		super(paramIAtomContainer.getAtomCount());
		if (paramBoolean)
			try {
				AtomContainerManipulator
						.percieveAtomTypesAndConfigureAtoms(paramIAtomContainer);
				CDKHueckelAromaticityDetector
						.detectAromaticity(paramIAtomContainer);
			} catch (CDKException localCDKException) {
				System.err.println(localCDKException.getMessage());
			}
		this.atomToNode = new HashMap<IAtom, MoleculeNode>();
		this.atomContainer = paramIAtomContainer;
		Iterator<?> localIterator = paramIAtomContainer.atoms().iterator();
		MoleculeNode localMoleculeNode1;
		while (localIterator.hasNext()) {
			IAtom localObject = (IAtom) localIterator.next();
			localMoleculeNode1 = new MoleculeNode(localObject,
					this.nodes.size());
			this.nodes.add(localMoleculeNode1);
			this.atomToNode.put(localObject, localMoleculeNode1);
		}
		localIterator = paramIAtomContainer.bonds().iterator();
		while (localIterator.hasNext()) {
			IBond localObject = (IBond) localIterator.next();
			assert (localObject.getAtomCount() == 2);
			localMoleculeNode1 = this.atomToNode
					.get(localObject.getAtom(0));
			MoleculeNode localMoleculeNode2 = this.atomToNode
					.get(localObject.getAtom(1));
			addEdge(localMoleculeNode1, localMoleculeNode2, localObject);
		}
	}

	@Override
	public MoleculeGraph clone() {
		try {
			AtomContainer localAtomContainer1 = (AtomContainer) getAtomContainer();
			AtomContainer localAtomContainer2 = (AtomContainer) localAtomContainer1
					.clone();
			MoleculeGraph localMoleculeGraph = new MoleculeGraph(
					localAtomContainer2);
			Iterator<?> localIterator = nodes().iterator();
			Object localObject;
			while (localIterator.hasNext()) {
				localObject = localIterator.next();
				localMoleculeGraph.getNode(((Node) localObject).getIndex())
						.setLabel(((Node) localObject).getLabel());
			}
			localIterator = edges().iterator();
			while (localIterator.hasNext()) {
				localObject = localIterator.next();
				Node localNode1 = ((Edge) localObject).getFirstNode();
				Node localNode2 = ((Edge) localObject).getSecondNode();
				DefaultEdge localDefaultEdge = localMoleculeGraph.getEdge(
						localMoleculeGraph.getNode(localNode1.getIndex()),
						localMoleculeGraph.getNode(localNode2.getIndex()));
				localDefaultEdge.setLabel(((Edge) localObject).getLabel());
			}
			return localMoleculeGraph;
		} catch (Exception localException) {
			throw new RuntimeException(localException);
		}
	}

	public IAtomContainer getAtomContainer() {
		return this.atomContainer;
	}

	public MoleculeNode getNode(IAtom paramIAtom) {
		return (this.atomToNode.get(paramIAtom));
	}

	public void addEdge(MoleculeNode paramMoleculeNode1,
			MoleculeNode paramMoleculeNode2, IBond paramIBond) {
		MoleculeEdge localMoleculeEdge = new MoleculeEdge(paramMoleculeNode1,
				paramMoleculeNode2, paramIBond);
		paramMoleculeNode1.addEdge(localMoleculeEdge);
		paramMoleculeNode2.addEdge(localMoleculeEdge);
		this.edgeCount += 1;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}