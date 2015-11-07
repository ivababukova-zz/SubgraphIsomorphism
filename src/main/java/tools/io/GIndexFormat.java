/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package tools.io;

import graph.DefaultGraph;
import graph.DefaultNode;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class GIndexFormat implements GraphInput, GraphOutput {
	@Override
	public void writeHeader(Writer paramWriter, int paramInt)
			throws IOException {
		// XXX: empty
	}

	@Override
	public void write(Writer paramWriter, Graph paramGraph) throws IOException {
		Iterator<Node> localIterator = paramGraph.nodes().iterator();
		while (localIterator.hasNext()) {
			Node localObject = localIterator.next();
			paramWriter
					.write("v "
							+ localObject.getIndex()
							+ " "
							+ LabelConverter.labelToInt(localObject
									.getLabel()) + "\n");
		}
		Iterator<Edge> _localIterator = paramGraph.edges().iterator();
		while (localIterator.hasNext()) {
			Edge localObject = _localIterator.next();
			paramWriter
					.write("e "
							+ localObject.getFirstNode().getIndex()
							+ " "
							+ localObject.getSecondNode().getIndex()
							+ " "
							+ LabelConverter.labelToInt(localObject
									.getLabel()) + "\n");
		}
	}

	@Override
	public void write(Writer paramWriter, Graph paramGraph, String paramString)
			throws IOException {
		paramWriter.write("t # " + paramString + "\n");
		write(paramWriter, paramGraph);
	}

	@Override
	public void writeFooter(Writer paramWriter) throws IOException {
		paramWriter.close();
	}

	@Override
	public DefaultGraph read(BufferedReader paramBufferedReader)
			throws IOException {
		DefaultGraph localDefaultGraph = new DefaultGraph();
		String str;
		while ((str = paramBufferedReader.readLine()) != null) {
			String[] arrayOfString = str.split(" ");
			DefaultNode localDefaultNode1;
			if ("v".equals(arrayOfString[0])) {
				localDefaultNode1 = localDefaultGraph.addNode(arrayOfString[2]);
				if (localDefaultNode1.getIndex() != Integer.valueOf(
						arrayOfString[1]).intValue())
					throw new IOException("Unsupported node order.");
			} else if ("e".equals(arrayOfString[0])) {
				localDefaultNode1 = localDefaultGraph.getNode(Integer.valueOf(
						arrayOfString[1]).intValue());
				DefaultNode localDefaultNode2 = localDefaultGraph
						.getNode(Integer.valueOf(arrayOfString[2]).intValue());
				localDefaultGraph.addEdge(localDefaultNode1, localDefaultNode2,
						arrayOfString[3]);
			} else {
				if (!("t".equals(arrayOfString[0])))
					break;
				paramBufferedReader.reset();
				break;
			}
			paramBufferedReader.mark(512);
		}
		return localDefaultGraph;
	}

	@Override
	public Iterator<GraphInput.IDGraph> createGraphIterator(
			BufferedReader paramBufferedReader) {
		return new GraphInput.AbstractIteratingReader(paramBufferedReader) {

			@Override
			protected boolean loadNext() {
				try {
					String str1 = this.reader.readLine();
					if (str1 == null)
						return false;
					String str2;
					if (str1.startsWith("t # "))
						str2 = str1.substring(4);
					else
						throw new IOException("Missing id.");
					DefaultGraph localDefaultGraph = GIndexFormat.this
							.read(this.reader);
					this.next = new GraphInput.IDGraph(str2, localDefaultGraph);
				} catch (IOException localIOException) {
					return false;
				}
				return true;
			}
		};
	}
}