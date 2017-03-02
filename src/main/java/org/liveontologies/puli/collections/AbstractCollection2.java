package org.liveontologies.puli.collections;

import java.util.AbstractCollection;
import java.util.Collection;

public abstract class AbstractCollection2<C extends Collection<?>>
		extends AbstractCollection<C> implements Collection2<C> {

	@Override
	public boolean isMinimal(Collection<?> s) {
		return !subCollectionsOf(s).iterator().hasNext();
	}

	@Override
	public boolean isMaximal(Collection<?> s) {
		return !superCollectionsOf(s).iterator().hasNext();
	}

}
