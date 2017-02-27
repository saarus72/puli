package org.liveontologies.puli.collections;

import java.util.Collection;

/**
 * A collection that supports efficient subset checks for collections it
 * contains.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <T>
 *            the type of elements in the collections
 * @param <C>
 *            the type of collections that can be used with this
 *            {@link CollectionMinimizer}
 */
public interface CollectionMinimizer<T, C extends Collection<T>>
		extends Collection<C> {

	/**
	 * @param s
	 * @return a view of subsets of the given set contained in this
	 *         {@link CollectionMinimizer}
	 */
	public Iterable<C> getSubsets(Collection<?> s);

	/**
	 * Removes all supersets of the given set
	 * 
	 * @param s
	 */
	public void prune(Collection<?> s);

}
