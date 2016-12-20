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

class BaseProofNode<C> implements ProofNode<C> {

	private final InferenceSet<C> inferenceSet_;

	private final C member_;

	private Collection<ProofStep<C>> steps_ = null;

	BaseProofNode(InferenceSet<C> inferences, C member) {
		Util.checkNotNull(inferences);
		Util.checkNotNull(member);
		this.inferenceSet_ = inferences;
		this.member_ = member;
	}

	public InferenceSet<C> getInferenceSet() {
		return inferenceSet_;
	}

	@Override
	public C getMember() {
		return member_;
	}

	@Override
	public Collection<? extends ProofStep<C>> getInferences() {
		if (steps_ == null) {
			Collection<? extends Inference<C>> original = inferenceSet_
					.getInferences(member_);
			steps_ = new ArrayList<ProofStep<C>>(original.size());
			for (Inference<C> inf : original) {
				convert(inf);
			}
		}
		return steps_;
	}

	void convert(Inference<C> inf) {
		steps_.add(new BaseProofStep<C>(inferenceSet_, inf));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BaseProofNode) {
			BaseProofNode<?> other = (BaseProofNode<?>) o;
			return member_.equals(other.member_)
					&& inferenceSet_.equals(other.inferenceSet_);
		}
		// else
		return false;
	}

	@Override
	public int hashCode() {
		return BaseProofNode.class.hashCode() + member_.hashCode()
				+ inferenceSet_.hashCode();
	}

	@Override
	public String toString() {
		return member_.toString();
	}

}
