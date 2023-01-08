/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2010 Eric Ahnell

 Any questions should be directed to the author via email at: lasertank@worldwizard.net
 */
package com.puttysoftware.lasertank.scoring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.puttysoftware.diane.fileio.GameIODataReader;
import com.puttysoftware.diane.fileio.GameIODataWriter;

public class LaserTankSortedScoreTable extends LaserTankScoreTable {
    // Fields
    protected LaserTankScoreSortOrder laserTankScoreSortOrder;

    // Constructors
    public LaserTankSortedScoreTable() {
	this.laserTankScoreSortOrder = LaserTankScoreSortOrder.ASCENDING;
    }

    public LaserTankSortedScoreTable(final int length, final LaserTankScoreSortOrder order) {
	super(length);
	this.laserTankScoreSortOrder = order;
    }

    @Override
    public void add(final LaserTankScore newEntry) {
	if (this.laserTankScoreSortOrder == LaserTankScoreSortOrder.DESCENDING) {
	    // Append the new score to the end
	    this.table.add(newEntry);
	    // Sort the score table
	    Collections.sort(this.table, new LaserTankScoreDescendingSorter());
	    // Remove the lowest score
	    this.table.remove(0);
	} else {
	    // Append the new score to the end
	    this.table.add(newEntry);
	    // Sort the score table
	    Collections.sort(this.table, new LaserTankScoreAscendingSorter());
	    // Remove the highest score
	    this.table.remove(this.table.size() - 1);
	}
    }

    public boolean check(final LaserTankScore newEntry) {
	final var temp = new ArrayList<>(this.table);
	if (this.laserTankScoreSortOrder == LaserTankScoreSortOrder.DESCENDING) {
	    // Copy the current table to the temporary table
	    Collections.copy(temp, this.table);
	    // Append the new score to the end
	    temp.add(newEntry);
	    // Sort the score table
	    Collections.sort(temp, new LaserTankScoreDescendingSorter());
	    // Determine if lowest score would be removed
	    return !Collections.min(temp, new LaserTankScoreDescendingSorter()).equals(newEntry);
	}
	// Copy the current table to the temporary table
	Collections.copy(temp, this.table);
	// Append the new score to the end
	temp.add(newEntry);
	// Sort the score table
	Collections.sort(temp, new LaserTankScoreAscendingSorter());
	// Determine if highest score would be removed
	return !Collections.max(temp, new LaserTankScoreAscendingSorter()).equals(newEntry);
    }

    @Override
    public void save(final GameIODataWriter gio) throws IOException {
	gio.writeInt(this.getLength());
	gio.writeString(this.laserTankScoreSortOrder.toString());
	for (final LaserTankScore sc : this.table) {
	    sc.save(gio);
	}
    }

    public static LaserTankSortedScoreTable load(final GameIODataReader gio) throws IOException {
	final var len = gio.readInt();
	final var so = LaserTankScoreSortOrder.valueOf(gio.readString());
	final var sst = new LaserTankSortedScoreTable(len, so);
	for (var x = 0; x < len; x++) {
	    final var sc = LaserTankScore.load(gio);
	    sst.add(sc);
	}
	return sst;
    }
}
