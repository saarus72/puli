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


import java.util.Collection;
import java.util.Set;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.JustifiedInference;

/**
 * A common interface for procedures that compute justifications of conclusions
 * from sets of inferences. Justification is a smallest set of axioms such that
 * there is a proof of the conclusion using only inferences with justifications
 * in this set.
 * 
 * @author Yevgeny Kazakov
 * @author Peter Skocovsky
 *
 * @param <C>
 *            the type of conclusion and premises used by the inferences
 * @param <A>
 *            the type of axioms used by the inferences
 */
public interface JustificationComputation<C, A> extends HasStatistics {

	/**
	 * @return the inference set used by this computation
	 */
	GenericInferenceSet<C, ? extends JustifiedInference<C, A>> getInferenceSet();

	/**
	 * Computes all justifications for the given conclusion. This method can be
	 * called several times for different conclusions.
	 * 
	 * @see JustifiedInference#getJustification()
	 * 
	 * @param conclusion
	 *            the conclusion for which to compute the justification
	 * @return the set consisting of all justifications for the given conclusion
	 */
	Collection<? extends Set<A>> computeJustifications(C conclusion);

	/**
	 * Computes all justifications up to the given size for the given
	 * conclusion. This method can be called several times for different
	 * conclusions.
	 * 
	 * @see JustifiedInference#getJustification()
	 * 
	 * @param conclusion
	 *            the conclusion for which to compute the justification
	 * @param sizeLimit
	 *            the maximal size of the justifications returned
	 * @return the set consisting of all justifications up to the give size
	 *         limit for the given conclusion
	 */
	Collection<? extends Set<A>> computeJustifications(C conclusion,
			int sizeLimit);

	/**
	 * Starts computation of justifications and visits every justification using
	 * the provided visitor as soon as it is computed. The visitor is called
	 * exactly once for every justification. When the method returns, all
	 * justifications must be visited.
	 * 
	 * @param conclusion
	 *            the conclusion for which to compute the justification
	 * @param visitor
	 *            the visitor using which to process justifications
	 */
//	void enumerateJustifications(C conclusion,
//			Justification.Visitor<C, A, ?> visitor);

	/**
	 * Factory for creating computations
	 * 
	 * @author Yevgeny Kazakov
	 * 
	 * @param <C>
	 *            the type of conclusion and premises used by the inferences
	 * @param <A>
	 *            the type of axioms used by the inferences
	 */
	static interface Factory<C, A> {

		/**
		 * @param inferenceSet
		 * @param monitor
		 * @return a new justification computation which uses the given
		 *         inference set
		 */
		JustificationComputation<C, A> create(
				GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet,
				Monitor monitor);

		/**
		 * @return the keys of the statistics map returned by the method
		 *         {@link HasStatistics#getStatistics()} of the
		 *         {@link JustificationComputation} created by this factory.
		 */
		String[] getStatNames();

	}

}
