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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.JustifiedInference;
import org.semanticweb.elk.proofs.adapters.InferenceSets;

/**
 * The {@link BottomUpJustificationComputation} applied to the binarization of
 * the input inference set.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 *            the type of conclusion and premises used by the inferences
 * @param <A>
 *            the type of axioms used by the inferences
 */
public class BinarizedJustificationComputation<C, A>
		extends AbstractJustificationComputation<C, A> {

	private final JustificationComputation<List<C>, A> computaiton_;

	BinarizedJustificationComputation(
			JustificationComputation.Factory<List<C>, A> mainFactory,
			GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferences,
			final Monitor monitor) {
		super(inferences);
		computaiton_ = mainFactory.create(InferenceSets.binarize(inferences),
				monitor);
	}

	@Override
	public Collection<? extends Set<A>> computeJustifications(C conclusion) {
		return computaiton_
				.computeJustifications(Collections.singletonList(conclusion));
	}

	@Override
	public Collection<? extends Set<A>> computeJustifications(C conclusion,
			int sizeLimit) {
		return computaiton_.computeJustifications(
				Collections.singletonList(conclusion), sizeLimit);
	}

	@Override
	public String[] getStatNames() {
		return computaiton_.getStatNames();
	}

	@Override
	public Map<String, Object> getStatistics() {
		return computaiton_.getStatistics();
	}

	@Override
	public void logStatistics() {
		computaiton_.logStatistics();
	}

	@Override
	public void resetStatistics() {
		computaiton_.resetStatistics();
	}

	public static <C, A> JustificationComputation.Factory<C, A> getFactory(
			JustificationComputation.Factory<List<C>, A> mainFactory) {
		return new Factory<C, A>(mainFactory);
	}

	private static class Factory<C, A>
			implements JustificationComputation.Factory<C, A> {

		JustificationComputation.Factory<List<C>, A> mainFactory_;

		Factory(JustificationComputation.Factory<List<C>, A> mainFactory) {
			this.mainFactory_ = mainFactory;
		}

		@Override
		public JustificationComputation<C, A> create(
				final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet,
				final Monitor monitor) {
			return new BinarizedJustificationComputation<C, A>(mainFactory_,
					inferenceSet, monitor);
		}

		@Override
		public String[] getStatNames() {
			return mainFactory_.getStatNames();
		}

	}

}
