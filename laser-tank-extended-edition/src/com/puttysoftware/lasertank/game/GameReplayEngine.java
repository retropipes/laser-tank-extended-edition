/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.DataIOReader;
import com.puttysoftware.lasertank.fileio.DataIOWriter;
import com.puttysoftware.lasertank.helper.GameActionHelper;
import com.puttysoftware.lasertank.index.GameAction;

class GameReplayEngine {
    // Inner classes
    private static class Link {
	public static Link read(final DataIOReader reader) throws IOException {
	    final var a = reader.readInt();
	    final var x = reader.readInt();
	    final var y = reader.readInt();
	    final var hasNextLink = reader.readBoolean();
	    final var link = new Link(GameActionHelper.fromOrdinal(a), x, y);
	    link.hasNext = hasNextLink;
	    return link;
	}

	// Fields
	public GameAction action;
	public int coordX, coordY;
	public Link next;
	public boolean hasNext;

	Link(final GameAction a, final int x, final int y) {
	    this.action = a;
	    this.coordX = x;
	    this.coordY = y;
	    this.next = null;
	}

	public void write(final DataIOWriter writer) throws IOException {
	    writer.writeInt(this.action.ordinal());
	    writer.writeInt(this.coordX);
	    writer.writeInt(this.coordY);
	    writer.writeBoolean(this.next != null);
	}
    }

    private static class LinkList {
	public static LinkList read(final DataIOReader reader) throws IOException {
	    final var hasData = reader.readBoolean();
	    final var ll = new LinkList();
	    if (hasData) {
		var curr = Link.read(reader);
		Link prev;
		ll.insertNext(null, curr);
		while (curr.hasNext) {
		    prev = curr;
		    curr = Link.read(reader);
		    ll.insertNext(prev, curr);
		}
	    }
	    return ll;
	}

	// Fields
	private Link first;

	LinkList() {
	    this.first = null;
	}

	public Link deleteFirst() {
	    final var temp = this.first;
	    this.first = this.first.next;
	    return temp;
	}

	public void insertFirst(final GameAction a, final int x, final int y) {
	    final var newLink = new Link(a, x, y);
	    newLink.next = this.first;
	    this.first = newLink;
	}

	private void insertNext(final Link currLink, final Link newLink) {
	    if (currLink == null) {
		this.first = newLink;
	    } else {
		currLink.next = newLink;
	    }
	}

	public boolean isEmpty() {
	    return this.first == null;
	}

	private void reverse() {
	    var current = this.first;
	    this.first = null;
	    while (current != null) {
		final var save = current;
		current = current.next;
		save.next = this.first;
		this.first = save;
	    }
	}

	public void write(final DataIOWriter writer) throws IOException {
	    this.reverse();
	    if (this.isEmpty()) {
		writer.writeBoolean(false);
	    } else {
		writer.writeBoolean(true);
		var node = this.first;
		while (node != null) {
		    node.write(writer);
		    node = node.next;
		}
	    }
	}
    }

    private static class LinkStack {
	public static LinkStack read(final DataIOReader reader) throws IOException {
	    final var ls = new LinkStack();
	    ls.theList = LinkList.read(reader);
	    return ls;
	}

	// Fields
	private LinkList theList;

	LinkStack() {
	    this.theList = new LinkList();
	}

	public boolean isEmpty() {
	    return this.theList.isEmpty();
	}

	public Link pop() {
	    return this.theList.deleteFirst();
	}

	public void push(final GameAction a, final int x, final int y) {
	    this.theList.insertFirst(a, x, y);
	}

	public void write(final DataIOWriter writer) throws IOException {
	    this.theList.write(writer);
	}
    }

    static GameReplayEngine readReplay(final DataIOReader reader) throws IOException {
	final var gre = new GameReplayEngine();
	gre.redoHistory = LinkStack.read(reader);
	return gre;
    }

    // Fields
    private final LinkStack undoHistory;
    private LinkStack redoHistory;
    private GameAction action;
    private int destX, destY;

    // Constructors
    public GameReplayEngine() {
	this.undoHistory = new LinkStack();
	this.redoHistory = new LinkStack();
	this.action = GameAction.NONE;
	this.destX = -1;
	this.destY = -1;
    }

    GameAction getAction() {
	return this.action;
    }

    int getX() {
	return this.destX;
    }

    int getY() {
	return this.destY;
    }

    // Public methods
    void redo() {
	if (!this.redoHistory.isEmpty()) {
	    final var entry = this.redoHistory.pop();
	    this.action = entry.action;
	    this.destX = entry.coordX;
	    this.destY = entry.coordY;
	} else {
	    this.action = GameAction.NONE;
	    this.destX = -1;
	    this.destY = -1;
	}
    }

    boolean tryRedo() {
	return !this.redoHistory.isEmpty();
    }

    void updateRedoHistory(final GameAction a, final int x, final int y) {
	this.redoHistory.push(a, x, y);
    }

    void updateUndoHistory(final GameAction a, final int x, final int y) {
	this.undoHistory.push(a, x, y);
    }

    void writeReplay(final DataIOWriter writer) throws IOException {
	this.undoHistory.write(writer);
    }
}
