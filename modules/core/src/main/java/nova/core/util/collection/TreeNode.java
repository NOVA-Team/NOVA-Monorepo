/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.util.collection;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * A node inside of a tree structure.
 *
 * @param <S> - Self type
 */
public class TreeNode<S extends TreeNode<S>> implements Iterable<S> {

	/**
	 * The children of the node.
	 */
	public final Set<S> children = new HashSet<>();
	/**
	 * The parent of the node. The root node has no parent.
	 */
	protected Optional<S> parent;

	/**
	 * The parrent of this node
	 *
	 * @return The parrent of this node if there is one, otherwise Optional.empty
	 */
	public Optional<S> getParent() {
		return parent;
	}

	/**
	 * Set's the parrent of this node
	 *
	 * @param parent The new parrent of this node
	 */
	public void setParent(Optional<S> parent) {
		this.parent = parent;
	}

	/**
	 * Adds a childnode to the node
	 *
	 * @param node The childnode to add
	 * @return The merged node
	 */
	@SuppressWarnings("unchecked")
	public S addChild(S node) {
		children.add(node);
		node.parent = Optional.of((S) this);
		return node;
	}

	/**
	 * Remove a childnode
	 *
	 * @param child The childnode to remove
	 * @return This
	 */
	@SuppressWarnings("unchecked")
	public S removeChild(S child) {
		children.remove(child);
		child.setParent(Optional.empty());
		return (S) this;
	}

	/**
	 * Get the direct childnodes
	 *
	 * @return A set containing the direct childnodes
	 */
	@SuppressWarnings("unchecked")
	public Set<S> children() {
		return children;
	}

	/**
	 * Gets all child nodes recursively
	 *
	 * @return A set containing all child nodes and their descendants
	 */
	@SuppressWarnings("unchecked")
	public Set<S> descendants() {
		Set<S> perms = new HashSet<>();

		for (S child : children) {
			perms.add(child);
			perms.addAll(child.descendants());
		}

		return perms;
	}

	/**
	 * Checks recursively to see if any of the children can match the given child.
	 *
	 * @param targetChild - The target match to find.
	 * @return True if the tree structure contains the targetPerm.
	 */
	@SuppressWarnings("unchecked")
	public boolean exists(S targetChild) {
		if (equals(targetChild)) {
			return true;
		}

		for (S child : children) {
			if (child.exists(targetChild)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return Gets the hierarchy of the tree ordered from the root node to the current node.
	 */
	@SuppressWarnings("unchecked")
	public List<S> hierarchy() {
		List<S> hierarchy = new ArrayList<>();

		Optional<S> currentParent = parent;

		while (currentParent.isPresent()) {
			hierarchy.add(currentParent.get());
			currentParent = currentParent.get().getParent();
		}

		return Lists.reverse(hierarchy);
	}

	public Stream<S> stream() {
		return children.stream();
	}

	@Override
	public Iterator<S> iterator() {
		return children.iterator();
	}

	@Override
	public Spliterator<S> spliterator() {
		return children.spliterator();
	}
}
