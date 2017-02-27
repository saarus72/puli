package org.liveontologies.puli.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SubsetFilteredIterator<C extends Collection<?>>
		implements Iterator<C> {

	private final Iterator<C> delegate_;

	private final Collection<?> s_;

	private C next_;

	SubsetFilteredIterator(Iterator<C> delegate, Collection<?> s) {
		this.delegate_ = delegate;
		this.s_ = s;
		findNext();
	}

	boolean include(C element) {
		return s_.containsAll(element);
	}

	void findNext() {
		while (delegate_.hasNext()) {
			next_ = delegate_.next();
			if (include(next_)) {
				return;
			}
		}
		// not found
		next_ = null;
	}

	@Override
	public boolean hasNext() {
		return next_ != null;
	}

	@Override
	public C next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		C result = next_;
		findNext();
		return result;
	}

}