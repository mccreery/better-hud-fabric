package mccreery.betterhud.internal.tree;

import java.util.Iterator;

/**
 * A depth first search over a {@link Tree}.
 */
public interface TreeIterator<T> extends Iterator<T> {
    /**
     * Ignores the children of the last branch returned by {@link #next()}. This must be called before
     * {@link #hasNext()} as all the remaining elements may be ignored.
     *
     * @throws IllegalStateException if the {@code next} method has not yet been called.
     */
    void prune();
}
