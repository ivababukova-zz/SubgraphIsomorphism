/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index;

import graph.Graph;
import index.features.Fingerprint;
import index.features.FingerprintBuilder;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

public class FingerprintIndexFile extends FileIndex {
	private int graphCount = 0;
	public double avgUtilization = 0;
	public double maxUtilization = 0.0D;

	public FingerprintIndexFile(String paramString) {
		super(null, paramString);
	}

	public FingerprintIndexFile(FingerprintBuilder paramFingerprintBuilder,
			String paramString) {
		super(paramFingerprintBuilder, paramString);
	}

	@Override
	public void addGraph(int paramInt, Graph paramGraph) {
		try {
			Fingerprint localFingerprint = this.fpBuilder
					.getFingerprint(paramGraph);
			long[] longArray = localFingerprint.toLongArray();
			this.indexFileOOS.writeObject(longArray);
			this.indexFileOOS.writeInt(paramInt);
			this.graphCount += 1;
			this.avgUtilization = ((this.graphCount - 1.0D) / this.graphCount
					* this.avgUtilization + 1.0D / this.graphCount
					* localFingerprint.getUtilization());
			if (localFingerprint.getUtilization() > this.maxUtilization)
				this.maxUtilization = localFingerprint.getUtilization();
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
	}

	@Override
	public LinkedList<Integer> findCandidates(Graph paramGraph) {
		Fingerprint localFingerprint = this.fpBuilder
				.getFingerprint(paramGraph);
		return findCandidates(localFingerprint.toLongArray());
	}

	public LinkedList<Integer> findCandidates(long al[]) {
		LinkedList<Integer> linkedlist = new LinkedList<Integer>();
		try {
			this.indexFileOIS = createIndexOIS(true);
			try {
				do {
					long al1[];
					int i;
					do {
						al1 = (long[]) this.indexFileOIS.readObject();
						i = this.indexFileOIS.readInt();
					} while (!Fingerprint.conatins(al1, al));
					linkedlist.add(Integer.valueOf(i));
				} while (true);
			} catch (EOFException eofexception) {
				this.indexFileOIS.close();
			}
		} catch (IOException ioexception) {
			throw new RuntimeException(ioexception);
		} catch (ClassNotFoundException classnotfoundexception) {
			throw new RuntimeException(classnotfoundexception);
		}
		return linkedlist;
	}

	@Override
	public String toString() {
		return "Fingerprint File, " + this.fpBuilder.toString();
	}

	@Override
	public String getStatistics() {
		return "Avg. Utilization: \t" + this.avgUtilization + "\n"
				+ "Max. Utilization: \t" + this.maxUtilization;
	}

	protected ObjectInputStream createIndexOIS(boolean paramBoolean)
			throws IOException {
		ObjectInputStream localObjectInputStream = super.createIndexOIS();
		if (paramBoolean) {
			localObjectInputStream.readInt();
			localObjectInputStream.readInt();
			localObjectInputStream.readInt();
			localObjectInputStream.readInt();
		}
		return localObjectInputStream;
	}

	protected void loadSettings() {
		try {
			int i = this.indexFileOIS.readInt();
			int j = Integer.valueOf(this.indexFileOIS.readInt()).intValue();
			int k = Integer.valueOf(this.indexFileOIS.readInt()).intValue();
			int l = Integer.valueOf(this.indexFileOIS.readInt()).intValue();
			this.fpBuilder = new FingerprintBuilder(i, j, k, l);
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
	}

	@Override
	public void open() {
		try {
			this.indexFileOIS = createIndexOIS(false);
			loadSettings();
			this.indexFileOIS.close();
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
	}

	@Override
	public void create() {
		try {
			this.indexFileOOS = createIndexOOS(false);
			this.indexFileOOS.writeInt(this.fpBuilder.getFingerprintSize());
			this.indexFileOOS.writeInt(this.fpBuilder.getPathSize());
			this.indexFileOOS.writeInt(this.fpBuilder.getSubtreeSize());
			this.indexFileOOS.writeInt(this.fpBuilder.getRingSize());
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
	}
}
