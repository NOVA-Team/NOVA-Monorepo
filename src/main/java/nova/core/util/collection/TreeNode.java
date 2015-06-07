package nova.core.util.collection;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * A node inside of a tree structure.
 * @param <S> - Self type
 */
public class TreeNode<S extends TreeNode> {

	/**
	 * The children of the node.
	 */
	public final Set<S> children = new HashSet<>();
	/**
	 * The parent of the node. The root node has no parent.
	 */
	protected Optional<S> parent;

	public Optional<S> getParent() {
		return parent;
	}

	public void setParent(Optional<S> parent) {
		this.parent = parent;
	}

	public S addChild(S node) {
		children.add(node);
		node.parent = Optional.of(this);
		return node;
	}

	public void removeChild(S child) {
		children.remove(child);
		child.setParent(Optional.empty());
	}

	public Set<S> getChildren() {
		return children;
	}

	public Set<S> getAllChildren() {
		Set<S> perms = new HashSet<>();

		for (S child : children) {
			perms.add(child);
			perms.addAll(child.getAllChildren());
		}

		return perms;
	}

	/**
	 * Checks recursively to see if any of the children can match the given child.
	 * @param targetChild - The target match to find.
	 * @return True if the tree structure contains the targetPerm.
	 */
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
	public List<S> getHierarchy() {
		List<S> hierarchy = new ArrayList<>();

		Optional<S> currentParent = parent;

		while (currentParent.isPresent()) {
			hierarchy.add(currentParent.get());
			currentParent = currentParent.get().getParent();
		}

		return Lists.reverse(hierarchy);
	}

}
