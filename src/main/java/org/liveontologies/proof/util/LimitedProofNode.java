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

class LimitedProofNode<C> extends ConvertedProofNode<C> {

	private final int inferenceLimit_;

	private int inferenceCount_ = 0;

	LimitedProofNode(ProofNode<C> delegate, int inferenceLimit) {
		super(delegate);
		if (inferenceLimit <= 0) {
			throw new IllegalArgumentException(
					"Limit should be positive: " + inferenceLimit);
		}
		this.inferenceLimit_ = inferenceLimit;
	}

	@Override
	protected final void convert(ConvertedProofStep<C> step) {
		if (inferenceCount_ < inferenceLimit_) {
			convert(new LimitedProofStep<C>(step.getDelegate(),
					inferenceLimit_));
			inferenceCount_++;
		}
	}

	void convert(LimitedProofStep<C> step) {
		super.convert(step);
	}

}
