package org.liveontologies.proof.util;

import java.util.ArrayList;
import java.util.Collection;

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

import org.liveontologies.proof.util.ConvertedProofNode;
import org.liveontologies.proof.util.DelegatingProofStep;
import org.liveontologies.proof.util.DerivableFromProofStep;
import org.liveontologies.proof.util.DerivableProofNode;
import org.liveontologies.proof.util.ExtendedProofNode;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofStep;

class DerivableFromProofNode<C> extends ConvertedProofNode<C> {

	private final Set<? extends C> statedAxioms_;

	DerivableFromProofNode(ProofNode<C> delegate,
			Set<? extends C> statedAxioms) {
		super(delegate);
		this.statedAxioms_ = statedAxioms;
	}

	@Override
	public Collection<ProofStep<C>> getInferences() {
		ProofNode<C> testNode = new ExtendedProofNode<C>(getDelegate(),
				statedAxioms_);
		testNode = new DerivableProofNode<C>(testNode);
		Collection<ProofStep<C>> result = new ArrayList<ProofStep<C>>();
		for (ProofStep<C> step : testNode.getInferences()) {
			step = ((DelegatingProofStep<C>) step).getDelegate();
			step = ((DelegatingProofStep<C>) step).getDelegate();
			result.add(convert(step));
		}
		return result;
	}

	@Override
	protected DerivableFromProofStep<C> convert(ProofStep<C> step) {
		return new DerivableFromProofStep<C>(step, statedAxioms_);
	}

}
