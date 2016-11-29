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

import org.liveontologies.proof.util.AcyclicDerivableProofNode;
import org.liveontologies.proof.util.AcyclicDerivableProofStep;
import org.liveontologies.proof.util.AcyclicProofNode;
import org.liveontologies.proof.util.DelegatingProofStep;
import org.liveontologies.proof.util.FilteredProofNode;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofNodeDerivabilityChecker;
import org.liveontologies.proof.util.ProofStep;

class AcyclicDerivableProofNode<C> extends AcyclicProofNode<C> {

	AcyclicDerivableProofNode(ProofNode<C> delegate,
			AcyclicDerivableProofNode<C> parent) {
		super(delegate, parent);
	}

	AcyclicDerivableProofNode(ProofNode<C> delegate) {
		super(delegate);
	}

	@Override
	public Collection<ProofStep<C>> getInferences() {
		ProofNode<C> testNode = new FilteredProofNode<C>(getDelegate(),
				getBlockedNodes());
		ProofNodeDerivabilityChecker<C> checker = new ProofNodeDerivabilityChecker<C>();
		Collection<ProofStep<C>> result = new ArrayList<ProofStep<C>>();
		inference_loop: for (ProofStep<C> step : testNode.getInferences()) {
			for (ProofNode<C> premise : step.getPremises()) {
				if (!checker.isDerivable(premise)) {
					continue inference_loop;
				}
			}
			step = ((DelegatingProofStep<C>) step).getDelegate();
			result.add(convert(step));
		}
		return result;
	}

	@Override
	protected AcyclicDerivableProofStep<C> convert(ProofStep<C> step) {
		return new AcyclicDerivableProofStep<C>(step, this);
	}

}
