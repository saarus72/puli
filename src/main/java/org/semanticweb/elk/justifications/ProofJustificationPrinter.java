package org.semanticweb.elk.justifications;

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

import java.io.BufferedWriter;
import java.io.IOException;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.JustifiedInference;
import org.semanticweb.elk.proofs.ProofPrinter;

/**
 * A simple pretty printer of proofs together with justification numbers for
 * conclusions.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 *            the type of the conclusions in proofs
 * @param <A>
 *            the type of the axioms in proofs
 */
public class ProofJustificationPrinter<C, A> extends ProofPrinter<C, A> {

	private final JustificationComputation<C, A> computation_;

	private final int sizeLimit_;

	ProofJustificationPrinter(JustificationComputation<C, A> computation,
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences,
			int sizeLimit) {
		super(inferences);
		this.computation_ = computation;
		this.sizeLimit_ = sizeLimit;
	}

	public static <C, A> void print(JustificationComputation<C, A> computation,
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences,
			C conclusion, int sizeLimit) throws IOException {
		ProofPrinter<C, A> pp = new ProofJustificationPrinter<>(computation,
				inferences, sizeLimit);
		pp.printProof(conclusion);
	}

	public static <C, A> void print(JustificationComputation<C, A> computation,
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences,
			C conclusion) throws IOException {
		print(computation, inferences, conclusion, Integer.MAX_VALUE);
	}

	@Override
	protected void writeConclusion(C conclusion) throws IOException {
		BufferedWriter w = getWriter();
		w.write('[');
		w.write(Integer.toString(computation_
				.computeJustifications(conclusion, sizeLimit_).size()));
		w.write(']');
		w.write(' ');
		super.writeConclusion(conclusion);
	}

}
