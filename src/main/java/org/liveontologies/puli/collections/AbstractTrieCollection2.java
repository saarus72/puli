package org.liveontologies.puli.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractTrieCollection2<C extends Collection<?>>
		extends AbstractCollection2<C> implements Collection2<C> {

	private final static Iterator<?> EMPTY_ITERATOR_ = Collections.EMPTY_LIST
			.iterator();

	private final static short FILTER_SHIFT_ = 6; // 2^6 = 64 bits is good
	// enough
	private final static int FILTER_MASK_ = getMask(FILTER_SHIFT_);

	private final static long LONG_MASK_ = -1L; // all bits 1

	private final static short BUCKET_SHIFT_ = 6;

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
	public Iterable<C> subCollectionsOf(final Collection<?> s) {
		return new Iterable<C>() {
			@Override
			public Iterator<C> iterator() {
				return new SubIterator(s);
			}
		};
	}

	@Override
	public Iterable<C> superCollectionsOf(final Collection<?> s) {
		return new Iterable<C>() {
			@Override
			public Iterator<C> iterator() {
				return new SuperIterator(s);
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

	@SuppressWarnings("unchecked")
	boolean add(C s, long fragment, long mask, Object[] data) {
		int i = (int) (fragment & BUCKET_MASK_ & mask);
		mask >>>= BUCKET_SHIFT_;
		fragment >>>= BUCKET_SHIFT_;
		if (mask == 0L) {
			if (data[i] == null) {
				data[i] = create();
			}
			return ((Collection<C>) data[i]).add(s);
		} else {
			if (data[i] == null) {
				data[i] = new Object[(int) (BUCKET_MASK_ & mask) + 1];
			}
			return add(s, fragment, mask, (Object[]) data[i]);
		}
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

	@SuppressWarnings("unchecked")
	boolean contains(Collection<?> s, long fragment, long mask, Object[] data) {
		int i = (int) (fragment & BUCKET_MASK_ & mask);
		mask >>>= BUCKET_SHIFT_;
		fragment >>>= BUCKET_SHIFT_;
		if (mask == 0L) {
			if (data[i] == null) {
				data[i] = create();
			}
			return ((Collection<C>) data[i]).contains(s);
		} else {
			if (data[i] == null) {
				data[i] = new Object[(int) (BUCKET_MASK_ & mask) + 1];
			}
			return contains(s, fragment, mask, (Object[]) data[i]);
		}
	}

	@Override
	public boolean isMinimal(Collection<?> s) {
		long filter = getFilter(s);
		return isMinimal(s, filter, LONG_MASK_, data_);
	}

	@SuppressWarnings("unchecked")
	boolean isMinimal(Collection<?> s, long fragment, long mask,
			Object[] data) {
		int fragmentMask = (int) (fragment & mask & BUCKET_MASK_);
		mask >>>= BUCKET_SHIFT_;
		fragment >>>= BUCKET_SHIFT_;
		int pos = 0;
		for (;;) {
			Object val = data[pos];
			if (val != null) {
				if (mask == 0L) {
					for (C other : (Collection<C>) val) {
						if (s.containsAll(other)) {
							return false;
						}
					}
				} else if (!isMinimal(s, fragment, mask, (Object[]) val)) {
					return false;
				}
			}
			if (pos == fragmentMask) {
				// no subset is found
				return true;
			}
			pos |= ~fragmentMask;
			pos++;
			pos &= fragmentMask;
		}
	}

	@Override
	public boolean isMaximal(Collection<?> s) {
		long filter = getFilter(s);
		return isMaximal(s, filter, LONG_MASK_, data_);
	}

	@SuppressWarnings("unchecked")
	boolean isMaximal(Collection<?> s, long fragment, long mask,
			Object[] data) {
		int fragmentMask = (int) (fragment & mask & BUCKET_MASK_);
		int pos = (int) (mask & BUCKET_MASK_);
		mask >>>= BUCKET_SHIFT_;
		fragment >>>= BUCKET_SHIFT_;
		for (;;) {
			Object val = data[pos];
			if (val != null) {
				if (mask == 0L) {
					for (C other : (Collection<C>) val) {
						if (other.containsAll(s)) {
							return false;
						}
					}
				} else if (!isMaximal(s, fragment, mask, (Object[]) val)) {
					return false;
				}
			}
			if (pos == fragmentMask) {
				// no superset is found
				return true;
			}
			pos &= ~fragmentMask;
			pos--;
			pos |= fragmentMask;
		}
	}

	@Override
	public Iterator<C> iterator() {
		return new BaseIterator(LONG_MASK_, data_);
	}

	@Override
	public int size() {
		return size_;
	}

	@Override
	public void clear() {
		Arrays.fill(data_, null);
		size_ = 0;
	}

	public static void printStatistics() {
		System.err.println(
				"Tests: " + TESTS_ + ", positive: " + POS_TESTS_ + (TESTS_ > 0
						? " (" + (POS_TESTS_ * 100) / TESTS_ + "%)" : ""));
	}

	protected abstract Collection<C> create();

	/**
	 * Iterator over all collections
	 * 
	 * @author Yevgeny Kazakov
	 */
	private class BaseIterator implements Iterator<C> {

		final long nextMask;

		final Object[] data;

		int pos = 0;

		@SuppressWarnings("unchecked")
		Iterator<C> iter = (Iterator<C>) EMPTY_ITERATOR_;

		/**
		 * {@code true} if the last element returned by this iterator is the
		 * last element returned by {@link #iter}; this is needed to implement
		 * {@link #remove()}
		 */
		boolean iterInSync = true;

		BaseIterator(long mask, Object[] data) {
			this.nextMask = mask >>> BUCKET_SHIFT_;
			this.data = data;
		}

		void advancePos() {
			pos++;
		}

		boolean noMorePos() {
			return pos == data.length;
		}

		Iterator<C> getRecursiveIterator(Object val) {
			return new BaseIterator(nextMask, (Object[]) val);
		}

		@SuppressWarnings("unchecked")
		Iterator<C> getLeafIterator(Object val) {
			return ((Collection<C>) val).iterator();
		}

		@Override
		public void remove() {
			if (iterInSync) {
				iter.remove();
			} else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public boolean hasNext() {
			for (;;) {
				if (iter.hasNext()) {
					return true;
				}
				for (;;) {
					if (noMorePos()) {
						return false;
					}
					Object val = data[pos];
					advancePos();
					if (val == null) {
						continue;
					}
					// else
					iterInSync = false;
					iter = nextMask == 0L
							? new SynchSizeIterator(getLeafIterator(val))
							: getRecursiveIterator(val);
					break;
				}
			}
		}

		@Override
		public C next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			C result = iter.next();
			iterInSync = true;
			return result;
		}

	}

	/**
	 * Iterator over sub-collections of a given collection
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	private class SubIterator extends BaseIterator {

		final Condition<? super C> condition;

		final long nextFragment;

		final int fragmentMask;

		boolean noMorePos = false;

		SubIterator(Collection<?> s) {
			this(s, getFilter(s), LONG_MASK_, data_);
		}

		SubIterator(final Collection<?> s, long fragment, long mask,
				Object[] data) {
			this(new Condition<C>() {

				@Override
				public boolean holds(C o) {
					return s.containsAll(o);
				}
			}, fragment, mask, data);

		}

		SubIterator(Condition<? super C> condition, long fragment, long mask,
				Object[] data) {
			super(mask, data);
			this.condition = condition;
			this.nextFragment = fragment >>> BUCKET_SHIFT_;
			this.fragmentMask = (int) (fragment & mask & BUCKET_MASK_);
		}

		@Override
		void advancePos() {
			if (pos == fragmentMask) {
				noMorePos = true;
				return;
			}
			pos |= ~fragmentMask;
			pos++;
			pos &= fragmentMask;
		}

		@Override
		boolean noMorePos() {
			return noMorePos;
		}

		@Override
		Iterator<C> getRecursiveIterator(Object val) {
			return new SubIterator(condition, nextFragment, nextMask,
					(Object[]) val);
		}

		@Override
		Iterator<C> getLeafIterator(Object val) {
			return new FilteredIterator<C>(super.getLeafIterator(val),
					condition);
		}

	}

	/**
	 * Iterator over super-collections of a given collection
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	private class SuperIterator extends BaseIterator {

		final Condition<? super C> condition;

		final long nextFragment;

		final int fragmentMask;

		boolean noMorePos = false;

		SuperIterator(Collection<?> s) {
			this(s, getFilter(s), LONG_MASK_, data_);
		}

		SuperIterator(final Collection<?> s, long fragment, long mask,
				Object[] data) {
			this(new Condition<C>() {

				@Override
				public boolean holds(C o) {
					return o.containsAll(s);
				}
			}, fragment, mask, data);

		}

		SuperIterator(Condition<? super C> condition, long fragment, long mask,
				Object[] data) {
			super(mask, data);
			this.condition = condition;
			this.nextFragment = fragment >>> BUCKET_SHIFT_;
			this.fragmentMask = (int) (fragment & mask & BUCKET_MASK_);
			pos = (int) (mask & BUCKET_MASK_);
		}

		@Override
		void advancePos() {
			if (pos == fragmentMask) {
				noMorePos = true;
				return;
			}
			// else
			pos &= ~fragmentMask;
			pos--;
			pos |= fragmentMask;
		}

		@Override
		boolean noMorePos() {
			return noMorePos;
		}

		@Override
		Iterator<C> getRecursiveIterator(Object val) {
			return new SuperIterator(condition, nextFragment, nextMask,
					(Object[]) val);
		}

		@Override
		Iterator<C> getLeafIterator(Object val) {
			return new FilteredIterator<C>(super.getLeafIterator(val),
					condition);
		}

	}

	class SynchSizeIterator extends DelegatingIterator<C> {

		SynchSizeIterator(Iterator<C> delegate) {
			super(delegate);
		}

		@Override
		public void remove() {
			super.remove();
			size_--;
		}

	}

}
