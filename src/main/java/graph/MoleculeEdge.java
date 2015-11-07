/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import org.openscience.cdk.interfaces.IBond;

public class MoleculeEdge extends DefaultEdge {
	IBond bond;

	protected MoleculeEdge(MoleculeNode paramMoleculeNode1,
			MoleculeNode paramMoleculeNode2, IBond paramIBond) {
		super(paramMoleculeNode1, paramMoleculeNode2, null);
		this.label = getLabel(paramIBond);
		this.bond = paramIBond;
	}

	public IBond getBond() {
		return this.bond;
	}

	private String getLabel(IBond paramIBond) {
		if (paramIBond.getStereo() == IBond.Stereo.E_OR_Z)
			return "*";
		if (paramIBond.getFlag(5))
			return ":";
		if (paramIBond.getOrder() == IBond.Order.SINGLE)
			return "-";
		if (paramIBond.getOrder() == IBond.Order.DOUBLE)
			return "=";
		if (paramIBond.getOrder() == IBond.Order.TRIPLE)
			return "#";
		throw new IllegalArgumentException("Unknown bond type");
	}
}