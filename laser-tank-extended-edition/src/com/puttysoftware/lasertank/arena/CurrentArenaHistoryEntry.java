package com.puttysoftware.lasertank.arena;

// Inner classes
class CurrentArenaHistoryEntry {
    // Fields
    private final CurrentArenaStorage histImage;
    private final HistoryStatus histWhatWas;

    CurrentArenaHistoryEntry(final CurrentArenaStorage i, final HistoryStatus hww) {
	this.histImage = i;
	this.histWhatWas = hww;
    }

    public CurrentArenaStorage getImage() {
	return this.histImage;
    }

    public HistoryStatus getWhatWas() {
	return this.histWhatWas;
    }
}