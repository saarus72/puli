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

import org.liveontologies.proof.util.ConvertedProofStep;
import org.liveontologies.proof.util.DelegatingProofNode;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofStep;

abstract class ConvertedProofNode<C> extends DelegatingProofNode<C> {

	protected ConvertedProofNode(ProofNode<C> delegate) {
		super(delegate);
	}

	@Override
	public Collection<ProofStep<C>> getInferences() {
		Collection<? extends ProofStep<C>> original = super.getInferences();
		Collection<ProofStep<C>> result = new ArrayList<ProofStep<C>>(
				original.size());
		for (ProofStep<C> step : original) {
			result.add(convert(step));
		}
		return result;
	}

	protected abstract ConvertedProofStep<C> convert(ProofStep<C> step);

}
