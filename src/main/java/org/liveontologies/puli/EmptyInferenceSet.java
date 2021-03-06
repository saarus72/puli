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

import java.util.Collection;
import java.util.Collections;

class EmptyInferenceSet<C, I extends Inference<C>>
		implements GenericDynamicInferenceSet<C, I> {

	@Override
	public Collection<? extends I> getInferences(Object conclusion) {
		return Collections.emptySet();
	}

	@Override
	public void addListener(ChangeListener listener) {
		// the set never changes
	}

	@Override
	public void removeListener(ChangeListener listener) {
		// the set never changes
	}

	@Override
	public void dispose() {
		// no-op
	}

}
