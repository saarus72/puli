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

import java.util.Collections;
import java.util.List;

/**
 * A special inference representing that a conclusion is derivable from no
 * premises; usually it means that the conclusion is preset in the initial set
 * from which other conclusions are derived.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 */
public class AssertedConclusionInference<C> implements Inference<C> {

	public static String NAME = "Asserted Conclusion";

	private final C conclusion_;

	public AssertedConclusionInference(C conclusion) {
		this.conclusion_ = conclusion;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public C getConclusion() {
		return conclusion_;
	}

	@Override
	public List<? extends C> getPremises() {
		return Collections.emptyList();
	}

}
