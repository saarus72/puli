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

class LimitedProofNode<C> extends ConvertedProofNode<C> {

	private final int inferenceLimit_;

	LimitedProofNode(ProofNode<C> delegate, int inferenceLimit) {
		super(delegate);
		if (inferenceLimit <= 0) {
			throw new IllegalArgumentException(
					"Limit should be positive: " + inferenceLimit);
		}
		this.inferenceLimit_ = inferenceLimit;
	}

	@Override
	public Collection<ProofStep<C>> getInferences() {
		// cut original inferences after a limit
		Collection<ProofStep<C>> result = new ArrayList<ProofStep<C>>();
		int steps = 0;
		for (ProofStep<C> step : getDelegate().getInferences()) {
			result.add(convert(step));
			steps++;
			if (steps == inferenceLimit_) {
				break;
			}
		}
		return result;
	}

	@Override
	protected LimitedProofStep<C> convert(ProofStep<C> inf) {
		return new LimitedProofStep<C>(inf, inferenceLimit_);
	}

}
