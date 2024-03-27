/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.scoring;

import java.io.IOException;

import com.puttysoftware.lasertank.fileio.GameIODataReader;
import com.puttysoftware.lasertank.fileio.GameIODataWriter;
import com.puttysoftware.lasertank.locale.GenericString;
import com.puttysoftware.lasertank.locale.Strings;

public final class LaserTankScore {
    public static LaserTankScore load(final GameIODataReader gio) throws IOException {
	final var loadName = gio.readString();
	final var loadMoves = gio.readLong();
	final var loadShots = gio.readLong();
	return new LaserTankScore(loadMoves, loadShots, loadName);
    }

    // Fields
    final long moves;
    final long shots;
    private final String name;

    // Constructors
    public LaserTankScore() {
	this.moves = 0L;
	this.shots = 0L;
	this.name = Strings.loadGeneric(GenericString.NOBODY);
    }

    public LaserTankScore(final long newMoves, final long newShots, final String newName) {
	this.moves = newMoves;
	this.shots = newShots;
	this.name = newName;
    }

    public long getMoves() {
	return this.moves;
    }

    // Methods
    public String getName() {
	return this.name;
    }

    public long getShots() {
	return this.shots;
    }

    public void save(final GameIODataWriter gio) throws IOException {
	gio.writeString(this.name);
	gio.writeLong(this.moves);
	gio.writeLong(this.shots);
    }
}
