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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yevgeny Kazakov
 *
 * @param <C>
 *            the type of conclusions and premises this inference operate with
 *
 */
public class MockInference<C> implements Inference<C> {

	private final String name_;

	private final C conclusion_;

	private final List<C> premises_;

	public static <C> MockInference<C> create(String name, C conclusion,
			List<C> premises) {
		return new MockInference<C>(name, conclusion, premises);
	}

	public static <C> MockInference<C> create(String name, C conclusion) {
		return new MockInference<C>(name, conclusion, new ArrayList<C>());
	}

	private MockInference(String name, C conclusion, List<C> premises) {
		name_ = name;
		conclusion_ = conclusion;
		premises_ = premises;
	}

	@Override
	public String getName() {
		return name_;
	}

	@Override
	public C getConclusion() {
		return conclusion_;
	}

	@Override
	public List<? extends C> getPremises() {
		return premises_;
	}

}
