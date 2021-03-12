package mccreery.betterhud.internal.tree;

import java.util.Collection;

/**
 * A tree iterable by depth first search.
 */
public interface Tree<T> extends Iterable<T> {
    /**
     * Returns all the direct children of this tree.
     */
    Collection<T> getChildren();

    /**
     * {@inheritDoc}
     * <p>For trees the return type is upgraded to a tree iterator to support pruning.
     * @see DefaultTreeIterator
     */
    @Override
    TreeIterator<T> iterator();
}
