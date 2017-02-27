package org.liveontologies.puli.collections;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListCollectionMinimizer<T, C extends Collection<T>> extends
		AbstractCollectionMinimizer<T, C> implements CollectionMinimizer<T, C> {

	@Override
	Collection<C> create() {
		return new ArrayList<C>(3);
	}

}
