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
import java.util.List;
import java.util.Set;

import org.liveontologies.puli.Delegator;
import org.liveontologies.puli.Inference;
import org.liveontologies.puli.JustifiedInference;

public class DummyJustifiedInference<C, A> extends Delegator<Inference<C>>
		implements JustifiedInference<C, A> {

	public DummyJustifiedInference(final Inference<C> delegate) {
		super(delegate);
	}

	@Override
	public String getName() {
		return getDelegate().getName();
	}

	@Override
	public C getConclusion() {
		return getDelegate().getConclusion();
	}

	@Override
	public List<? extends C> getPremises() {
		return getDelegate().getPremises();
	}

	@Override
	public Set<? extends A> getJustification() {
		return Collections.emptySet();
	}

}
