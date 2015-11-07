/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package match.pattern;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public abstract interface GraphQueryLabel {
	public abstract boolean matches(Object paramObject);

	public static class NotList extends GraphQueryLabel.AbstractList {
		public NotList() {
		}

		public NotList(Collection<Object> paramCollection) {
			super(paramCollection);
		}

		public NotList(Object[] paramArrayOfObject) {
			super(paramArrayOfObject);
		}

		@Override
		public boolean matches(Object paramObject) {
			return (!(this.labelList.contains(paramObject)));
		}

		@Override
		public String toString() {
			return "!" + super.toString();
		}
	}

	public static class List extends GraphQueryLabel.AbstractList {
		public List() {
		}

		public List(Collection<Object> paramCollection) {
			super(paramCollection);
		}

		public List(Object[] paramArrayOfObject) {
			super(paramArrayOfObject);
		}

		@Override
		public boolean matches(Object paramObject) {
			return this.labelList.contains(paramObject);
		}
	}

	public static abstract class AbstractList implements GraphQueryLabel {
		HashSet<Object> labelList;

		public AbstractList() {
			this.labelList = new HashSet<Object>();
		}

		public AbstractList(Collection<Object> paramCollection) {
			setLabelList(paramCollection);
		}

		public AbstractList(Object[] paramArrayOfObject) {
			setLabelList(paramArrayOfObject);
		}

		public void setLabelList(Collection<Object> paramCollection) {
			this.labelList.clear();
			this.labelList.addAll(paramCollection);
		}

		public void setLabelList(Object[] paramArrayOfObject) {
			this.labelList.clear();
			for (Object localObject : paramArrayOfObject)
				this.labelList.add(localObject);
		}

		public HashSet<Object> getLabelList() {
			return this.labelList;
		}

		@Override
		public abstract boolean matches(Object paramObject);

		@Override
		public String toString() {
			StringBuilder localStringBuilder = new StringBuilder();
			localStringBuilder.append('[');
			if (!(this.labelList.isEmpty())) {
				Iterator<Object> localIterator = this.labelList.iterator();
				localStringBuilder.append(localIterator.next());
				while (localIterator.hasNext()) {
					localStringBuilder.append(",");
					localStringBuilder.append(localIterator.next());
				}
			}
			localStringBuilder.append(']');
			return localStringBuilder.toString();
		}
	}

	public static class Wildcard implements GraphQueryLabel {
		public static final String WILDCARD_LABEL = "*";

		@Override
		public boolean matches(Object paramObject) {
			return true;
		}

		@Override
		public String toString() {
			return "*";
		}
	}
}