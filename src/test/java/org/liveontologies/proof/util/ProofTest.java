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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.liveontologies.proof.util.ProofNode;
import org.liveontologies.proof.util.ProofNodes;

/**
 * @author Pavel Klinov
 *
 *         pavel.klinov@uni-ulm.de
 * 
 * @author Yevgeny Kazakov
 */
public class ProofTest {

	@Test
	public void blockCyclicProof() throws Exception {
		MockProof<String> proof = MockProof.create();
		proof.conclusion("A ⊑ B").premise("A ⊑ B ⊓ C");
		proof.conclusion("A ⊑ B").premise("A ⊑ C").premise("C ⊑ B");
		proof.conclusion("A ⊑ C").premise("A ⊑ D").premise("D ⊑ C");
		proof.conclusion("A ⊑ D").premise("A ⊑ B").premise("B ⊑ D");

		Set<String> stated = new HashSet<String>(
				Arrays.asList("A ⊑ B ⊓ C", "B ⊑ D", "D ⊑ C", "C ⊑ B"));

		assertTrue(ProofNodes.isDerivable(proof.getNode("A ⊑ C"), stated));

		ProofNode<String> root = ProofNodes
				.addStatedAxioms(proof.getNode("A ⊑ B"), stated);

		assertTrue(ProofNodes.isDerivable(root));

		assertEquals(2, ProofNodes.eliminateNotDerivable(root)
				.getInferences().size());

		// only one inference remains since the other is cyclic
		assertEquals(1, ProofNodes.eliminateNotDerivableAndCycles(root)
				.getInferences().size());

		// testing the same but using derivability "from" methods

		root = proof.getNode("A ⊑ B");
		assertTrue(ProofNodes.isDerivable(root, stated));

		assertEquals(2, ProofNodes.eliminateNotDerivable(root, stated)
				.getInferences().size());

		// only one inference remains since the other is cyclic
		assertEquals(1,
				ProofNodes.eliminateNotDerivableAndCycles(root, stated)
						.getInferences().size());

	}

	@Test
	public void blockCyclicProof2() throws Exception {
		MockProof<Integer> proof = MockProof.create();
		proof.conclusion(0).premise(1).premise(2);
		proof.conclusion(0).premise(3).premise(4);
		proof.conclusion(2).premise(0).premise(0);

		Set<Integer> stated = new HashSet<Integer>(Arrays.asList(1, 3, 4));

		ProofNode<Integer> root = ProofNodes
				.addStatedAxioms(proof.getNode(0), stated);

		assertTrue(ProofNodes.isDerivable(root));

		// everything is derivable
		assertEquals(2, ProofNodes.eliminateNotDerivable(root)
				.getInferences().size());

		// only one inference remains since the other is cyclic
		assertEquals(1, ProofNodes.eliminateNotDerivableAndCycles(root)
				.getInferences().size());

		// the same using derivability "from"

		root = proof.getNode(0);

		assertTrue(ProofNodes.isDerivable(root, stated));

		assertEquals(2, ProofNodes.eliminateNotDerivable(root, stated)
				.getInferences().size());

		// only one inference remains since the other is cyclic
		assertEquals(1,
				ProofNodes.eliminateNotDerivableAndCycles(root, stated)
						.getInferences().size());

	}

	@Test
	public void recursiveBlocking() throws Exception {
		MockProof<Integer> proof = MockProof.create();
		proof.conclusion(0).premise(1).premise(2);
		proof.conclusion(1).premise(3).premise(4).premise(5);
		proof.conclusion(3).premise(6).premise(7);
		proof.conclusion(4).premise(8).premise(9);

		Set<Integer> stated = new HashSet<Integer>(
				Arrays.asList(2, 5, 6, 7, 8, 9));

		ProofNode<Integer> root = ProofNodes
				.addStatedAxioms(proof.getNode(0), stated);

		assertEquals(1, ProofNodes.eliminateNotDerivable(root)
				.getInferences().size());

		stated.remove(6);

		// not derivable anymore
		assertEquals(null, ProofNodes.eliminateNotDerivable(root));

		// the same using "from" methods

		stated.add(6);

		root = proof.getNode(0);

		assertEquals(1, ProofNodes.eliminateNotDerivable(root, stated)
				.getInferences().size());

		stated.remove(6);

		// not derivable anymore
		assertEquals(null, ProofNodes.eliminateNotDerivable(root, stated));
	}

}
