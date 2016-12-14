package org.liveontologies.proof.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomProof {

	public static ProofNode<Integer> generate(Random random, int maxConclusions,
			int maxPremises, int maxInferences) {
		List<Integer> derived = new ArrayList<Integer>(maxConclusions);
		Set<Integer> derivedSet = new HashSet<Integer>(maxConclusions);
		MockProof<Integer> proof = MockProof.create();
		for (int i = 0; i < maxInferences; i++) {
			int conclusion = random.nextInt(maxConclusions);
			MockProof<Integer>.MockProofStepBuilder b = proof
					.conclusion(conclusion);
			int noPremises = random.nextInt(maxPremises);
			if (derived.size() < noPremises) {
				noPremises = derived.size();
			}
			for (int j = 0; j < noPremises; j++) {
				int premise = derived.get(random.nextInt(derived.size()));
				b.premise(premise);
			}
			b.build();
			if (derivedSet.add(conclusion)) {
				derived.add(conclusion);
			}
		}
		// return the last derived
		return proof.getNode(derived.get(derived.size() - 1));
	}

	public static ProofNode<Integer> generate(Random random, int maxConclusions,
			int maxPremises) {
		return generate(random, maxConclusions, maxPremises,
				maxConclusions + random.nextInt(2 * maxConclusions));
	}

	public static ProofNode<Integer> generate(Random random,
			int maxConclusions) {
		return generate(random, maxConclusions, 3 + random.nextInt(5));
	}

	public static ProofNode<Integer> generate(Random random) {
		return generate(random, random.nextInt(100));
	}

	public static ProofNode<Integer> generate() {
		return generate(new Random());
	}

}
