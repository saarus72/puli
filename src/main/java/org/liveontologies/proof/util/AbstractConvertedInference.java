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
package org.liveontologies.proof.util;

import java.util.AbstractList;
import java.util.List;

/**
 * A skeleton implementation for conversion of inferences
 * 
 * @author Yevgeny Kazakov
 *
 * @param <I>
 *            the type of conclusion and premises of the input inference
 * @param <O>
 *            the type of conclusion and premises of the output inference
 */
public abstract class AbstractConvertedInference<I, O> implements Inference<O> {

	private final Inference<I> input_;

	public AbstractConvertedInference(Inference<I> input) {
		this.input_ = input;
	}

	public Inference<I> getInput() {
		return input_;
	}

	@Override
	public String getName() {
		return input_.getName();
	}

	@Override
	public O getConclusion() {
		return convert(input_.getConclusion());
	}

	@Override
	public List<? extends O> getPremises() {
		final List<? extends I> inputPremises = input_.getPremises();
		return new AbstractList<O>() {

			@Override
			public O get(int index) {
				return convert(inputPremises.get(index));
			}

			@Override
			public int size() {
				return inputPremises.size();
			}
		};
	}

	protected abstract O convert(I conclusion);

}
