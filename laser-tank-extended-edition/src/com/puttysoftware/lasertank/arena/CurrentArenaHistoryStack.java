package com.puttysoftware.lasertank.arena;

import java.util.ArrayDeque;

class CurrentArenaHistoryStack {
    // Fields
    private final ArrayDeque<CurrentArenaHistoryEntry> stack;

    CurrentArenaHistoryStack() {
        this.stack = new ArrayDeque<>();
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public CurrentArenaHistoryEntry pop() {
        return this.stack.removeFirst();
    }

    public void push(final CurrentArenaStorage i, final HistoryStatus hww) {
        final var newEntry = new CurrentArenaHistoryEntry(i, hww);
        this.stack.addFirst(newEntry);
    }
}