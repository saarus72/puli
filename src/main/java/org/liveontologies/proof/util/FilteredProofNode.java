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
import java.util.Set;

import org.liveontologies.proof.util.ConvertedProofNode;
import org.liveontologies.proof.util.FilteredProofNode;
import org.liveontologies.proof.util.FilteredProofStep;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FilteredProofNode<C> extends ConvertedProofNode<C> {

	// logger for this class
	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(FilteredProofNode.class);

	private final Set<? extends ProofNode<C>> forbidden_;

	FilteredProofNode(ProofNode<C> delegate,
			Set<? extends ProofNode<C>> forbidden) {
		super(delegate);
		this.forbidden_ = forbidden;
	}

	@Override
	public Collection<ProofStep<C>> getInferences() {
		Collection<ProofStep<C>> result = new ArrayList<ProofStep<C>>();
		inference_loop: for (ProofStep<C> inf : getDelegate().getInferences()) {
			for (ProofNode<C> premise : inf.getPremises()) {
				if (forbidden_.contains(premise)) {
					LOGGER_.trace("{}: ignored: {} is forbiden", inf, premise);
					continue inference_loop;
				}
			}
			result.add(convert(inf));
		}
		return result;
	}

	@Override
	protected FilteredProofStep<C> convert(ProofStep<C> inf) {
		return new FilteredProofStep<C>(inf, forbidden_);
	}

}
