/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2010 Eric Ahnell

 Any questions should be directed to the author via email at: lasertank@worldwizard.net
 */
package com.puttysoftware.lasertank.scoring;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.diane.fileio.GameIODataReader;
import com.puttysoftware.diane.fileio.GameIODataWriter;

public class LaserTankScoreTable {
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

    // Methods
    public String getEntryName(final int pos) {
	return this.table.get(pos).getName();
    }

    public long getEntryMoves(final int pos) {
	return this.table.get(pos).getMoves();
    }

    public long getEntryShots(final int pos) {
	return this.table.get(pos).getShots();
    }

    public int getLength() {
	return this.table.size();
    }

    public void add(final LaserTankScore laserTankScore) {
	this.table.add(laserTankScore);
    }

    public void save(final GameIODataWriter gio) throws IOException {
	gio.writeInt(this.getLength());
	for (final LaserTankScore sc : this.table) {
	    sc.save(gio);
	}
    }

    public static LaserTankScoreTable load(final GameIODataReader gio) throws IOException {
	final int len = gio.readInt();
	final LaserTankScoreTable st = new LaserTankScoreTable(len);
	for (int x = 0; x < len; x++) {
	    final LaserTankScore sc = LaserTankScore.load(gio);
	    st.add(sc);
	}
	return st;
    }
}
