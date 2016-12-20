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

import java.util.Set;

class AcyclicDerivableFromProofNode<C> extends AcyclicProofNode<C> {

	private final Set<? extends C> statedAxioms_;

	private final DerivabilityChecker<ProofNode<C>> checker_ = new ProofNodeDerivabilityChecker<C>();

	AcyclicDerivableFromProofNode(ProofNode<C> delegate,
			AcyclicDerivableFromProofNode<C> parent,
			Set<? extends C> statedAxioms) {
		super(delegate, parent);
		Util.checkNotNull(statedAxioms);
		this.statedAxioms_ = statedAxioms;
	}

	AcyclicDerivableFromProofNode(ProofNode<C> delegate,
			Set<? extends C> statedAxioms) {
		super(delegate);
		this.statedAxioms_ = statedAxioms;
	}

	@Override
	final void convert(AcyclicProofStep<C> step) {
		ProofStep<C> delegate = step.getDelegate();
		for (ProofNode<C> premise : delegate.getPremises()) {
			if (!checker_.isDerivable(new ExtendedProofNode<C>(
					new FilteredProofNode<C>(premise, getBlockedNodes()),
					statedAxioms_))) {
				return;
			}
		}
		// all premises are derivable
		convert(new AcyclicDerivableFromProofStep<C>(delegate, this,
				statedAxioms_));
	}

	void convert(AcyclicDerivableFromProofStep<C> step) {
		super.convert(step);
	}

}
