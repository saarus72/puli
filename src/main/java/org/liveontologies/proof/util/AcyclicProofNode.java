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

import java.util.HashSet;
import java.util.Set;

import org.liveontologies.proof.util.AcyclicProofNode;
import org.liveontologies.proof.util.ConvertedProofNode;
import org.liveontologies.proof.util.ProofNode;

abstract class AcyclicProofNode<C> extends ConvertedProofNode<C> {

	private final AcyclicProofNode<C> parent_;

	AcyclicProofNode(ProofNode<C> delegate, AcyclicProofNode<C> parent) {
		super(delegate);
		this.parent_ = parent;
	}

	AcyclicProofNode(ProofNode<C> delegate) {
		this(delegate, null);
	}

	/**
	 * @param node
	 * @return the original nodes which should not be used as premises of
	 *         inferences
	 */
	Set<ProofNode<C>> getBlockedNodes() {
		Set<ProofNode<C>> result = new HashSet<ProofNode<C>>();
		AcyclicProofNode<C> node = this;
		do {
			result.add(node.getDelegate());
			node = node.parent_;
		} while (node != null);
		return result;
	}

}
