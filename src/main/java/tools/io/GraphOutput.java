/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package tools.io;

import graph.Graph;

import java.io.IOException;
import java.io.Writer;

public abstract interface GraphOutput {
	public abstract void write(Writer paramWriter, Graph paramGraph)
			throws IOException;

	public abstract void write(Writer paramWriter, Graph paramGraph,
			String paramString) throws IOException;

	public abstract void writeHeader(Writer paramWriter, int paramInt)
			throws IOException;

	public abstract void writeFooter(Writer paramWriter) throws IOException;
}