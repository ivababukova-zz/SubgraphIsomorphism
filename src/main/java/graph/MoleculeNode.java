/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package graph;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IPseudoAtom;

public class MoleculeNode extends DefaultNode {
	IAtom atom;

	public MoleculeNode(IAtom paramIAtom, int paramInt) {
		super((paramIAtom instanceof IPseudoAtom) ? ((IPseudoAtom) paramIAtom)
				.getLabel() : paramIAtom.getSymbol(), paramInt);
		this.atom = paramIAtom;
	}

	public IAtom getAtom() {
		return this.atom;
	}
}