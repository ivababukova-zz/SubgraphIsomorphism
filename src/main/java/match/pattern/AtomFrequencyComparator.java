/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import graph.Graph;
import graph.Node;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import tools.io.LabelConverter;

public class AtomFrequencyComparator implements Comparator<Node> {
	private static HashMap<Object, Integer> atomSymbolFrequency;
	public static int totalAtomCount;
	private int[] priorities;

	public static HashMap<Object, Integer> createLabelFrequencyMap() {
		HashMap<Object, Integer> localHashMap = new HashMap<Object, Integer>();
		localHashMap.put("H", Integer.valueOf(4238982));
		localHashMap.put("C", Integer.valueOf(3834810));
		localHashMap.put("O", Integer.valueOf(610408));
		localHashMap.put("N", Integer.valueOf(489704));
		localHashMap.put("S", Integer.valueOf(87881));
		localHashMap.put("F", Integer.valueOf(60154));
		localHashMap.put("Cl", Integer.valueOf(49092));
		localHashMap.put("Br", Integer.valueOf(13355));
		localHashMap.put("Si", Integer.valueOf(3138));
		localHashMap.put("P", Integer.valueOf(3063));
		localHashMap.put("I", Integer.valueOf(2242));
		localHashMap.put("B", Integer.valueOf(545));
		localHashMap.put("R", Integer.valueOf(345));
		localHashMap.put("Se", Integer.valueOf(225));
		localHashMap.put("Sn", Integer.valueOf(147));
		localHashMap.put("Al", Integer.valueOf(42));
		localHashMap.put("As", Integer.valueOf(30));
		localHashMap.put("Hg", Integer.valueOf(30));
		localHashMap.put("Te", Integer.valueOf(29));
		localHashMap.put("Ge", Integer.valueOf(24));
		localHashMap.put("Sb", Integer.valueOf(13));
		localHashMap.put("Pb", Integer.valueOf(10));
		localHashMap.put("Cr", Integer.valueOf(8));
		localHashMap.put("Bi", Integer.valueOf(8));
		localHashMap.put("W", Integer.valueOf(7));
		localHashMap.put("Ru", Integer.valueOf(4));
		localHashMap.put("Ti", Integer.valueOf(4));
		localHashMap.put("Mo", Integer.valueOf(3));
		localHashMap.put("Ga", Integer.valueOf(3));
		localHashMap.put("Tl", Integer.valueOf(3));
		localHashMap.put("In", Integer.valueOf(2));
		localHashMap.put("Zr", Integer.valueOf(2));
		localHashMap.put("V", Integer.valueOf(1));
		localHashMap.put("Hf", Integer.valueOf(1));
		localHashMap.put("Rh", Integer.valueOf(1));
		localHashMap.put("Tc", Integer.valueOf(1));
		ArrayList<Object> localArrayList = new ArrayList<Object>(localHashMap.keySet());
		Iterator<Object> localIterator = localArrayList.iterator();
		while (localIterator.hasNext()) {
			Object localObject = localIterator.next();
			localHashMap.put(
					String.valueOf(LabelConverter.labelToInt(localObject)),
					localHashMap.get(localObject));
		}
		return localHashMap;
	}

	public AtomFrequencyComparator(Graph paramGraph) {
		this.priorities = new int[paramGraph.getNodeCount()];
		Iterator<?> localIterator = paramGraph.nodes().iterator();
		while (localIterator.hasNext()) {
			Node localNode = (Node) localIterator.next();
			this.priorities[localNode.getIndex()] = getFrequency(localNode);
		}
	}

	@Override
	public int compare(Node paramNode1, Node paramNode2) {
		int i = this.priorities[paramNode1.getIndex()];
		int j = this.priorities[paramNode2.getIndex()];
		if (i < j)
			return -1;
		if (i > j)
			return 1;
		return 0;
	}

	public int getFrequency(Node paramNode) {
		Integer localInteger = Integer.valueOf(0);
		Object localObject1 = paramNode.getLabel();
		if (localObject1 instanceof GraphQueryLabel)
			if (localObject1 instanceof GraphQueryLabel.Wildcard) {
				localInteger = Integer.valueOf(totalAtomCount);
			} else {
				Object localObject2;
				Iterator<?> localIterator;
				Object localObject3;
				if (localObject1 instanceof GraphQueryLabel.List) {
					localInteger = Integer.valueOf(0);
					localObject2 = localObject1;
					localIterator = ((GraphQueryLabel.List) localObject2)
							.getLabelList().iterator();
					while (localIterator.hasNext()) {
						localObject3 = localIterator.next();
						localInteger = Integer.valueOf(localInteger.intValue()
								+ getFrequency(localObject3));
					}
				} else if (localObject1 instanceof GraphQueryLabel.NotList) {
					localInteger = Integer.valueOf(totalAtomCount);
					localObject2 = localObject1;
					localIterator = ((GraphQueryLabel.NotList) localObject2)
							.getLabelList().iterator();
					while (localIterator.hasNext()) {
						localObject3 = localIterator.next();
						localInteger = Integer.valueOf(localInteger.intValue()
								- getFrequency(localObject3));
					}
				}
			}
		else
			localInteger = Integer.valueOf(getFrequency(localObject1));
		return localInteger.intValue();
	}

	private int getFrequency(Object paramObject) {
		Integer localInteger = atomSymbolFrequency.get(paramObject);
		if (localInteger == null)
			localInteger = Integer.valueOf(0);
		return localInteger.intValue();
	}

	public static void setAtomSymbolFrequency(
			HashMap<Object, Integer> paramHashMap) {
		atomSymbolFrequency = paramHashMap;
		totalAtomCount = 0;
		Iterator<Integer> localIterator = paramHashMap.values().iterator();
		while (localIterator.hasNext()) {
			int i = localIterator.next().intValue();
			totalAtomCount += i;
		}
	}
}