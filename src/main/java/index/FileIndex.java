/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index;

import graph.Graph;
import index.features.FingerprintBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public abstract class FileIndex extends Index {
	protected File indexFile;
	protected ObjectOutputStream indexFileOOS;
	protected ObjectInputStream indexFileOIS;

	public FileIndex(FingerprintBuilder paramFingerprintBuilder,
			String paramString) {
		super(paramFingerprintBuilder);
		this.indexFile = new File(paramString);
	}

	protected ObjectInputStream createIndexOIS() throws IOException {
		FileInputStream localFileInputStream = new FileInputStream(
				this.indexFile);
		BufferedInputStream localBufferedInputStream = new BufferedInputStream(
				localFileInputStream);
		ObjectInputStream localObjectInputStream = new ObjectInputStream(
				localBufferedInputStream);
		return localObjectInputStream;
	}

	protected ObjectOutputStream createIndexOOS(boolean paramBoolean)
			throws IOException {
		FileOutputStream localFileOutputStream = new FileOutputStream(
				this.indexFile, paramBoolean);
		BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(
				localFileOutputStream);
		ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(
				localBufferedOutputStream);
		return localObjectOutputStream;
	}

	@Override
	public void open() {
		try {
			this.indexFileOIS = createIndexOIS();
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
	}

	@Override
	public void close() {
		try {
			if (this.indexFileOOS != null) {
				this.indexFileOOS.flush();
				this.indexFileOOS.close();
			}
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
	}

	@Override
	public void create() {
		// XXX: Empty
	}

	@Override
	public void clear() {
		this.indexFile.delete();
	}

	@Override
	public abstract void addGraph(int paramInt, Graph paramGraph);

	@Override
	public abstract List<Integer> findCandidates(Graph paramGraph);
}