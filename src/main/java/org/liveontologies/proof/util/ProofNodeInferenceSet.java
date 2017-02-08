/*-
 * #%L
 * Proof Utility Library
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2017 Live Ontologies Project
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
package org.liveontologies.proof.util;

import java.util.Collection;

import org.liveontologies.proof.util.InferenceSet;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofNodeInferenceSet;
import org.liveontologies.proof.util.ProofStep;

public class ProofNodeInferenceSet<C> implements InferenceSet<ProofNode<C>> {

	@SuppressWarnings("rawtypes")
	private final static ProofNodeInferenceSet INSTANCE_ = new ProofNodeInferenceSet();

	@SuppressWarnings("unchecked")
	public static <C> ProofNodeInferenceSet<C> get() {
		return INSTANCE_;
	}

	@Override
	public Collection<? extends ProofStep<C>> getInferences(
			ProofNode<C> conclusion) {
		return conclusion.getInferences();
	}

}
