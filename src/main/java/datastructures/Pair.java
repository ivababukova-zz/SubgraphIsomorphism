/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package datastructures;

public class Pair<S, T> {
	private S firstElement;
	private T secondElement;

	public Pair() {
	}

	public Pair(S paramS, T paramT) {
		this.firstElement = paramS;
		this.secondElement = paramT;
	}

	public void setFirstElement(S paramS) {
		this.firstElement = paramS;
	}

	public S getFirstElement() {
		return this.firstElement;
	}

	public void setSecondElement(T paramT) {
		this.secondElement = paramT;
	}

	public T getSecondElement() {
		return this.secondElement;
	}

	@Override
	public boolean equals(Object paramObject) {
		if (paramObject instanceof Pair) {
			Pair<?, ?> localPair = (Pair<?, ?>) paramObject;
			return ((this.firstElement.equals(localPair.getFirstElement())) && (this.secondElement
					.equals(localPair.getSecondElement())));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.firstElement.hashCode() + this.secondElement.hashCode());
	}

	@Override
	public String toString() {
		return "(" + this.firstElement.toString() + ", "
				+ this.secondElement.toString() + ")";
	}
}