package org.semanticweb.elk.proofs.browser;

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


import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.liveontologies.puli.GenericInferenceSet;
import org.liveontologies.puli.JustifiedInference;

import com.google.common.collect.HashMultimap;

public class InferenceSetTreeComponent<C, A> extends JTree {
	private static final long serialVersionUID = 8406872780618425810L;

	private final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet_;
	private final C conclusion_;

	private final TreeNodeLabelProvider nodeDecorator_;
	private final TreeNodeLabelProvider toolTipProvider_;

	private final HashMultimap<Object, TreePath> visibleNodes_;

	public InferenceSetTreeComponent(
			final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet,
			final C conclusion) {
		this(inferenceSet, conclusion, null, null);
	}

	public InferenceSetTreeComponent(
			final GenericInferenceSet<C, ? extends JustifiedInference<C, A>> inferenceSet,
			final C conclusion, final TreeNodeLabelProvider nodeDecorator,
			final TreeNodeLabelProvider toolTipProvider) {
		this.inferenceSet_ = inferenceSet;
		this.conclusion_ = conclusion;
		this.nodeDecorator_ = nodeDecorator;
		this.toolTipProvider_ = toolTipProvider;

		this.visibleNodes_ = HashMultimap.create();

		setModel(new TreeModelInferenceSetAdapter());

		setEditable(true);

		final TreeCellRenderer renderer = new TreeCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		setCellRenderer(renderer);

		// JTree does not delegate ToolTip to tree cells otherwise.
		ToolTipManager.sharedInstance().registerComponent(this);

		resetVisibleNodes();

		// Need to know what will be visible before it gets displayed.
		addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillExpand(final TreeExpansionEvent event)
					throws ExpandVetoException {

				final TreePath path = event.getPath();
				final Object parent = path.getLastPathComponent();
				final int count = getModel().getChildCount(parent);
				for (int i = 0; i < count; i++) {
					final Object child = getModel().getChild(parent, i);
					if (!(child instanceof JustifiedInference)) {
						visibleNodes_.put(child, path.pathByAddingChild(child));
					}
				}

			}

			@Override
			public void treeWillCollapse(final TreeExpansionEvent event)
					throws ExpandVetoException {

				final TreePath path = event.getPath();
				final Object parent = path.getLastPathComponent();
				final int count = getModel().getChildCount(parent);
				for (int i = 0; i < count; i++) {
					final Object child = getModel().getChild(parent, i);
					if (!(child instanceof JustifiedInference)) {
						visibleNodes_.remove(child,
								path.pathByAddingChild(child));
					}
				}

			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {

				final TreePath path = getPathForLocation(e.getX(), e.getY());
				if (path == null) {
					return;
				}

				if (e.getClickCount() == 2) {
					if (isExpanded(path)) {
						// TODO: collapse all children
					} else {
						expandAll(path);
					}
				}

			}
		});

	}

	private void resetVisibleNodes() {
		visibleNodes_.clear();
		final Object root = getModel().getRoot();
		final TreePath rootPath = new TreePath(root);
		if (isRootVisible()) {
			visibleNodes_.put(root, rootPath);
		}

		final Enumeration<TreePath> expanded = getExpandedDescendants(rootPath);
		if (expanded != null) {
			while (expanded.hasMoreElements()) {
				final TreePath path = expanded.nextElement();
				final Object parent = path.getLastPathComponent();
				final int count = getModel().getChildCount(parent);
				for (int i = 0; i < count; i++) {
					final Object child = getModel().getChild(parent, i);
					if (!(child instanceof JustifiedInference)) {
						visibleNodes_.put(child, path.pathByAddingChild(child));
					}
				}
			}
		}
	}

	private void expandAll(final TreePath rootPath) {
		if (rootPath == null) {
			return;
		}

		final Queue<Object> toDo = new LinkedList<Object>();
		final Map<Object, TreePath> done = new HashMap<Object, TreePath>();

		Object node = rootPath.getLastPathComponent();
		if (node instanceof JustifiedInference) {

			expandPath(rootPath);

			final int premCount = getModel().getChildCount(node);
			for (int premIndex = 0; premIndex < premCount; premIndex++) {
				final Object prem = getModel().getChild(node, premIndex);
				final TreePath premPath = rootPath.pathByAddingChild(prem);
				if (done.put(prem, premPath) == null) {
					toDo.add(prem);
				}
			}

		} else {
			toDo.add(node);
			done.put(node, rootPath);
			// If the node is an axiom, it is not a problem.
		}

		while ((node = toDo.poll()) != null) {

			final TreePath path = done.get(node);

			expandPath(path);

			final int infCount = getModel().getChildCount(node);
			for (int infIndex = 0; infIndex < infCount; infIndex++) {
				final Object inf = getModel().getChild(node, infIndex);

				final TreePath infPath = path.pathByAddingChild(inf);

				expandPath(infPath);

				final int premCount = getModel().getChildCount(inf);
				for (int premIndex = 0; premIndex < premCount; premIndex++) {
					final Object prem = getModel().getChild(inf, premIndex);
					final TreePath premPath = infPath.pathByAddingChild(prem);
					if (done.put(prem, premPath) == null) {
						toDo.add(prem);
					}
				}
			}

		}

	}

	@Override
	public String convertValueToText(final Object value, final boolean selected,
			final boolean expanded, final boolean leaf, final int row,
			final boolean hasFocus) {

		final String label;
		if (value == null) {
			label = "";
		} else {
			if (value instanceof JustifiedInference) {
				label = "⊣";// TODO: display the justification here instead of
							// as one of the premises
			} else {
				label = value.toString();
			}
		}

		if (nodeDecorator_ == null) {
			return label;
		} else {
			final TreePath path = getPathForRow(row);
			final String decoration = nodeDecorator_.getLabel(value, path);
			return decoration == null ? label : decoration + label;
		}

	}

	private class TreeModelInferenceSetAdapter implements TreeModel {

		@Override
		public Object getRoot() {
			return conclusion_;
		}

		@Override
		public Object getChild(final Object parent, final int index) {
			if (parent instanceof JustifiedInference) {
				final JustifiedInference<?, ?> inf = (JustifiedInference<?, ?>) parent;
				int i = 0;
				for (final Object premise : inf.getPremises()) {
					if (i == index) {
						return premise;
					}
					i++;
				}
				for (final Object axiom : inf.getJustification()) {
					if (i == index) {
						return axiom;
					}
					i++;
				}
			} else {
				try {
					/*
					 * Whether parent is a conclusion or an axiom can be
					 * determined only by trying and catching
					 * ClassCastException.
					 */
					@SuppressWarnings("unchecked")
					final Collection<? extends JustifiedInference<C, A>> inferences = inferenceSet_
							.getInferences((C) parent);
					int i = 0;
					for (final JustifiedInference<C, A> inf : inferences) {
						if (i == index) {
							return inf;
						}
						i++;
					}
				} catch (final ClassCastException e) {
					// parent is an axiom, so return null.
				}
			}
			return null;
		}

		@Override
		public int getChildCount(final Object parent) {
			if (parent instanceof JustifiedInference) {
				final JustifiedInference<?, ?> inf = (JustifiedInference<?, ?>) parent;
				return inf.getPremises().size() + inf.getJustification().size();
			} else {
				try {
					/*
					 * Whether parent is a conclusion or an axiom can be
					 * determined only by trying and catching
					 * ClassCastException.
					 */
					@SuppressWarnings("unchecked")
					final Iterator<? extends JustifiedInference<C, A>> inferenceIterator = inferenceSet_
							.getInferences((C) parent).iterator();
					int i = 0;
					while (inferenceIterator.hasNext()) {
						inferenceIterator.next();
						i++;
					}
					return i;
				} catch (final ClassCastException e) {
					// parent is an axiom, so return 0.
					return 0;
				}
			}
		}

		@Override
		public boolean isLeaf(final Object node) {
			if (node instanceof JustifiedInference) {
				final JustifiedInference<?, ?> inf = (JustifiedInference<?, ?>) node;
				return inf.getPremises().isEmpty()
						&& inf.getJustification().isEmpty();
			} else {
				try {
					/*
					 * Whether node is a conclusion or an axiom can be
					 * determined only by trying and catching
					 * ClassCastException.
					 */
					@SuppressWarnings("unchecked")
					final Collection<? extends JustifiedInference<C, A>> inferences = inferenceSet_
							.getInferences((C) node);
					return !inferences.iterator().hasNext();
				} catch (final ClassCastException e) {
					// node is an axiom, so return true.
					return true;
				}
			}
		}

		@Override
		public int getIndexOfChild(final Object parent, final Object child) {
			if (parent == null || child == null) {
				return -1;
			}
			if (parent instanceof JustifiedInference) {
				final JustifiedInference<?, ?> inf = (JustifiedInference<?, ?>) parent;
				int i = 0;
				for (final Object premise : inf.getPremises()) {
					if (child.equals(premise)) {
						return i;
					}
					i++;
				}
				for (final Object axiom : inf.getJustification()) {
					if (child.equals(axiom)) {
						return i;
					}
					i++;
				}
			} else {
				try {
					/*
					 * Whether parent is a conclusion or an axiom can be
					 * determined only by trying and catching
					 * ClassCastException.
					 */
					@SuppressWarnings("unchecked")
					final Collection<? extends JustifiedInference<C, A>> inferences = inferenceSet_
							.getInferences((C) parent);
					int i = 0;
					for (final JustifiedInference<C, A> inf : inferences) {
						if (child.equals(inf)) {
							return i;
						}
						i++;
					}
				} catch (final ClassCastException e) {
					// parent is an axiom, so return -1.
				}
			}
			return -1;
		}

		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
			// The tree is immutable, so no change is possible.
		}

		@Override
		public void addTreeModelListener(TreeModelListener l) {
			// The tree is immutable, so listeners never fire.
		}

		@Override
		public void removeTreeModelListener(TreeModelListener l) {
			// The tree is immutable, so listeners never fire.
		}

	}

	private class TreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -711871019527222465L;

		@Override
		public Component getTreeCellRendererComponent(final JTree tree,
				final Object value, final boolean sel, final boolean expanded,
				final boolean leaf, final int row, final boolean hasFocus) {

			final Component component = super.getTreeCellRendererComponent(tree,
					value, sel, expanded, leaf, row, hasFocus);

			if (component instanceof JComponent) {
				final JComponent jComponent = (JComponent) component;
				if (toolTipProvider_ != null) {
					final TreePath path = getPathForRow(row);
					final String toolTip = toolTipProvider_.getLabel(value,
							path);
					if (toolTip != null) {
						jComponent.setToolTipText(toolTip);
					}
				}
			}

			// If the value is displayed multiple times, highlight it
			final Set<TreePath> paths = visibleNodes_.get(value);
			if (paths.size() > 1) {
				setBackgroundNonSelectionColor(colorFromHash(value));
			} else {
				setBackgroundNonSelectionColor(null);
			}

			return component;
		}

	}

	private static Color colorFromHash(final Object obj) {
		return new Color(Color.HSBtoRGB(obj.hashCode() / 7919.0f, 0.4f, 0.95f));
	}

}
