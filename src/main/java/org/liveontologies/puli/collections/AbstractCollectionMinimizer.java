package org.liveontologies.puli.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractCollectionMinimizer<T, C extends Collection<T>>
		extends AbstractCollection<C> implements CollectionMinimizer<T, C> {

	private final static short FILTER_SHIFT_ = 6; // 2^6 = 64 bits is good
	// enough
	private final static int FILTER_MASK_ = getMask(FILTER_SHIFT_);

	private final static long LONG_MASK_ = -1L; // all bits 1

	private final static short BUCKET_SHIFT_ = 5;

	private static int BUCKET_MASK_ = getMask(BUCKET_SHIFT_);

	static int TESTS_ = 0, POS_TESTS_ = 0; // statistics

	private final Object[] data_ = new Object[BUCKET_MASK_ + 1];

	private int size_ = 0;

	/**
	 * @param bits
	 * @return 11..1 bits times
	 */
	private static int getMask(short bits) {
		return (1 << bits) - 1;
	}

	private static long getFilter(Collection<?> s) {
		long result = 0;
		for (Object e : s) {
			result |= 1L << (e.hashCode() & FILTER_MASK_);
		}
		return result;
	}

	@Override
	public Iterable<C> getSubsets(final Collection<?> s) {
		return new Iterable<C>() {
			@Override
			public Iterator<C> iterator() {
				return new SubsetIterator<T, C>(s, getFilter(s), LONG_MASK_,
						data_);
			}
		};
	}

	@Override
	public boolean add(C s) {
		long filter = getFilter(s);
		if (add(s, filter, LONG_MASK_, data_)) {
			size_++;
			return true;
		}
		// else
		return false;
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Collection<?>) {
			Collection<?> s = (Collection<?>) o;
			long filter = getFilter(s);
			return contains(s, filter, LONG_MASK_, data_);
		} else {
			return false;
		}

	}

	@Override
	public void prune(Collection<?> s) {
		long filter = getFilter(s);
		prune(s, filter, LONG_MASK_, data_);
	}

	@SuppressWarnings("unchecked")
	boolean prune(Collection<?> s, long fragment, long mask, Object[] data) {
		long newMask = mask >>> BUCKET_SHIFT_;
		boolean dataEmptied = true;
		int maskedFragment = (int) (BUCKET_MASK_ & fragment);
		for (int i = 0; i <= (BUCKET_MASK_ & mask); i++) {
			if (data[i] == null) {
				continue;
			}
			if ((maskedFragment & i) != maskedFragment) {
				dataEmptied = false;
				continue;
			}
			if (newMask == 0L) {
				Iterator<C> iter = ((Collection<C>) data[i]).iterator();
				boolean allRemoved = true;
				while (iter.hasNext()) {
					C other = iter.next();
					if (other.containsAll(s)) {
						iter.remove();
						size_--;
					} else {
						allRemoved = false;
					}
				}
				if (allRemoved) {
					data[i] = null;
				} else {
					dataEmptied = false;
				}
			} else {
				if (prune(s, fragment >>> BUCKET_SHIFT_, newMask,
						(Object[]) data[i])) {
					data[i] = null;
				} else {
					dataEmptied = false;
				}
			}
		}
		return dataEmptied;

	}

	@SuppressWarnings("unchecked")
	boolean add(C s, long fragment, long mask, Object[] data) {
		int i = (int) (fragment & BUCKET_MASK_ & mask);
		mask >>>= BUCKET_SHIFT_;
		if (mask == 0L) {
			if (data[i] == null) {
				data[i] = create();
			}
			return ((Collection<C>) data[i]).add(s);
		} else {
			if (data[i] == null) {
				data[i] = new Object[(int) (BUCKET_MASK_ & mask) + 1];
			}
			return add(s, fragment >>> (BUCKET_SHIFT_), mask,
					(Object[]) data[i]);
		}
	}

	@SuppressWarnings("unchecked")
	boolean contains(Collection<?> s, long fragment, long mask, Object[] data) {
		int i = (int) (fragment & BUCKET_MASK_ & mask);
		mask >>>= BUCKET_SHIFT_;
		if (mask == 0L) {
			if (data[i] == null) {
				data[i] = create();
			}
			return ((Collection<C>) data[i]).contains(s);
		} else {
			if (data[i] == null) {
				data[i] = new Object[(int) (BUCKET_MASK_ & mask) + 1];
			}
			return contains(s, fragment >>> (BUCKET_SHIFT_), mask,
					(Object[]) data[i]);
		}
	}

	public static void printStatistics() {
		System.err.println(
				"Tests: " + TESTS_ + ", positive: " + POS_TESTS_ + (TESTS_ > 0
						? " (" + (POS_TESTS_ * 100) / TESTS_ + "%)" : ""));
	}

	abstract Collection<C> create();

	@Override
	public Iterator<C> iterator() {
		return new Iter<T, C>(LONG_MASK_, data_);
	}

	@Override
	public int size() {
		return size_;
	}

	private static class Iter<T, C extends Collection<T>>
			implements Iterator<C> {

		private long mask_;

		private final Object[] data_;

		private int pos = 0;

		private Iterator<C> iter_;

		Iter(long mask, Object[] data) {
			this.mask_ = mask;
			this.data_ = data;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean hasNext() {
			if (iter_ != null) {
				if (iter_.hasNext()) {
					return true;
				} else {
					pos++;
				}
			}
			while (pos < data_.length && data_[pos] == null) {
				pos++;
			}
			if (pos == data_.length) {
				iter_ = null;
				return false;
			}
			// else
			long newMask = mask_ >>> BUCKET_SHIFT_;
			if (newMask == 0L) {
				iter_ = ((Collection<C>) data_[pos]).iterator();
			} else {
				iter_ = new Iter<T, C>(newMask, (Object[]) data_[pos]);
			}
			assert (iter_.hasNext());
			return true;
		}

		@Override
		public C next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return iter_.next();
		}

	}

	private static class SubsetIterator<T, C extends Collection<T>>
			implements Iterator<C> {

		final Collection<?> s;

		final long nextFragment, nextMask;

		final int fragmentMask, complementMask;

		final Object[] data;

		private int pos = 0;

		private Iterator<C> iter_;

		SubsetIterator(Collection<?> s, long fragment, long mask,
				Object[] data) {
			this.s = s;
			this.nextMask = mask >>> BUCKET_SHIFT_;
			this.nextFragment = fragment >>> BUCKET_SHIFT_;
			this.fragmentMask = (int) (fragment & mask & BUCKET_MASK_);
			this.complementMask = ~fragmentMask;
			this.data = data;
			findNext();
		}

		int increment(int pos) {
			pos |= complementMask;
			pos++;
			pos &= fragmentMask;
			return pos;
		}

		@SuppressWarnings("unchecked")
		void findNext() {
			for (;;) {
				if (data[pos] != null) {
					if (nextMask == 0L) {
						iter_ = new SubsetFilteredIterator<C>(
								((Collection<C>) data[pos]).iterator(), s);
					} else {
						iter_ = new SubsetIterator<T, C>(s, nextFragment,
								nextMask, (Object[]) data[pos]);
					}
					if (iter_.hasNext()) {
						return;
					}
				}
				if (pos == fragmentMask) {
					iter_ = null;
					return;
				} else {
					pos = increment(pos);
				}
			}
		}

		@Override
		public boolean hasNext() {
			return iter_ != null;
		}

		@Override
		public C next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			C result = iter_.next();
			if (pos == fragmentMask) {
				iter_ = null;
			} else {
				pos = increment(pos);
				findNext();
			}
			return result;
		}

	}

}
