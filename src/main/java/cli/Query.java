/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package cli;

import graph.Graph;
import graph.Node;
import index.FingerprintIndexFileRAM;
import index.features.Fingerprint;
import index.features.FingerprintBuilder;
import index.features.storage.CountHashTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import match.MatcherFast;
import match.pattern.AtomFrequencyComparator;
import match.pattern.SearchPattern;
import tools.io.GIndexFormat;
import tools.io.GraphInput;
import tools.io.GraphInput.IDGraph;
import tools.io.GraphLabelConverter;

public class Query {
	public static String indexFileName;
	public static String graphFileName;
	public static String queryFileName;

	private static void parseAndCheck(String[] paramArrayOfString)
			throws FileNotFoundException {
		if (paramArrayOfString.length != 3) {
			System.out
					.println("Usage: java cli.Query indexFile graphFile queryFile");
			System.exit(1);
		}
		indexFileName = paramArrayOfString[0];
		graphFileName = paramArrayOfString[1];
		queryFileName = paramArrayOfString[2];
		File localFile1 = new File(indexFileName);
		File localFile2 = new File(graphFileName);
		File localFile3 = new File(queryFileName);
		if (!(localFile1.exists()))
			throw new FileNotFoundException("File not found: "
					+ localFile1.getAbsolutePath());
		if (!(localFile2.exists()))
			throw new FileNotFoundException("File not found: "
					+ localFile2.getAbsolutePath());
		if (!(localFile3.exists()))
			throw new FileNotFoundException("File not found: "
					+ localFile3.getAbsolutePath());
		System.out.println("Index File: \t\t" + localFile1.getAbsolutePath()
				+ "\n" + "Graph File: \t\t" + localFile2.getAbsolutePath()
				+ "\n" + "Query File: \t\t" + localFile3.getAbsolutePath());
	}

	public static void main(String[] paramArrayOfString)
			throws FileNotFoundException {
		parseAndCheck(paramArrayOfString);
		FingerprintIndexFileRAM localFingerprintIndexFileRAM = new FingerprintIndexFileRAM(
				indexFileName);
		localFingerprintIndexFileRAM.open();
		FingerprintBuilder localFingerprintBuilder = localFingerprintIndexFileRAM
				.getFingerprintBuilder();
		System.out.println("Fingerprint Size: \t"
				+ localFingerprintBuilder.getFingerprintSize() + "\n"
				+ "Max. Path Size: \t" + localFingerprintBuilder.getPathSize()
				+ "\n" + "Max. Subtree Size: \t"
				+ localFingerprintBuilder.getSubtreeSize() + "\n"
				+ "Max. Ring Size: \t" + localFingerprintBuilder.getRingSize());
		System.out.println();
		GIndexFormat localGIndexFormat = new GIndexFormat();
		GraphLabelConverter localGraphLabelConverter = new GraphLabelConverter();
		System.out.print("Loading graphs...");
		ArrayList<IDGraph> localArrayList1 = new ArrayList<IDGraph>();
		Iterator<?> localIterator1 = localGIndexFormat
				.createGraphIterator(new BufferedReader(new FileReader(
						graphFileName)));
		while (localIterator1.hasNext()) {
			GraphInput.IDGraph localObject1 = (GraphInput.IDGraph) localIterator1.next();
			localGraphLabelConverter
					.convert(localObject1.getGraph());
			localArrayList1.add(localObject1);
		}
		System.out.println("done");
		System.out.print("Computing vertex label frequencies...");
		CountHashTable<Object> localObject1 = new CountHashTable<Object>();
		Iterator<IDGraph> localObject2 = localArrayList1.iterator();
		GraphInput.IDGraph localIDGraph1;
		while (localObject2.hasNext()) {
			localIDGraph1 = localObject2.next();
			Iterator<?> localIterator2 = localIDGraph1.getGraph().nodes()
					.iterator();
			while (localIterator2.hasNext()) {
				Node localNode = (Node) localIterator2.next();
				localObject1.processFeature(localNode
						.getLabel());
			}
		}
		AtomFrequencyComparator
				.setAtomSymbolFrequency(localObject1
						.getResult());
		System.out.println("done");
		System.out.print("Loading query graphs...");
		ArrayList<IDGraph> _localObject2 = new ArrayList<IDGraph>();
		localIterator1 = localGIndexFormat
				.createGraphIterator(new BufferedReader(new FileReader(
						queryFileName)));
		while (localIterator1.hasNext()) {
			localIDGraph1 = (GraphInput.IDGraph) localIterator1.next();
			localGraphLabelConverter.convert(localIDGraph1.getGraph());
			_localObject2.add(localIDGraph1);
		}
		System.out.println("done");
		System.out.println();
		System.out
				.println("ID\tC\tD\tFPBuildTime\tIndexQueryTime\tVerificationTime");
		double d1 = 0.0D;
		int i = 0;
		int j = 0;
		long l1 = 0L;
		long l2 = 0L;
		long l3 = 0L;
		Iterator<?> localIterator3 = _localObject2.iterator();
		while (localIterator3.hasNext()) {
			GraphInput.IDGraph localIDGraph2 = (GraphInput.IDGraph) localIterator3
					.next();
			String str = localIDGraph2.getID();
			Graph localGraph = localIDGraph2.getGraph();
			long l4 = System.nanoTime();
			Fingerprint localFingerprint = localFingerprintIndexFileRAM
					.getFingerprintBuilder().getFingerprint(localGraph);
			long l5 = System.nanoTime() - l4;
			l4 = System.nanoTime();
			LinkedList<Integer> localLinkedList = localFingerprintIndexFileRAM
					.findCandidates(localFingerprint.toLongArray());
			long l6 = System.nanoTime() - l4;
			int k = localLinkedList.size();
			l4 = System.nanoTime();
			ArrayList<?> localArrayList2 = calculateAnswers(localLinkedList,
					localArrayList1, localIDGraph2);
			long l7 = System.nanoTime() - l4;
			int l = localArrayList2.size();
			double d2 = (k == 0) ? 1.0D : l / (double)k;
			System.out.println(str + "\t" + k + "\t" + l + "\t"
					+ (l5 / 1000000.0D) + "\t" + (l6 / 1000000.0D) + "\t"
					+ (l7 / 1000000.0D));
			i += k;
			j += l;
			l1 += l5;
			l2 += l6;
			l3 += l7;
			d1 += d2;
		}
		d1 /= _localObject2.size();
		System.out.println();
		System.out.println("Total number of candidates:    " + i);
		System.out.println("Total number of answers:       " + j);
		System.out.println("Total time FP build [s]:       "
				+ (l1 / 1000000000.0D));
		System.out.println("Total time index query [s]:    "
				+ (l2 / 1000000000.0D));
		System.out.println("Total time verification [s]:   "
				+ (l3 / 1000000000.0D));
		System.out.println("Avg. Accuracy:                 " + d1);
		localFingerprintIndexFileRAM.close();
	}

	/*
	private static ArrayList<String> calculateAnswers(
			ArrayList<IDGraph> paramArrayList,
			IDGraph paramIDGraph) {
		int i = 2;
		SearchPattern localSearchPattern = new SearchPattern(
				paramIDGraph.getGraph(), false, i);
		ArrayList<String> localArrayList = new ArrayList<String>();
		Iterator<IDGraph> localIterator = paramArrayList.iterator();
		while (localIterator.hasNext()) {
			GraphInput.IDGraph localIDGraph = localIterator
					.next();
			MatcherFast localMatcherFast = new MatcherFast(localSearchPattern,
					localIDGraph.getGraph());
			boolean bool = localMatcherFast.match();
			if (bool)
				localArrayList.add(localIDGraph.getID());
		}
		return localArrayList;
	}
	*/

	private static ArrayList<String> calculateAnswers(List<Integer> paramList,
			ArrayList<GraphInput.IDGraph> paramArrayList,
			GraphInput.IDGraph paramIDGraph) {
		int i = 2;
		SearchPattern localSearchPattern = new SearchPattern(
				paramIDGraph.getGraph(), false, i);
		ArrayList<String> localArrayList = new ArrayList<String>();
		Iterator<Integer> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			Integer localInteger = localIterator.next();
			GraphInput.IDGraph localIDGraph = paramArrayList
					.get(localInteger.intValue());
			MatcherFast localMatcherFast = new MatcherFast(localSearchPattern,
					localIDGraph.getGraph());
			boolean bool = localMatcherFast.match();
			if (bool)
				localArrayList.add(localIDGraph.getID());
		}
		return localArrayList;
	}
}