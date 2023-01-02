/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2010 Eric Ahnell

 Any questions should be directed to the author via email at: lasertank@worldwizard.net
 */
package com.puttysoftware.lasertank.scoring;

import java.io.IOException;

import com.puttysoftware.diane.fileio.GameIODataReader;
import com.puttysoftware.diane.fileio.GameIODataWriter;

public final class LaserTankScore {
    public static LaserTankScore load(final GameIODataReader gio) throws IOException {
	final String loadName = gio.readString();
	final long loadMoves = gio.readLong();
	final long loadShots = gio.readLong();
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
	this.name = "Nobody";
    }

    public LaserTankScore(final long newMoves, final long newShots, final String newName) {
	this.moves = newMoves;
	this.shots = newShots;
	this.name = newName;
    }

    // Methods
    public String getName() {
	return this.name;
    }

    public long getMoves() {
	return this.moves;
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
