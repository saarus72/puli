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

class AcyclicDerivableProofNode<C> extends AcyclicProofNode<C> {

	private final DerivabilityChecker<ProofNode<C>> checker_ = new ProofNodeDerivabilityChecker<C>();

	AcyclicDerivableProofNode(ProofNode<C> delegate,
			AcyclicDerivableProofNode<C> parent) {
		super(delegate, parent);
	}

	AcyclicDerivableProofNode(ProofNode<C> delegate) {
		this(delegate, null);
	}

	@Override
	final void convert(AcyclicProofStep<C> step) {
		ProofStep<C> delegate = step.getDelegate();
		for (ProofNode<C> premise : delegate.getPremises()) {
			if (!checker_.isDerivable(
					new FilteredProofNode<C>(premise, getBlockedNodes()))) {
				return;
			}
		}
		// all premises are derivable
		convert(new AcyclicDerivableProofStep<C>(delegate, this));
	}

	void convert(AcyclicDerivableProofStep<C> step) {
		super.convert(step);
	}

}
