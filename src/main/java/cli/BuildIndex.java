/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package cli;
import org.apache.spark.api.java.*;
import graph.Graph;
import index.FingerprintIndexFile;
import index.features.FingerprintBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import tools.io.GIndexFormat;
import tools.io.GraphInput;
import tools.io.GraphInput.IDGraph;
import tools.io.GraphLabelConverter;
import org.apache.spark.*;

public class BuildIndex {
	public static String indexFileName;
	public static String graphFileName;
	public static int fingerprintSize;
	public static int maxPathSize;
	public static int maxSubtreeSize;
	public static int maxRingSize;

	private static void parseAndCheck(String[] paramArrayOfString)
			throws FileNotFoundException {
		if (paramArrayOfString.length != 6) {
			System.out
					.println("Usage: java cli.BuildIndex indexFile graphFile fingerprintSize maxPathSize maxSubtreeSize maxRingSize");
			System.exit(1);
		}
		indexFileName = paramArrayOfString[0];
		graphFileName = paramArrayOfString[1];
		fingerprintSize = Integer.valueOf(paramArrayOfString[2]).intValue();
		maxPathSize = Integer.valueOf(paramArrayOfString[3]).intValue();
		maxSubtreeSize = Integer.valueOf(paramArrayOfString[4]).intValue();
		maxRingSize = Integer.valueOf(paramArrayOfString[5]).intValue();
		File localFile1 = new File(indexFileName);
		File localFile2 = new File(graphFileName);
		if (!(localFile2.exists()))
			throw new FileNotFoundException("File not found: "
					+ localFile2.getAbsolutePath());
		if (!(isPowerOfTwo(fingerprintSize)))
			throw new IllegalArgumentException(
					"Fingerprint size must be a power of two.");
		System.out.println("Index File: \t\t" + localFile1.getAbsolutePath()
				+ "\n" + "Graph File: \t\t" + localFile2.getAbsolutePath()
				+ "\n" + "Fingerprint Size: \t" + fingerprintSize + "\n"
				+ "Max. Path Size: \t" + maxPathSize + "\n"
				+ "Max. Subtree Size: \t" + maxSubtreeSize + "\n"
				+ "Max. Ring Size: \t" + maxRingSize);
		System.out.println();
	}

	private static boolean isPowerOfTwo(int paramInt) {
		return ((paramInt & -paramInt) == paramInt);
	}

	public static void main(String[] paramArrayOfString) throws IOException {
		parseAndCheck(paramArrayOfString);
		File localFile = new File(indexFileName);
		localFile.delete();
		FingerprintBuilder localFingerprintBuilder = new FingerprintBuilder(
				fingerprintSize, maxPathSize, maxSubtreeSize, maxRingSize);
		FingerprintIndexFile localFingerprintIndexFile = new FingerprintIndexFile(
				localFingerprintBuilder, indexFileName);
		localFingerprintIndexFile.create();
		GIndexFormat localGIndexFormat = new GIndexFormat();
		GraphLabelConverter localGraphLabelConverter = new GraphLabelConverter();
		System.out.print("Loading graphs...");
		ArrayList<IDGraph> localArrayList = new ArrayList<IDGraph>();
		Iterator<IDGraph> localIterator1 = localGIndexFormat
				.createGraphIterator(new BufferedReader(new FileReader(
						graphFileName)));
		while (localIterator1.hasNext()) {
			GraphInput.IDGraph localIDGraph1 = localIterator1.next();
			localGraphLabelConverter.convert(localIDGraph1.getGraph());
			localArrayList.add(localIDGraph1);
			System.out.println("" + localIDGraph1.getID()); 
		}
		System.out.println("number of graphs: " + localArrayList.size());
		System.out.println("done");
		System.out.print("Building index...");
		long l1 = System.nanoTime();
		Iterator<IDGraph> localIterator2 = localArrayList.iterator();
		while (localIterator2.hasNext()) {
			GraphInput.IDGraph localIDGraph2 = localIterator2.next();
			String str = localIDGraph2.getID();
			Graph localGraph = localIDGraph2.getGraph();
			localFingerprintIndexFile.addGraph(Integer.valueOf(str).intValue(),
					localGraph);
		}
		long l2 = System.nanoTime() - l1;
		localFingerprintIndexFile.close();
		System.out.println("done");
		System.out.println();
		System.out.println("Index Build Time [s]: \t" + (l2 / 1000000000.0D));
		System.out.println(localFingerprintIndexFile.getStatistics());
	}
}
