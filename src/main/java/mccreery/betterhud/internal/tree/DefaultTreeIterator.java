package mccreery.betterhud.internal.tree;

import java.util.ArrayDeque;
import java.util.Deque;

public class DefaultTreeIterator<T extends Tree<T>> implements TreeIterator<T> {
    private final Deque<T> stack = new ArrayDeque<>();

    /**
     * The result of the previous next() for deferred branching.
     */
    private T nextBranch;

    public DefaultTreeIterator(T root) {
        stack.push(root);
    }

    @Override
    public boolean hasNext() {
        // The stack cannot be empty until next() so no null check is needed for nextBranch
        return !stack.isEmpty() || !nextBranch.getChildren().isEmpty();
    }

    @Override
    public T next() {
        // To support pruning, adding children is deferred
        // This would usually be done using the direct result of pop()
        if (nextBranch != null) {
            for (T child : nextBranch.getChildren()) {
                stack.push(child);
            }
        }

        nextBranch = stack.pop();
        return nextBranch;
    }

    @Override
    public void prune() {
        if (nextBranch == null) {
            throw new IllegalStateException("prune cannot be called before next");
        }
        nextBranch = null;
    }
}
