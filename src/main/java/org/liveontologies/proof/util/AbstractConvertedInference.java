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
