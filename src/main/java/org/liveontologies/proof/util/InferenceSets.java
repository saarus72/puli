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

public class InferenceSets {

	@SuppressWarnings("rawtypes")
	public static InferenceSet EMPTY_INFERENCE_SET = new InferenceSet() {

		@Override
		public Collection<? extends Inference> getInferences(
				Object conclusion) {
			return Collections.emptySet();
		}
	};

	@SuppressWarnings("unchecked")
	public static <C> InferenceSet<C> emptyInferenceSet() {
		return (InferenceSet<C>) EMPTY_INFERENCE_SET;

	}

}
