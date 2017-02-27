package org.liveontologies.puli.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MockListCollectionMinimizer<T, C extends Collection<T>>
		extends ArrayList<C> implements CollectionMinimizer<T, C> {

	private static final long serialVersionUID = -359260665565273216L;

	@Override
	public Iterable<C> getSubsets(final Collection<?> s) {
		return new Iterable<C>() {
			@Override
			public Iterator<C> iterator() {
				return new SubsetFilteredIterator<C>(
						MockListCollectionMinimizer.this.iterator(), s);
			}
		};
	}

	@Override
	public void prune(Collection<?> s) {
		Iterator<C> iter = iterator();
		while (iter.hasNext()) {
			Collection<T> next = iter.next();
			if (next.containsAll(s)) {
				iter.remove();
			}
		}
	}

}
