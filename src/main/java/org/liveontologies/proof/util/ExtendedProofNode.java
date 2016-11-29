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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.liveontologies.proof.util.ConvertedProofNode;
import org.liveontologies.proof.util.ConvertedProofStep;
import org.liveontologies.proof.util.ExtendedProofStep;
import org.liveontologies.proof.util.Inference;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofStep;

class ExtendedProofNode<C> extends ConvertedProofNode<C>
		implements ProofStep<C> {

	private final Set<? extends C> statedAxioms_;

	ExtendedProofNode(ProofNode<C> delegate, Set<? extends C> statedAxioms) {
		super(delegate);
		this.statedAxioms_ = statedAxioms;
	}

	@Override
	public Collection<ProofStep<C>> getInferences() {
		Collection<ProofStep<C>> result = super.getInferences();
		if (statedAxioms_.contains(getMember())) {
			result.add(this);
		}
		return result;
	}

	@Override
	protected ConvertedProofStep<C> convert(ProofStep<C> step) {
		return new ExtendedProofStep<C>(step, statedAxioms_);
	}

	/** implementation of {@link ProofStep} */

	@Override
	public String getName() {
		return "Asserted Axiom";
	}

	@Override
	public ProofNode<C> getConclusion() {
		return this;
	}

	@Override
	public List<? extends ProofNode<C>> getPremises() {
		return Collections.emptyList();
	}

	@Override
	public Inference<C> getExample() {
		// this inference should not be displayed
		return null;
	}

}
