package org.semanticweb.elk.proofs.adapters;

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

import java.util.List;
import java.util.Set;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.Inference;
import org.liveontologies.puli.InferenceSet;
import org.liveontologies.puli.JustifiedInference;
import org.semanticweb.elk.proofs.JustifiedAssertedConclusionInferenceSet;

/**
 * Static utilities for inference sets
 * 
 * @author Yevgeny Kazakov
 *
 */
public class InferenceSets {

	public static <C, A> GenericInferenceSet<List<C>, ? extends JustifiedInference<List<C>, A>> binarize(
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences) {
		return new BinarizedInferenceSetAdapter<C, A>(inferences);
	}

	public static <C, I extends Inference<C>> GenericInferenceSet<C, I> eliminateCycles(
			final GenericInferenceSet<C, I> inferences) {
		return new CycleRemovingInferenceSetAdapter<C, I>(inferences);
	}

	public static <C, I extends JustifiedInference<C, A>, A> GenericInferenceSet<C, I> eliminateTautologyInferences(
			final GenericInferenceSet<C, I> inferences) {
		return new TautologyRemovingInferenceSetAdapter<C, I, A>(inferences);
	}

	public static <C> boolean hasCycle(final InferenceSet<C> inferences,
			final C conclusion) {
		return (new InferenceSetCycleDetector<C>(inferences))
				.hasCyclicProofFor(conclusion);
	}

	public static <C, A> InferenceSetInfoForConclusion<C, A> getInfo(
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences,
			C conclusion) {
		return new InferenceSetInfoForConclusion<C, A>(inferences, conclusion);
	}

	public static <C> GenericInferenceSet<C, ? extends JustifiedInference<C, C>> justifyAsserted(
			final InferenceSet<C> inferences, final Set<? extends C> asserted) {
		return new JustifiedAssertedConclusionInferenceSet<C>(inferences,
				asserted);
	}

}
