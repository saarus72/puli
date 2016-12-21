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

public class InferenceSetBuilder<C> {

	private static final String INF_NAME = "inf";

	BaseInferenceSet<C> inferenceSet_ = new BaseInferenceSet<C>();

	/**
	 * use {@link #create()}
	 */
	private InferenceSetBuilder() {
	}

	public static <C> InferenceSetBuilder<C> create() {
		return new InferenceSetBuilder<C>();
	}

	BaseInferenceSet<C> build() {
		return inferenceSet_;
	}

	ThisInferenceBuilder conclusion(C conclusion) {
		ThisInferenceBuilder result = new ThisInferenceBuilder(INF_NAME);
		result.conclusion(conclusion);
		return result;
	}

	class ThisInferenceBuilder extends InferenceBuilder<C> {

		protected ThisInferenceBuilder(String name) {
			super(name);
		}

		@Override
		ThisInferenceBuilder conclusion(C conclusion) {
			super.conclusion(conclusion);
			return this;
		}

		@Override
		ThisInferenceBuilder premise(C premise) {
			super.premise(premise);
			return this;
		}

		Inference<C> add() {
			Inference<C> inference = build();
			inferenceSet_.produce(inference);
			return inference;
		}

	}

}
