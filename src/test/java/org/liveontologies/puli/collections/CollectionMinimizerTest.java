package org.liveontologies.puli.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectionMinimizerTest {

	private final static Logger LOGGER_ = LoggerFactory
			.getLogger(CollectionMinimizerTest.class);

	@Test
	public void testListMinimizer() {
		long seed = new Random().nextLong();
		LOGGER_.debug("seed: {}", seed);
		Random rnd = new Random(seed);
		ArrayListCollectionMinimizer<Integer, Set<Integer>> tested = new ArrayListCollectionMinimizer<Integer, Set<Integer>>();
		MockListCollectionMinimizer<Integer, Set<Integer>> expected = new MockListCollectionMinimizer<Integer, Set<Integer>>();
		for (int i = 0; i < 1000; i++) {
			Set<Integer> next = getRandomSet(rnd, 5 + rnd.nextInt(5), 20);
			LOGGER_.debug("merging: {}", next);
			for (Set<Integer> subset : tested.getSubsets(next)) {
				LOGGER_.debug("contains a subset: {}", subset);
				assertTrue(next.containsAll(subset));
				assertTrue(expected.contains(subset));
				assertTrue(tested.contains(subset));
			}
			for (Set<Integer> subset : expected.getSubsets(next)) {
				LOGGER_.debug("contains a subset: {}", subset);
				assertTrue(next.containsAll(subset));
				assertTrue(expected.contains(subset));
				assertTrue(tested.contains(subset));
			}
		}
		assertEquals(expected.size(), tested.size());
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
