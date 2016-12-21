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

public class DelegatingDynamicInferenceSet<C, S extends DynamicInferenceSet<C>>
		extends DelegatingInferenceSet<C, S> implements DynamicInferenceSet<C> {

	DelegatingDynamicInferenceSet(S delegate) {
		super(delegate);
	}

	@Override
	public void addListener(DynamicInferenceSet.ChangeListener listener) {
		getDelegate().addListener(listener);
	}

	@Override
	public void removeListener(DynamicInferenceSet.ChangeListener listener) {
		getDelegate().removeListener(listener);
	}

	@Override
	public void dispose() {
		getDelegate().dispose();
	}

}
