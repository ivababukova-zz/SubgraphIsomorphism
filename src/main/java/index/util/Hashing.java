/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package index.util;

import java.util.Random;
import java.util.zip.CRC32;

public class Hashing {
	private static final Random rng = new Random();
	public static int multHash(String paramString, int paramInt) {
		double d1 = (Math.sqrt(5.0D) - 1.0D) / 2.0D;
		int i = paramString.hashCode();
		double d2 = d1 * i % 1.0D * paramInt;
		return (int) Math.floor(Math.abs(d2));
	}

	public static int hash(Object paramObject, int paramInt) {
		int i = paramObject.hashCode();
		i ^= i >>> 20 ^ i >>> 12;
		i ^= i >>> 7 ^ i >>> 4;
		return (i & paramInt - 1);
	}

	public static int cdkHash(Object paramObject, int paramInt) {
		rng.setSeed(paramObject.hashCode());
		return rng.nextInt(paramInt);
	}

	public static int crc32Hash(String paramString, int paramInt) {
		long l = crc32(paramString);
		return Math.abs((int) (l % paramInt));
	}

	public static int crc32RandomHash(String paramString, int paramInt) {
		long l = crc32(paramString);
		rng.setSeed(l);
		return rng.nextInt(paramInt);
	}

	public static int djb2Hash(String paramString, int paramInt) {
		int i = 5381;
		for (int j = 0; j < paramString.length(); ++j)
			i = (i << 5) + i + paramString.charAt(j);
		return Math.abs(i % paramInt);
	}

	public static long crc32(String paramString) {
		CRC32 localCRC32 = new CRC32();
		localCRC32.update(paramString.getBytes());
		return localCRC32.getValue();
	}

	public static int DEKHash(String paramString, int paramInt) {
		int i = paramString.length();
		for (int j = 0; j < paramString.length(); ++j)
			i = i << 5 ^ i >> 27 ^ paramString.charAt(j);
		return Math.abs(i % paramInt);
	}

	public static int ELFHash(String paramString, int paramInt) {
		long l1 = 0L;
		long l2 = 0L;
		for (int i = 0; i < paramString.length(); ++i) {
			l1 = (l1 << 4) + paramString.charAt(i);
			if ((l2 = l1 & 0xF0000000) != 0L)
				l1 ^= l2 >> 24;
			l1 &= (l2 ^ 0xFFFFFFFF);
		}
		return Math.abs((int) (l1 % paramInt));
	}

	public static int SDBMHash(String paramString, int paramInt) {
		int i = 0;
		for (int j = 0; j < paramString.length(); ++j)
			i = paramString.charAt(j) + (i << 6) + (i << 16) - i;
		return Math.abs(i % paramInt);
	}
}