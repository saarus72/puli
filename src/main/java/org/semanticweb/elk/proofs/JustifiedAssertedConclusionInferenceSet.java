package org.semanticweb.elk.proofs;

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


import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.liveontologies.puli.Delegator;
import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.Inference;
import org.liveontologies.puli.InferenceSet;
import org.liveontologies.puli.JustifiedInference;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;

public class JustifiedAssertedConclusionInferenceSet<C>
		extends Delegator<InferenceSet<C>>
		implements GenericInferenceSet<C, JustifiedInference<C, C>> {

	private final Set<? extends C> assertedConclusions_;

	public JustifiedAssertedConclusionInferenceSet(
			final InferenceSet<C> delegate,
			final Set<? extends C> assertedConclusions) {
		super(delegate);
		this.assertedConclusions_ = assertedConclusions;
	}

	@Override
	public Collection<? extends JustifiedInference<C, C>> getInferences(
			final C conclusion) {
		final Collection<JustifiedInference<C, C>> result = Collections2
				.transform(getDelegate().getInferences(conclusion),
						new Function<Inference<C>, JustifiedInference<C, C>>() {

							@Override
							public JustifiedInference<C, C> apply(
									final Inference<C> input) {
								return new DummyJustifiedInference<C, C>(input);
							}

						});
		if (!assertedConclusions_.contains(conclusion)) {
			return result;
		}
		// else, add asserted conclusion inference
		return new AbstractCollection<JustifiedInference<C, C>>() {

			@Override
			public Iterator<JustifiedInference<C, C>> iterator() {
				return Iterators
						.concat(result.iterator(),
								Collections
										.singleton(
												new AssertedConclusionJustifiedInference.Projection<C>(
														conclusion))
										.iterator());
			}

			@Override
			public int size() {
				return result.size() + 1;
			}

		};
	}

}
