/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.features;

import java.util.BitSet;

public class Fingerprint extends BitSet {
	private static final long serialVersionUID = 1L;
	private static char[] hex;
	private int preferredSize;

	public Fingerprint() {
	}

	public Fingerprint(int paramInt) {
		super(paramInt);
		this.preferredSize = paramInt;
	}

	public Fingerprint(long[] paramArrayOfLong) {
		this(paramArrayOfLong.length * 64);
		setTo(paramArrayOfLong);
	}

	public Fingerprint(BitSet paramBitSet) {
		this(paramBitSet.size());
		setTo(paramBitSet);
	}

	public Fingerprint(String paramString) {
		this(paramString.length() * 4);
		setToHexString(paramString);
	}

	public void setTo(BitSet paramBitSet) {
		for (int i = 0; i < paramBitSet.size(); ++i)
			set(i, paramBitSet.get(i));
	}

	public void setTo(long[] paramArrayOfLong) {
		for (int i = 0; i < paramArrayOfLong.length; ++i)
			for (int j = 0; j < 64; ++j) {
				if ((1L << j & paramArrayOfLong[i]) == 0L)
					continue;
				set(i * 64 + j);
			}
	}

	public boolean contains(Fingerprint paramFingerprint) {
		Fingerprint localFingerprint = (Fingerprint) paramFingerprint.clone();
		localFingerprint.and(this);
		return (localFingerprint.equals(paramFingerprint));
	}

	public long[] toLongArray() {
		int i = getLongCount(size());
		long[] arrayOfLong = new long[i];
		for (int j = 0; j < i; ++j)
			for (int k = 0; k < 64; ++k) {
				if (!(get(j * 64 + k)))
					continue;
				arrayOfLong[j] |= 1L << k;
			}
		return arrayOfLong;
	}

	public byte[] toByteArray() {
		int i = getByteCount(Math.max(this.preferredSize, length()));
		byte[] arrayOfByte = new byte[i];
		for (int j = 0; j < i; ++j)
			for (int k = 0; k < 8; ++k) {
				if (!(get(j * 8 + k)))
					continue;
				int tmp52_51 = j;
				byte[] tmp52_50 = arrayOfByte;
				tmp52_50[tmp52_51] = (byte) (tmp52_50[tmp52_51] | 1 << k);
			}
		return arrayOfByte;
	}

	public double getUtilization() {
		return (cardinality() / (double)size());
	}

	public String toBinString() {
		StringBuilder localStringBuilder = new StringBuilder();
		int i = Math.max(this.preferredSize, length());
		for (int j = 0; j < i; ++j)
			if (get(j))
				localStringBuilder.append('1');
			else
				localStringBuilder.append('0');
		return localStringBuilder.toString();
	}

	private static int getVarCount(int paramInt1, int paramInt2) {
		int i = paramInt1 / paramInt2;
		if (paramInt1 % paramInt2 > 0)
			++i;
		return i;
	}

	public static int getLongCount(int paramInt) {
		return getVarCount(paramInt, 64);
	}

	public static int getByteCount(int paramInt) {
		return getVarCount(paramInt, 8);
	}

	public static boolean conatins(long[] paramArrayOfLong1,
			long[] paramArrayOfLong2) {
		assert (paramArrayOfLong1.length == paramArrayOfLong2.length);
		for (int i = 0; i < paramArrayOfLong1.length; ++i)
			if ((paramArrayOfLong2[i] & paramArrayOfLong1[i]) != paramArrayOfLong2[i])
				return false;
		return true;
	}

	public String toHexString() {
		StringBuffer localStringBuffer = new StringBuffer();
		byte[] arrayOfByte1 = toByteArray();
		for (int k : arrayOfByte1) {
			int l = k >> 4 & 0xF;
			int i1 = k & 0xF;
			localStringBuffer.append(hex[l]);
			localStringBuffer.append(hex[i1]);
		}
		return localStringBuffer.toString();
	}

	public void setToHexString(String paramString) {
		clear();
		char[] arrayOfChar = paramString.toCharArray();
		for (int i = 0; i < arrayOfChar.length; ++i) {
			int j = Byte.valueOf(String.valueOf(arrayOfChar[i]), 16)
					.byteValue();
			int k = i * 4;
			if (i % 2 == 0)
				k += 4;
			else
				k -= 4;
			if ((j & 0x1) != 0)
				set(k);
			if ((j & 0x2) != 0)
				set(k + 1);
			if ((j & 0x4) != 0)
				set(k + 2);
			if ((j & 0x8) == 0)
				continue;
			set(k + 3);
		}
	}

	public void setPreferredSize(int paramInt) {
		this.preferredSize = paramInt;
	}

	public int getPreferredSize() {
		return this.preferredSize;
	}

	static {
		hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
	}
}