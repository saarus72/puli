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


import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.liveontologies.puli.Inference;
import org.liveontologies.puli.InferenceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A convenience class for checking whether there are cyclic proofs for given
 * conclusions in the given inference set. A proof is cyclic if some conclusion
 * in the proof can be derived from itself. Cycle detection is performed by
 * descending over inferences in depth-first manner.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 */
public class InferenceSetCycleDetector<C> {

	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(InferenceSetCycleDetector.class);

	/**
	 * inferences that are filtered
	 */
	private final InferenceSet<C> originalInferences_;

	/**
	 * verified conclusions with cyclic proofs will be collected here
	 */
	private final Set<C> visitedCyclic_ = new HashSet<C>();

	/**
	 * verified conclusions without cyclic proofs will be collected here
	 */
	private final Set<C> visitedNonCyclic_ = new HashSet<C>();

	/**
	 * conclusions on the current proof path, for cycle detection
	 */
	private final Set<C> conclusionsOnPath_ = new HashSet<C>();

	/**
	 * the current stack of conclusions together with the iterators over
	 * inferences
	 */
	private final Deque<ConclusionRecord<C>> conclusionStack_ = new LinkedList<ConclusionRecord<C>>();

	/**
	 * the current stack of inferences together with the iterators over premises
	 */
	private final Deque<InferenceRecord<C>> inferenceStack_ = new LinkedList<InferenceRecord<C>>();

	InferenceSetCycleDetector(final InferenceSet<C> originalInferences) {
		this.originalInferences_ = originalInferences;
	}

	/**
	 * @param conclusion
	 * @return {@code true} if there exists a cyclic proof for the conclusion in
	 *         this inference set and {@code false} otherwise; a proof is cyclic
	 *         if some of the premises in the proof can be derived from itself
	 *         using the inferences in this inference set
	 */
	public boolean hasCyclicProofFor(C conclusion) {
		if (visitedNonCyclic_.contains(conclusion)) {
			return false;
		}
		push(conclusion);
		checkCycles();
		return (visitedCyclic_.contains(conclusion));
	}

	private boolean checkCycles() {
		for (;;) {
			ConclusionRecord<C> conclRec = conclusionStack_.peek();
			if (conclRec == null) {
				return false;
			}
			// else
			if (conclRec.inferenceIterator_.hasNext()) {
				push(conclRec.inferenceIterator_.next());
			} else {
				// no more inferences
				popNonCyclic();
			}
			InferenceRecord<C> infRec = inferenceStack_.peek();
			if (infRec == null) {
				return false;
			}
			for (;;) {
				if (!infRec.premiseIterator_.hasNext()) {
					// no more premises
					popInference();
					break;
				}
				// else
				C premise = infRec.premiseIterator_.next();
				if (visitedNonCyclic_.contains(premise)) {
					// already checked
					LOGGER_.trace("{}: already visited", premise);
					continue;
				}
				// else
				if (!push(premise)) {
					LOGGER_.trace("{}: CYCLE!", premise);
					inferenceStack_.clear();
					while (!conclusionStack_.isEmpty()) {
						popCyclic();
						// mark all conclusions on the path as cyclic
					}
					return true;
				}
				// else
				break;
			}

		}

	}

	private boolean push(C conclusion) {
		if (visitedCyclic_.contains(conclusion)
				|| !conclusionsOnPath_.add(conclusion)) {
			return false;
		}
		// else
		conclusionStack_
				.push(new ConclusionRecord<>(originalInferences_, conclusion));
		LOGGER_.trace("{}: conclusion pushed", conclusion);
		return true;
	}

	private void push(final Inference<C> inf) {
		inferenceStack_.push(new InferenceRecord<>(inf));
		LOGGER_.trace("{}: inference pushed", inf);
	}

	private ConclusionRecord<C> popNonCyclic() {
		ConclusionRecord<C> result = conclusionStack_.pop();
		LOGGER_.trace("{}: conclusion popped, non-cyclic", result.conclusion_);
		conclusionsOnPath_.remove(result.conclusion_);
		visitedNonCyclic_.add(result.conclusion_);
		return result;
	}

	private ConclusionRecord<C> popCyclic() {
		ConclusionRecord<C> result = conclusionStack_.pop();
		LOGGER_.trace("{}: conclusion popped, cyclic", result.conclusion_);
		conclusionsOnPath_.remove(result.conclusion_);
		visitedCyclic_.add(result.conclusion_);
		return result;
	}

	private InferenceRecord<C> popInference() {
		InferenceRecord<C> result = inferenceStack_.pop();
		LOGGER_.trace("{}: inference popped", result.inference_);
		return result;
	}

	private static class ConclusionRecord<C> {

		private final C conclusion_;

		private final Iterator<? extends Inference<C>> inferenceIterator_;

		ConclusionRecord(final InferenceSet<C> inferenceSet,
				final C conclusion) {
			this.conclusion_ = conclusion;
			this.inferenceIterator_ = inferenceSet.getInferences(conclusion)
					.iterator();
		}

	}

	private static class InferenceRecord<C> {

		Inference<C> inference_;

		private final Iterator<? extends C> premiseIterator_;

		InferenceRecord(Inference<C> inference) {
			this.inference_ = inference;
			this.premiseIterator_ = inference.getPremises().iterator();
		}

	}

}
