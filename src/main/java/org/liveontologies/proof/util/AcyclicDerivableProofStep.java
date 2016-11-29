package org.liveontologies.proof.util;

import org.liveontologies.proof.util.AcyclicDerivableProofNode;
import org.liveontologies.proof.util.AcyclicProofStep;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofStep;

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

class AcyclicDerivableProofStep<C> extends AcyclicProofStep<C> {

	AcyclicDerivableProofStep(ProofStep<C> delegate,
			AcyclicDerivableProofNode<C> conclusion) {
		super(delegate, conclusion);
	}

	@Override
	protected AcyclicDerivableProofNode<C> convert(ProofNode<C> premise) {
		return new AcyclicDerivableProofNode<C>(premise,
				(AcyclicDerivableProofNode<C>) getConclusion());
	}

}
