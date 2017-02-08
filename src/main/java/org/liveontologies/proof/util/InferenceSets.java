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

import java.util.Set;

public class InferenceSets {

	@SuppressWarnings("rawtypes")
	public static DynamicInferenceSet EMPTY_INFERENCE_SET = new EmptyInferenceSet();

	@SuppressWarnings("unchecked")
	public static <C> DynamicInferenceSet<C> emptyInferenceSet() {
		return (DynamicInferenceSet<C>) EMPTY_INFERENCE_SET;
	}

	public static <C> boolean isDerivable(InferenceSet<C> inferenceSet,
			C conclusion) {
		return ProofNodes
				.isDerivable(ProofNodes.create(inferenceSet, conclusion));
	}

	public static <C> boolean isDerivable(InferenceSet<C> inferenceSet,
			C conclusion, Set<C> statedAxioms) {
		return ProofNodes.isDerivable(
				ProofNodes.create(inferenceSet, conclusion), statedAxioms);
	}

}
