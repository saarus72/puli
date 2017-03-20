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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.JustifiedInference;
import org.semanticweb.elk.justifications.andorgraph.AndOrGraphs;
import org.semanticweb.elk.justifications.andorgraph.Node;

public class BottomUpOverAndOrGraphsForJustifications<C, A>
		extends CancellableJustificationComputation<C, A> {

	private static final BottomUpOverAndOrGraphsForJustifications.Factory<?, ?> FACTORY_ = new Factory<Object, Object>();

	@SuppressWarnings("unchecked")
	public static <C, A> JustificationComputation.Factory<C, A> getFactory() {
		return (Factory<C, A>) FACTORY_;
	}

	private final BottomUpOverAndOrGraphs<A> computation_;

	public BottomUpOverAndOrGraphsForJustifications(
			final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet,
			final Monitor monitor) {
		super(inferenceSet, monitor);
		this.computation_ = new BottomUpOverAndOrGraphs<>(monitor);
	}

	@Override
	public Collection<? extends Set<A>> computeJustifications(
			final C conclusion) {
		final Node<A> goal = AndOrGraphs
				.getAndOrGraphForJustifications(conclusion, getInferenceSet());
		return computation_.computeJustifications(goal);
	}

	@Override
	public Collection<? extends Set<A>> computeJustifications(
			final C conclusion, final int sizeLimit) {
		final Node<A> goal = AndOrGraphs
				.getAndOrGraphForJustifications(conclusion, getInferenceSet());
		return computation_.computeJustifications(goal, sizeLimit);
	}

	@Override
	public String[] getStatNames() {
		return computation_.getStatNames();
	}

	@Override
	public Map<String, Object> getStatistics() {
		return computation_.getStatistics();
	}

	private static class Factory<C, A>
			implements JustificationComputation.Factory<C, A> {

		@Override
		public JustificationComputation<C, A> create(
				final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet,
				final Monitor monitor) {
			return new BottomUpOverAndOrGraphsForJustifications<>(inferenceSet,
					monitor);
		}

		@Override
		public String[] getStatNames() {
			final String[] statNames = new String[] {};
			final String[] bloomStatNames = BloomSet.getStatNames();
			final String[] ret = Arrays.copyOf(statNames,
					statNames.length + bloomStatNames.length);
			System.arraycopy(bloomStatNames, 0, ret, statNames.length,
					bloomStatNames.length);
			return ret;
		}

	}

}
