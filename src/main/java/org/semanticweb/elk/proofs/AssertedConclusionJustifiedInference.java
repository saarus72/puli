package org.semanticweb.elk.proofs;

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


import java.util.Collections;
import java.util.Set;

import org.liveontologies.puli.AssertedConclusionInference;
import org.liveontologies.puli.JustifiedInference;

public abstract class AssertedConclusionJustifiedInference<C, A> extends
		AssertedConclusionInference<C> implements JustifiedInference<C, A> {

	public AssertedConclusionJustifiedInference(final C conclusion) {
		super(conclusion);
	}

	@Override
	public Set<? extends A> getJustification() {
		return Collections.singleton(conclusionToAxiom(getConclusion()));
	}

	protected abstract A conclusionToAxiom(C conclusion);

	public static class Projection<C>
			extends AssertedConclusionJustifiedInference<C, C> {

		public Projection(final C conclusion) {
			super(conclusion);
		}

		@Override
		protected C conclusionToAxiom(final C conclusion) {
			return conclusion;
		}

	}

}
