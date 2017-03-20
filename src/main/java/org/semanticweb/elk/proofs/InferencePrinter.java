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

import java.util.Collection;

import org.liveontologies.puli.JustifiedInference;

/**
 * Static methods for printing inferences
 * 
 * @author Yevgeny Kazakov
 */
public class InferencePrinter {

	public static <C, A> String toString(JustifiedInference<C, A> inference) {
		String result = inference.getConclusion() + " -| ";
		boolean first = true;
		for (C premise : inference.getPremises()) {
			if (!first) {
				result += "; ";
			} else {
				first = false;
			}
			result += premise;
		}
		Collection<? extends A> axioms = inference.getJustification();
		if (axioms.isEmpty()) {
			return result;
		}
		// else
		result += " [";
		first = true;
		for (A axiom : inference.getJustification()) {
			if (!first) {
				result += "; ";
			} else {
				first = false;
			}
			result += axiom;
		}
		result += "]";
		return result;
	}

}
