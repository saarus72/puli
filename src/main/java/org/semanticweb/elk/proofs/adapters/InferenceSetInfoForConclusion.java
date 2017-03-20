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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.JustifiedInference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A convenience class for collecting basic information about the inferences
 * that used for deriving a given conclusion within a given inference set.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 * @param <A>
 */
public class InferenceSetInfoForConclusion<C, A> {

	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(InferenceSetInfoForConclusion.class);

	private final Set<C> usedConclusions_ = new HashSet<>();
	private final Set<A> usedAxioms_ = new HashSet<>();
	private final List<JustifiedInference<C, A>> usedInferences_ = new ArrayList<>();

	private final Queue<C> toDo_ = new LinkedList<C>();

	private final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences_;

	InferenceSetInfoForConclusion(
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences,
			C conclusion) {
		this.inferences_ = inferences;
		toDo(conclusion);
		process();
	}

	/**
	 * @return the inferences used in the proofs for the given conclusion
	 */
	public List<JustifiedInference<C, A>> getUsedInferences() {
		return usedInferences_;
	}

	/**
	 * @return the conclusions used in the proofs for the given conclusion
	 */
	public Set<C> getUsedConclusions() {
		return usedConclusions_;
	}

	/**
	 * @return the axioms used in justifications of inferences used in the
	 *         proofs for the given conclusion
	 */
	public Set<A> getUsedAxioms() {
		return usedAxioms_;
	}

	public void log() {
		LOGGER_.debug("{} used inferences", usedInferences_.size());
		LOGGER_.debug("{} used conclusions", usedConclusions_.size());
		LOGGER_.debug("{} used axioms", usedAxioms_.size());
	}

	private void toDo(C conclusion) {
		if (usedConclusions_.add(conclusion)) {
			toDo_.add(conclusion);
		}
	}

	private void process() {
		C next;
		while ((next = toDo_.poll()) != null) {
			for (JustifiedInference<C, A> inf : inferences_
					.getInferences(next)) {
				usedInferences_.add(inf);
				usedAxioms_.addAll(inf.getJustification());
				for (C premise : inf.getPremises()) {
					toDo(premise);
				}
			}
		}
	}

}
