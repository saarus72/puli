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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A {@link Collection2} backed by {@link ArrayList}s; iterators over
 * sub-collections and super-collections returned by
 * {@link #subCollectionsOf(Collection)} {@link #superCollectionsOf(Collection)}
 * support removal of elements so long {@link Iterator#hasNext()} is not called
 * before {@link Iterator#remove()}
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 *            the types of elements managed by this {@link Collection2}
 */
public class ArrayListCollection2<C extends Collection<?>>
		extends AbstractTrieCollection2<C> {

	@Override
	protected Collection<C> create() {
		return new ArrayList<C>(3);
	}

}
