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

import java.util.List;

public class BaseInference<C> implements Inference<C> {

	private final String name_;

	private final C conclusion_;

	private final List<? extends C> premises_;

	public BaseInference(String name, C conclusion,
			List<? extends C> premises) {
		Util.checkNotNull(name);
		Util.checkNotNull(conclusion);
		Util.checkNotNull(premises);
		this.name_ = name;
		this.conclusion_ = conclusion;
		this.premises_ = premises;
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof BaseInference<?>) {
			BaseInference<?> other = (BaseInference<?>) o;
			return (name_.equals(other.name_)
					&& conclusion_.equals(other.conclusion_)
					&& premises_.equals(other.premises_));
		}
		// else
		return false;
	}

	@Override
	public int hashCode() {
		return BaseInference.class.hashCode() + name_.hashCode()
				+ conclusion_.hashCode() + premises_.hashCode();
	}

	@Override
	public String toString() {
		return conclusion_ + " -| " + premises_ + " by " + name_;
	}

}
