package org.liveontologies.proof.util;

import org.liveontologies.proof.util.Inference;
import org.liveontologies.proof.util.ProofNode;

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

/**
 * Represents an inference step in which a conclusion represented by a proof
 * node is obtained from premises represented by other proof nodes.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 *            the type of conclusions and premises this inference operate with
 */
public interface ProofStep<C> extends Inference<ProofNode<C>> {

	/**
	 * @return an example of an inference used in this step, which can be used
	 *         for explanation purpose. Usually it is an inference instantiated
	 *         with some generic parameters. If {@code null}, no example is
	 *         provided.
	 */
	Inference<C> getExample();

}
