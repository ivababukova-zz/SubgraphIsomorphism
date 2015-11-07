/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package tools.io;

import graph.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public abstract interface GraphInput {
	public abstract Graph read(BufferedReader paramBufferedReader)
			throws IOException;

	public abstract Iterator<IDGraph> createGraphIterator(
			BufferedReader paramBufferedReader);

	public static class IDGraph {
		String id;
		Graph graph;

		public IDGraph(String paramString, Graph paramGraph) {
			this.id = paramString;
			this.graph = paramGraph;
		}

		public String getID() {
			return this.id;
		}

		public Graph getGraph() {
			return this.graph;
		}

		public void setID(String paramString) {
			this.id = paramString;
		}

		public void setGraph(Graph paramGraph) {
			this.graph = paramGraph;
		}
	}

	public static abstract class AbstractIteratingReader implements
			Iterator<GraphInput.IDGraph> {
		BufferedReader reader;
		public AbstractIteratingReader(BufferedReader reader) {
			this.reader = reader;
		}
		protected GraphInput.IDGraph next;

		@Override
		public boolean hasNext() {
			return ((this.next != null) || (loadNext()));
		}

		@Override
		public GraphInput.IDGraph next() {
			GraphInput.IDGraph localIDGraph = this.next;
			this.next = null;
			return localIDGraph;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		protected abstract boolean loadNext();
	}
}