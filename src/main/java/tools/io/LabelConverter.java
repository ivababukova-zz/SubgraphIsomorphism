/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package tools.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class LabelConverter {
	private static LabelConverter instance = new LabelConverter();
	private HashMap<Object, Integer> convMap = new HashMap<Object, Integer>();
	private int lastNumber;
	private static HashMap<Object, Integer> o2i = new HashMap<Object, Integer>();
	//private static HashMap<Integer, Object> i2o = new HashMap<Integer, Object>();

	private LabelConverter() {
		this.convMap.put("-", Integer.valueOf(2));
		this.convMap.put("=", Integer.valueOf(3));
		this.convMap.put("#", Integer.valueOf(4));
		this.convMap.put("C", Integer.valueOf(5));
		this.convMap.put("H", Integer.valueOf(6));
		this.convMap.put("N", Integer.valueOf(7));
		this.convMap.put("O", Integer.valueOf(8));
		this.convMap.put("Cl", Integer.valueOf(9));
		this.convMap.put("F", Integer.valueOf(10));
		this.convMap.put("S", Integer.valueOf(11));
		this.convMap.put("Br", Integer.valueOf(12));
		this.convMap.put("I", Integer.valueOf(13));
		this.convMap.put("Cu", Integer.valueOf(14));
		this.convMap.put("P", Integer.valueOf(15));
		this.convMap.put("Zn", Integer.valueOf(16));
		this.convMap.put("B", Integer.valueOf(17));
		this.convMap.put("Co", Integer.valueOf(18));
		this.convMap.put("Mn", Integer.valueOf(19));
		this.convMap.put("As", Integer.valueOf(20));
		this.convMap.put("Al", Integer.valueOf(21));
		this.convMap.put("Ni", Integer.valueOf(22));
		this.convMap.put("Se", Integer.valueOf(23));
		this.convMap.put("Si", Integer.valueOf(24));
		this.convMap.put("V", Integer.valueOf(25));
		this.convMap.put("Zr", Integer.valueOf(26));
		this.convMap.put("Sn", Integer.valueOf(27));
		this.convMap.put("Li", Integer.valueOf(28));
		this.convMap.put("Sb", Integer.valueOf(29));
		this.convMap.put("Fe", Integer.valueOf(30));
		this.convMap.put("Bi", Integer.valueOf(31));
		this.convMap.put("Pd", Integer.valueOf(32));
		this.convMap.put("Hg", Integer.valueOf(33));
		this.convMap.put("Na", Integer.valueOf(34));
		this.convMap.put("Ca", Integer.valueOf(35));
		this.convMap.put("Ti", Integer.valueOf(36));
		this.convMap.put("Ho", Integer.valueOf(37));
		this.convMap.put("Ge", Integer.valueOf(38));
		this.convMap.put("Pt", Integer.valueOf(39));
		this.convMap.put("Ru", Integer.valueOf(40));
		this.convMap.put("Rh", Integer.valueOf(41));
		this.convMap.put("Cr", Integer.valueOf(42));
		this.convMap.put("Ga", Integer.valueOf(43));
		this.convMap.put("K", Integer.valueOf(44));
		this.convMap.put("Ag", Integer.valueOf(45));
		this.convMap.put("Au", Integer.valueOf(46));
		this.convMap.put("Tb", Integer.valueOf(47));
		this.convMap.put("Ir", Integer.valueOf(48));
		this.convMap.put("Te", Integer.valueOf(49));
		this.convMap.put("Mg", Integer.valueOf(50));
		this.convMap.put("Pb", Integer.valueOf(51));
		this.convMap.put("W", Integer.valueOf(52));
		this.convMap.put("Cs", Integer.valueOf(53));
		this.convMap.put("Mo", Integer.valueOf(54));
		this.convMap.put("Re", Integer.valueOf(55));
		this.convMap.put("Cd", Integer.valueOf(56));
		this.convMap.put("Os", Integer.valueOf(57));
		this.convMap.put("Pr", Integer.valueOf(58));
		this.convMap.put("Nd", Integer.valueOf(59));
		this.convMap.put("Sm", Integer.valueOf(60));
		this.convMap.put("Gd", Integer.valueOf(61));
		this.convMap.put("Yb", Integer.valueOf(62));
		this.convMap.put("Er", Integer.valueOf(63));
		this.convMap.put("U", Integer.valueOf(64));
		this.convMap.put("Tl", Integer.valueOf(65));
		this.convMap.put("Nb", Integer.valueOf(66));
		this.convMap.put("Ac", Integer.valueOf(67));
		this.lastNumber = 67;
	}

	public static int calcInt(Object paramObject) {
		Integer localInteger = o2i.get(paramObject);
		if (localInteger == null) {
			localInteger = Integer.valueOf(++instance.lastNumber);
			instance.convMap.put(paramObject, localInteger);
		}
		return localInteger.intValue();
	}

	public static int labelToInt(Object paramObject) {
		Integer localInteger1 = null;
		if (paramObject instanceof Integer)
			localInteger1 = (Integer) paramObject;
		else if (paramObject instanceof String)
			try {
				localInteger1 = Integer.valueOf((String) paramObject);
			} catch (NumberFormatException localNumberFormatException) {
				// XXX: empty
			}
		if (localInteger1 != null)
			return localInteger1.intValue();
		Integer localInteger2 = instance.convMap.get(paramObject);
		if (localInteger2 == null)
			localInteger2 = Integer.valueOf(calcInt(paramObject));
		return localInteger2.intValue();
	}

	public static void printMap() {
		HashMap<Integer, Object> localHashMap = new HashMap<Integer, Object>();
		Iterator<Entry<Object, Integer>> _localObject1 = instance.convMap.entrySet().iterator();
		while (_localObject1.hasNext()) {
			Entry<Object, Integer> localObject2 = _localObject1.next();
			localHashMap.put(localObject2.getValue(),
					localObject2.getKey());
		}
		ArrayList<Integer> localObject1 = new ArrayList<Integer>(localHashMap.keySet());
		Collections.sort(localObject1);
		Object localObject2 = ((ArrayList<?>) localObject1).iterator();
		while (((Iterator<?>) localObject2).hasNext()) {
			Integer localInteger = (Integer) ((Iterator<?>) localObject2).next();
			System.out.println(localHashMap.get(localInteger) + "\t"
					+ localInteger);
		}
	}
}