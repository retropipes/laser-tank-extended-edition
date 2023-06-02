/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.scoring;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.lasertank.engine.fileio.GameIODataReader;
import com.puttysoftware.lasertank.engine.fileio.GameIODataWriter;

public class LaserTankScoreTable {
    public static LaserTankScoreTable load(final GameIODataReader gio) throws IOException {
	final var len = gio.readInt();
	final var st = new LaserTankScoreTable(len);
	for (var x = 0; x < len; x++) {
	    final var sc = LaserTankScore.load(gio);
	    st.add(sc);
	}
	return st;
    }

    // Fields
    protected ArrayList<LaserTankScore> table;

    // Constructors
    public LaserTankScoreTable() {
	this(10);
    }

    public LaserTankScoreTable(final int length) {
	this.table = new ArrayList<>(length);
	int x;
	for (x = 0; x < length; x++) {
	    this.table.set(x, new LaserTankScore());
	}
    }

    public void add(final LaserTankScore laserTankScore) {
	this.table.add(laserTankScore);
    }

    public long getEntryMoves(final int pos) {
	return this.table.get(pos).getMoves();
    }

    // Methods
    public String getEntryName(final int pos) {
	return this.table.get(pos).getName();
    }

    public long getEntryShots(final int pos) {
	return this.table.get(pos).getShots();
    }

    public int getLength() {
	return this.table.size();
    }

    public void save(final GameIODataWriter gio) throws IOException {
	gio.writeInt(this.getLength());
	for (final LaserTankScore sc : this.table) {
	    sc.save(gio);
	}
    }
}
