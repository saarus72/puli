package org.semanticweb.elk.proofs.adapters;

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


import java.util.List;
import java.util.Set;

import org.liveontologies.puli.JustifiedInference;
import org.semanticweb.elk.proofs.InferencePrinter;

public class DirectSatEncodingInference
		implements JustifiedInference<Integer, Integer> {

	private final Integer conclusion_;
	private final List<? extends Integer> premises_;
	private final Set<? extends Integer> justifications_;

	DirectSatEncodingInference(final Integer conclusion,
			final List<? extends Integer> premises,
			final Set<? extends Integer> justifications) {
		this.conclusion_ = conclusion;
		this.premises_ = premises;
		this.justifications_ = justifications;
	}

	@Override
	public Integer getConclusion() {
		return conclusion_;
	}

	@Override
	public List<? extends Integer> getPremises() {
		return premises_;
	}

	@Override
	public Set<? extends Integer> getJustification() {
		return justifications_;
	}

	@Override
	public String toString() {
		return InferencePrinter.toString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (conclusion_ == null ? 0 : conclusion_.hashCode());
		result = prime * result
				+ (justifications_ == null ? 0 : justifications_.hashCode());
		result = prime * result
				+ (premises_ == null ? 0 : premises_.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		final DirectSatEncodingInference other = (DirectSatEncodingInference) obj;

		return (conclusion_ == null ? other.conclusion_ == null
				: conclusion_.equals(other.conclusion_))
				&& (justifications_ == null ? other.justifications_ == null
						: justifications_.equals(other.justifications_))
				&& (premises_ == null ? other.premises_ == null
						: premises_.equals(other.premises_));
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

}
