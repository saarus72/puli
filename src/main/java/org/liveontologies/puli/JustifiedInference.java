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
package org.liveontologies.puli;

import java.util.Set;

/**
 * An inference that is associated with a set of axioms that justify this
 * inference. This could be, for example, axioms occurring in the ontology.
 * 
 * @author Peter Skocovsky
 *
 * @param <C>
 *            the type of conclusions and premises this inference operate with
 * @param <A>
 *            the type of axioms this inference operates with
 */
public interface JustifiedInference<C, A> extends Inference<C> {

	/**
	 * @return the axioms by which this inference is justified; the axioms can
	 *         be different from premises in the sense that may not be derived
	 */
	Set<? extends A> getJustification();

}
