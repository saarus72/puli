package org.liveontologies.puli.collections;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Collection2Test {

	private final static Logger LOGGER_ = LoggerFactory
			.getLogger(Collection2Test.class);

	@Test
	public void testListMinimizer() {
		long seed = new Random().nextLong();
		LOGGER_.debug("seed: {}", seed);
		Random rnd = new Random(seed);
		ArrayListCollection2<Set<Integer>> tested = new ArrayListCollection2<Set<Integer>>();
		MockListCollection2<Set<Integer>> expected = new MockListCollection2<Set<Integer>>();
		for (int i = 0; i < 1000; i++) {
			Set<Integer> next = getRandomSet(rnd, 5 + rnd.nextInt(5), 20);
			LOGGER_.debug("new set: {}", next);
			// remove either all subsets or all supersets of the new set
			boolean pruneSubsets = rnd.nextBoolean();
			Iterator<Set<Integer>> expectedIter, testedIter;
			if (pruneSubsets) {
				expectedIter = expected.subCollectionsOf(next).iterator();
				testedIter = tested.subCollectionsOf(next).iterator();
			} else {
				expectedIter = expected.superCollectionsOf(next).iterator();
				testedIter = tested.superCollectionsOf(next).iterator();
			}
			int expectedPruned = 0;
			while (expectedIter.hasNext()) {
				Set<Integer> pruned = expectedIter.next();
				expectedPruned++;
				if (pruneSubsets) {
					LOGGER_.debug("expected subset: {}", pruned);
					assertTrue(next.containsAll(pruned));
				} else {
					LOGGER_.debug("expected superset: {}", pruned);
					assertTrue(pruned.containsAll(next));
				}
				assertTrue(expected.contains(pruned));
				expectedIter.remove();
				assertFalse(expected.contains(pruned));
			}
			int testedPruned = 0;
			while (testedIter.hasNext()) {
				Set<Integer> pruned = testedIter.next();
				testedPruned++;
				if (pruneSubsets) {
					LOGGER_.debug("tested subset: {}", pruned);
					assertTrue(next.containsAll(pruned));
				} else {
					LOGGER_.debug("tested superset: {}", pruned);
					assertTrue(pruned.containsAll(next));
				}
				assertTrue(tested.contains(pruned));
				testedIter.remove();
				assertFalse(tested.contains(pruned));
			}
			assertEquals(expectedPruned, testedPruned);
			// add only if nothing is pruned
			if (expectedPruned == 0) {
				assertEquals(expected.add(next), tested.add(next));
			}
		}
		assertEquals(expected.size(), tested.size());
		// verify that the result has the same elements
		int count = 0;
		for (Set<Integer> s : tested) {
			count++;
			assertTrue(tested.contains(s));
		}
		assertEquals(expected.size(), count);
	}

	Set<Integer> getRandomSet(Random rnd, int size, int maxValue) {
		Set<Integer> result = new HashSet<Integer>();
		for (int i = 0; i < size; i++) {
			int next = rnd.nextInt(maxValue);
			result.add(next);
		}
		return result;
	}

}
