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

import java.util.List;

public class DelegatingProofStep<C> extends Delegator<ProofStep<C>>
		implements ProofStep<C> {

	protected DelegatingProofStep(ProofStep<C> delegate) {
		super(delegate);
	}

	@Override
	public String getName() {
		return getDelegate().getName();
	}

	@Override
	public ProofNode<C> getConclusion() {
		return getDelegate().getConclusion();
	}

	@Override
	public List<? extends ProofNode<C>> getPremises() {
		return getDelegate().getPremises();
	}

	@Override
	public Inference<C> getInference() {
		return getDelegate().getInference();
	}

}
