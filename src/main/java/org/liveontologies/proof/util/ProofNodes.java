/**
 * 
 */
package org.liveontologies.proof.util;

import java.util.HashSet;
import java.util.Set;

import org.liveontologies.proof.util.AcyclicDerivableFromProofNode;
import org.liveontologies.proof.util.AcyclicDerivableProofNode;
import org.liveontologies.proof.util.CachingProofNode;
import org.liveontologies.proof.util.DerivableFromProofNode;
import org.liveontologies.proof.util.DerivableProofNode;
import org.liveontologies.proof.util.ExtendedProofNode;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofNodeDerivabilityChecker;
import org.liveontologies.proof.util.ProofStep;

/*
 * #%L
 * OWL API Proofs Model
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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
 * A collection of static utilities for manipulation with {@link ProofNode}
 * 
 * @author Pavel Klinov
 *
 *         pavel.klinov@uni-ulm.de
 * 
 * @author Yevgeny Kazakov
 *
 */
public class ProofNodes {

	public static <C> ProofNode<C> addStatedAxioms(ProofNode<C> node,
			Set<C> statedAxioms) {
		return new ExtendedProofNode<C>(node, statedAxioms);
	}

	public static <C> ProofNode<C> eliminateNotDerivable(ProofNode<C> node) {
		if (isDerivable(node)) {
			return new DerivableProofNode<C>(node);
		}
		// else
		return null;
	}

	public static <C> ProofNode<C> eliminateNotDerivable(ProofNode<C> node,
			Set<C> statedAxioms) {
		if (isDerivable(node, statedAxioms)) {
			return new DerivableFromProofNode<C>(node, statedAxioms);
		}
		// else
		return null;
	}

	public static <C> ProofNode<C> eliminateNotDerivableAndCycles(
			ProofNode<C> node) {
		if (isDerivable(node)) {
			return new AcyclicDerivableProofNode<C>(node);
		}
		// else
		return null;
	}

	public static <C> ProofNode<C> eliminateNotDerivableAndCycles(
			ProofNode<C> node, Set<C> statedAxioms) {
		if (isDerivable(node, statedAxioms)) {
			return new AcyclicDerivableFromProofNode<C>(node, statedAxioms);
		}
		// else
		return null;
	}

	public static <C> ProofNode<C> cache(ProofNode<C> node) {
		return new CachingProofNode<C>(node);
	}

	public static <C> boolean isDerivable(ProofNode<C> node) {
		return new ProofNodeDerivabilityChecker<C>().isDerivable(node);
	}

	public static <C> boolean isDerivable(ProofNode<C> node,
			Set<C> statedAxioms) {
		return new ProofNodeDerivabilityChecker<C>()
				.isDerivable(new ExtendedProofNode<C>(node, statedAxioms));
	}

	public static <C> String print(ProofNode<C> node) {
		StringBuilder builder = new StringBuilder();
		Set<ProofNode<C>> done = new HashSet<ProofNode<C>>();

		print(node, builder, done, 0);

		return builder.toString();
	}

	private static <C> void print(ProofNode<C> node, StringBuilder builder,
			Set<ProofNode<C>> done, int depth) {
		for (int i = 0; i < depth; i++) {
			builder.append("   ");
		}

		builder.append(node);

		if (done.add(node)) {
			builder.append('\n');
		} else {
			builder.append("*\n");
			return;
		}

		for (ProofStep<C> inf : node.getInferences()) {
			for (int i = 0; i < depth + 1; i++) {
				builder.append("   ");
			}

			builder.append(inf.getName()).append('\n');

			for (ProofNode<C> premise : inf.getPremises()) {
				print(premise, builder, done, depth + 2);
			}
		}
	}

}
