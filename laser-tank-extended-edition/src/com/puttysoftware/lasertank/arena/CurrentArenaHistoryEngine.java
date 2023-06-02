package com.puttysoftware.lasertank.arena;

class CurrentArenaHistoryEngine {
    // Fields
    private CurrentArenaHistoryStack undoHistory, redoHistory;
    private HistoryStatus whatWas;
    private CurrentArenaStorage image;

    // Constructors
    public CurrentArenaHistoryEngine() {
	this.undoHistory = new CurrentArenaHistoryStack();
	this.redoHistory = new CurrentArenaHistoryStack();
	this.image = null;
	this.whatWas = null;
    }

    public void clearRedoHistory() {
	this.redoHistory = new CurrentArenaHistoryStack();
    }

    public void clearUndoHistory() {
	this.undoHistory = new CurrentArenaHistoryStack();
    }

    public CurrentArenaStorage getImage() {
	return this.image;
    }

    public HistoryStatus getWhatWas() {
	return this.whatWas;
    }

    public void redo() {
	if (!this.redoHistory.isEmpty()) {
	    final var entry = this.redoHistory.pop();
	    this.image = entry.getImage();
	    this.whatWas = entry.getWhatWas();
	} else {
	    this.image = null;
	    this.whatWas = null;
	}
    }

    public boolean tryRedo() {
	return !this.redoHistory.isEmpty();
    }

    public boolean tryUndo() {
	return !this.undoHistory.isEmpty();
    }

    // Public methods
    public void undo() {
	if (!this.undoHistory.isEmpty()) {
	    final var entry = this.undoHistory.pop();
	    this.image = entry.getImage();
	    this.whatWas = entry.getWhatWas();
	} else {
	    this.image = null;
	    this.whatWas = null;
	}
    }

    public void updateRedoHistory(final CurrentArenaStorage newImage, final HistoryStatus newWhatWas) {
	this.redoHistory.push(newImage, newWhatWas);
    }

    public void updateUndoHistory(final CurrentArenaStorage newImage, final HistoryStatus newWhatWas) {
	this.undoHistory.push(newImage, newWhatWas);
    }
}