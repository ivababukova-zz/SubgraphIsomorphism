/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index;

import graph.Graph;
import index.features.Fingerprint;
import index.features.FingerprintBuilder;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FingerprintIndexFileRAM extends FingerprintIndexFile implements
		SimilaritySearchIndex {
	//XXX: public double avgUtilization;
	public List<IDFingerprint> fingerprints;

	public FingerprintIndexFileRAM(String paramString) {
		super(paramString);
	}

	public FingerprintIndexFileRAM(FingerprintBuilder paramFingerprintBuilder,
			String paramString) {
		super(paramFingerprintBuilder, paramString);
	}

	@Override
	public LinkedList<Integer> findCandidates(long[] paramArrayOfLong) {
		LinkedList<Integer> localLinkedList = new LinkedList<Integer>();
		Iterator<IDFingerprint> localIterator = this.fingerprints.iterator();
		while (localIterator.hasNext()) {
			IDFingerprint localIDFingerprint = localIterator
					.next();
			long[] arrayOfLong = localIDFingerprint.getFingerprint();
			int i = localIDFingerprint.getId();
			if (Fingerprint.conatins(arrayOfLong, paramArrayOfLong))
				localLinkedList.add(Integer.valueOf(i));
		}
		return localLinkedList;
	}

	@Override
	public String toString() {
		return "Fingerprint File, " + this.fpBuilder.toString();
	}

	@Override
	public void open() {
		super.open();
		this.fingerprints = new ArrayList<IDFingerprint>();
		try {
			this.indexFileOIS = createIndexOIS(true);
			while (true)
				try {
					long[] arrayOfLong = (long[]) this.indexFileOIS
							.readObject();
					int i = this.indexFileOIS.readInt();
					this.fingerprints.add(new IDFingerprint(arrayOfLong, i));
				} catch (EOFException localEOFException) {
					this.indexFileOIS.close();
					break;
				}
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new RuntimeException(localClassNotFoundException);
		}
	}

	@Override
	public List<Integer> rank(Graph paramGraph) {
		return null;
	}

	public class IDFingerprint {
		private long[] fingerprint;
		private int id;

		public IDFingerprint(long[] paramArrayOfLong, int paramInt) {
			this.fingerprint = paramArrayOfLong;
			this.id = paramInt;
		}

		public void setFingerprint(long[] paramArrayOfLong) {
			this.fingerprint = paramArrayOfLong;
		}

		public long[] getFingerprint() {
			return this.fingerprint;
		}

		public void setId(int paramInt) {
			this.id = paramInt;
		}

		public int getId() {
			return this.id;
		}
	}
}
