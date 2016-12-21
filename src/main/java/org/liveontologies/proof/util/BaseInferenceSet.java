package org.liveontologies.proof.util;

/*-
 * #%L
 * OWL API Proof Extension
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Live Ontologies Project
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base implementation for inference sets
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 */
public class BaseInferenceSet<C>
		implements ModifiableInferenceSet<C>, DynamicInferenceSet<C> {

	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(BaseInferenceSet.class);

	private final Map<C, Collection<Inference<C>>> inferences_ = new HashMap<C, Collection<Inference<C>>>();

	/**
	 * conclusion for which {@link #get(Object)} was called and the result did
	 * not change since then
	 */
	private final Set<C> queried_ = new HashSet<C>();

	private final List<ChangeListener> listeners_ = new ArrayList<ChangeListener>();

	@Override
	public Collection<? extends Inference<C>> getInferences(C conclusion) {
		Collection<? extends Inference<C>> result = inferences_.get(conclusion);
		if (result == null) {
			result = Collections.emptyList();
		}
		return result;
	}

	@Override
	public void addListener(ChangeListener listener) {
		listeners_.add(listener);
	}

	@Override
	public void removeListener(ChangeListener listener) {
		listeners_.remove(listener);
	}

	@Override
	public void produce(Inference<C> inference) {
		LOGGER_.trace("{}: inference added", inference);
		C conclusion = inference.getConclusion();
		Collection<Inference<C>> existing = inferences_.get(conclusion);
		if (existing == null) {
			existing = new ArrayList<Inference<C>>();
			inferences_.put(conclusion, existing);
		}
		existing.add(inference);
		if (queried_.contains(conclusion)) {
			fireChanged();
		}
	}

	@Override
	public void clear() {
		if (inferences_.isEmpty()) {
			return;
		}
		// else
		LOGGER_.trace("inferences cleared");
		inferences_.clear();
		if (!queried_.isEmpty()) {
			fireChanged();
		}
	}

	protected void fireChanged() {
		queried_.clear();
		for (ChangeListener listener : listeners_) {
			listener.inferencesChanged();
		}
	}

	/**
	 * Checks if all premises of the given {@link Inference} are conclusions of
	 * some inferences stored in this {@link InferenceSet} and throws a
	 * {@link RuntimeException} otherwise
	 * 
	 * @param inference
	 */
	protected void checkDerived(Inference<C> inference) {
		List<? extends C> premises = inference.getPremises();
		Set<C> derived = inferences_.keySet();
		for (int i = 0; i < premises.size(); i++) {
			C premise = premises.get(i);
			if (!derived.contains(premise)) {
				throw new RuntimeException(
						inference + ": premise not derived: " + premise);
			}
		}
	}

	@Override
	public void dispose() {
		// no-op
	}

}
