//package org.semanticweb.elk.justifications;

/*-
 * #%L
 * Proof Utility Library
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2017 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import org.liveontologies.puli.GenericInferenceSet;
//import org.liveontologies.puli.JustifiedInference;
//import org.semanticweb.elk.justifications.ConvertToElSatKrssInput.ElSatPrinterVisitor;
//import org.semanticweb.elk.owlapi.ElkProver;
//import org.semanticweb.elk.owlapi.ElkProverFactory;
//import org.semanticweb.elk.owlapi.wrapper.OwlConverter;
//import org.semanticweb.elk.proofs.adapters.InferenceSets;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.AxiomType;
//import org.semanticweb.owlapi.model.OWLAxiom;
//import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.OWLPropertyAxiom;
//import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
//import org.semanticweb.owlapi.model.parameters.Imports;
//import org.semanticweb.owlapi.reasoner.InferenceType;
//import org.semanticweb.owlapi.reasoner.UnsupportedEntailmentTypeException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.base.Function;
//import com.google.common.base.Functions;
//import com.google.common.collect.Iterables;
//
//public class DirectSatEncoding {
//	
//	private static final Logger LOG =
//			LoggerFactory.getLogger(DirectSatEncoding.class);
//
//	public static void main(final String[] args) {
//		
//		if (args.length < 3) {
//			LOG.error("Insufficient arguments!");
//			System.exit(1);
//		}
//		
//		final String ontologyFileName = args[0];
//		final String conclusionsFileName = args[1];
//		final File outputDirectory = new File(args[2]);
//		if (!Utils.cleanDir(outputDirectory)) {
//			LOG.error("Could not prepare the output directory!");
//			System.exit(2);
//		}
//		
//		final OWLOntologyManager manager =
//				OWLManager.createOWLOntologyManager();
//
//		try {
//			
//			LOG.info("Loading ontology ...");
//			long start = System.currentTimeMillis();
//			final OWLOntology ont = manager.loadOntologyFromOntologyDocument(
//					new File(ontologyFileName));
//			LOG.info("... took {}s",
//					(System.currentTimeMillis() - start)/1000.0);
//			LOG.info("Loaded ontology: {}", ont.getOntologyID());
//			
//			LOG.info("Loading conclusions ...");
//			start = System.currentTimeMillis();
//			final OWLOntology conclusionsOnt =
//					manager.loadOntologyFromOntologyDocument(
//							new File(conclusionsFileName));
//			final Set<OWLSubClassOfAxiom> conclusions =
//					conclusionsOnt.getAxioms(AxiomType.SUBCLASS_OF);
//			LOG.info("... took {}s",
//					(System.currentTimeMillis() - start)/1000.0);
//			LOG.info("Number of conclusions: {}", conclusions.size());
//			
//			final ElkProverFactory proverFactory = new ElkProverFactory();
//			final ElkProver reasoner = proverFactory.createReasoner(ont);
//			
//			LOG.info("Classifying ...");
//			start = System.currentTimeMillis();
//			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
//			LOG.info("... took {}s",
//					(System.currentTimeMillis() - start)/1000.0);
//
//			for (final OWLSubClassOfAxiom conclusion : conclusions) {
//				encodeConclusion(reasoner, conclusion, outputDirectory);
//			}
//			
//		} catch (final OWLOntologyCreationException e) {
//			LOG.error("Could not load the ontology!", e);
//			System.exit(2);
//		} catch (final UnsupportedEntailmentTypeException e) {
//			LOG.error("Could not obtain the proof!", e);
//			System.exit(3);
//		} catch (final FileNotFoundException e) {
//			LOG.error("File Not Found!", e);
//			System.exit(2);
//		}
//		
//	}
//
//	private static void encodeConclusion(final ElkProver reasoner,
//			final OWLSubClassOfAxiom conclusion, final File outputDirectory)
//					throws FileNotFoundException {
//
//		final String conclName = Utils.toFileName(conclusion);
//		final File outDir = new File(outputDirectory, conclName);
//		final File hFile = new File(outDir, "encoding.h");
//		final File cnfFile = new File(outDir, "encoding.cnf");
//		final File questionFile = new File(outDir, "encoding.question");
//		final File pppFile = new File(outDir, "encoding.ppp");
//		final File pppguFile = new File(outDir, "encoding.ppp.g.u");
//		final File zzzFile = new File(outDir, "encoding.zzz");
//		final File zzzgciFile = new File(outDir, "encoding.zzz.gci");
//		final File zzzriFile = new File(outDir, "encoding.zzz.ri");
//		outDir.mkdirs();
//		
//		PrintWriter cnfWriter = null;
//		PrintWriter hWriter = null;
//		
//		try {
//			
//			cnfWriter = new PrintWriter(cnfFile);
//			hWriter = new PrintWriter(hFile);
//			final PrintWriter cnf = cnfWriter;
//			
//			final GenericInferenceSet<OWLAxiom, ? extends JustifiedInference<OWLAxiom, OWLAxiom>> inferenceSet =
//					InferenceSets.justifyAsserted(reasoner.getProof(conclusion),
//							reasoner.getRootOntology().getAxioms(Imports.EXCLUDED));
//			
//			final Set<OWLAxiom> axiomExprs =
//					new HashSet<OWLAxiom>();
//			final Set<OWLAxiom> lemmaExprs =
//					new HashSet<OWLAxiom>();
//			
//			Utils.traverseProofs(conclusion, inferenceSet,
//					Functions.<JustifiedInference<OWLAxiom, OWLAxiom>>identity(),
//					new Function<OWLAxiom, Void>(){
//						@Override
//						public Void apply(final OWLAxiom expr) {
//							lemmaExprs.add(expr);
//							return null;
//						}
//					},
//					new Function<OWLAxiom, Void>(){
//						@Override
//						public Void apply(final OWLAxiom axiom) {
//							axiomExprs.add(axiom);
//							return null;
//						}
//					}
//			);
//			
//			final Counter literalCounter = new Counter(1);
//			final Counter clauseCounter = new Counter();
//			
//			final Map<OWLAxiom, Integer> axiomIndex =
//					new HashMap<OWLAxiom, Integer>();
//			for (final OWLAxiom axExpr : axiomExprs) {
//				axiomIndex.put(axExpr, literalCounter.next());
//			}
//			final Map<OWLAxiom, Integer> conclusionIndex =
//					new HashMap<OWLAxiom, Integer>();
//			for (final OWLAxiom expr : lemmaExprs) {
//				conclusionIndex.put(expr, literalCounter.next());
//			}
//			
//			// cnf
//			Utils.traverseProofs(conclusion, inferenceSet,
//					new Function<JustifiedInference<OWLAxiom, OWLAxiom>, Void>() {
//						@Override
//						public Void apply(
//								final JustifiedInference<OWLAxiom, OWLAxiom> inf) {
//							
//							LOG.trace("processing {}", inf);
//							
//							for (final OWLAxiom axiom :
//									inf.getJustification()) {
//								cnf.print(-axiomIndex.get(axiom));
//								cnf.print(" ");
//							}
//							
//							for (final OWLAxiom premise :
//									inf.getPremises()) {
//								cnf.print(-conclusionIndex.get(premise));
//								cnf.print(" ");
//							}
//							
//							cnf.print(conclusionIndex.get(inf.getConclusion()));
//							cnf.println(" 0");
//							clauseCounter.next();
//							
//							return null;
//						}
//					},
//					Functions.<OWLAxiom>identity(),
//					Functions.<OWLAxiom>identity());
//			
//			final int lastLiteral = literalCounter.next();
//			
//			// h
//			hWriter.println("p cnf " + (lastLiteral - 1)
//					+ " " + clauseCounter.next());
//			
//			// ppp
//			writeLines(axiomIndex.values(), pppFile);
//			
//			// ppp.g.u
//			final List<Integer> orderedAxioms =
//					new ArrayList<Integer>(axiomIndex.values());
//			Collections.sort(orderedAxioms);
//			writeLines(orderedAxioms, pppguFile);
//			
//			// question
//			writeLines(Collections.singleton(conclusionIndex.get(conclusion)),
//					questionFile);
//			
//			// zzz
//			final SortedMap<Integer, OWLAxiom> gcis =
//					new TreeMap<Integer, OWLAxiom>();
//			final SortedMap<Integer, OWLAxiom> ris =
//					new TreeMap<Integer, OWLAxiom>();
//			for (final Entry<OWLAxiom, Integer> entry
//					: axiomIndex.entrySet()) {
//				final OWLAxiom expr = entry.getKey();
//				final int lit = entry.getValue();
//				if (expr instanceof OWLPropertyAxiom) {
//					ris.put(lit, expr);
//				} else {
//					gcis.put(lit, expr);
//				}
//			}
//			final SortedMap<Integer, OWLAxiom> lemmas =
//					new TreeMap<Integer, OWLAxiom>();
//			for (final Entry<OWLAxiom, Integer> entry
//					: conclusionIndex.entrySet()) {
//				lemmas.put(entry.getValue(), entry.getKey());
//			}
//			
//			writeLines(Iterables.transform(gcis.entrySet(), PRINT2), zzzgciFile);
//			writeLines(Iterables.transform(ris.entrySet(), PRINT2), zzzriFile);
//			writeLines(Iterables.transform(lemmas.entrySet(), PRINT), zzzFile);
//			
//		} finally {
//			if (cnfWriter != null) {
//				cnfWriter.close();
//			}
//			if (hWriter != null) {
//				hWriter.close();
//			}
//		}
//		
//	}
//	
//	private static void writeLines(final Iterable<?> lines, final File file)
//			throws FileNotFoundException {
//		
//		PrintWriter writer = null;
//		
//		try {
//			writer = new PrintWriter(file);
//			
//			for (final Object line : lines) {
//				writer.println(line);
//			}
//			
//		} finally {
//			if (writer != null) {
//				writer.close();
//			}
//		}
//		
//	}
//	
//	private static final OwlConverter OWLCONVERTER = OwlConverter.getInstance();
//	
//	private static final Function<Entry<Integer, OWLAxiom>, String> PRINT =
//			new Function<Entry<Integer, OWLAxiom>, String>() {
//		
//		@Override
//		public String apply(final Entry<Integer, OWLAxiom> entry) {
//			final StringBuilder result = new StringBuilder();
//			
//			result.append(entry.getKey()).append(" ");
//			
//			final ElSatPrinterVisitor printer = new ElSatPrinterVisitor(result);
//			
//			OWLCONVERTER.convert(entry.getValue()).accept(printer);
//			
//			result.setLength(result.length() - 1);// Remove the last line end.
//			
//			return result.toString();
//		}
//		
//	};
//	
//	private static final Function<Entry<Integer, OWLAxiom>, String> PRINT2 =
//			new Function<Entry<Integer, OWLAxiom>, String>() {
//		
//		@Override
//		public String apply(final Entry<Integer, OWLAxiom> entry) {
//			final StringBuilder result = new StringBuilder();
//			
//			result.append(entry.getKey()).append(" ");
//			
//			final ElSatPrinterVisitor printer = new ElSatPrinterVisitor(result);
//			
//			OWLCONVERTER.convert(entry.getValue()).accept(printer);
//			
//			result.setLength(result.length() - 1);// Remove the last line end.
//			
//			return result.toString();
//		}
//		
//	};
//	
//	private static class Counter {
//		private int counter;
//		public Counter() {
//			this(0);
//		}
//		public Counter(final int first) {
//			this.counter = first;
//		}
//		public int next() {
//			return counter++;
//		}
//	}
//	
//}
