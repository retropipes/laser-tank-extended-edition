/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2010 Eric Ahnell

 Any questions should be directed to the author via email at: lasertank@worldwizard.net
 */
package com.puttysoftware.lasertank.scoring;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.locale.DialogString;
import com.puttysoftware.lasertank.locale.Strings;

public class LaserTankScores {
    // Fields and Constants
    private static final String NAME_PROMPT = "Enter a name for the score list:";
    public static final boolean SORT_ORDER_DESCENDING = false;
    protected LaserTankSortedScoreTable table;
    private String name;
    private String title;
    private final String viewerTitle;

    // Constructors
    public LaserTankScores() {
	this.table = new LaserTankSortedScoreTable();
	this.name = "";
	final String dialogTitle = Strings.loadDialog(DialogString.SCORES_HEADER);
	this.title = dialogTitle;
	this.viewerTitle = dialogTitle;
    }

    public LaserTankScores(final int length, final LaserTankScoreSortOrder laserTankScoreSortOrder,
	    final String customTitle) {
	this.table = new LaserTankSortedScoreTable(length, laserTankScoreSortOrder);
	this.name = "";
	if (customTitle == null || customTitle.equals("")) {
	    this.title = Strings.loadDialog(DialogString.SCORES_HEADER);
	} else {
	    this.title = customTitle;
	}
	this.viewerTitle = customTitle;
    }

    // Methods
    public boolean add(final long newMoves, final long newShots) {
	boolean success = true;
	this.name = CommonDialogs.showTextInputDialog(LaserTankScores.NAME_PROMPT, this.title);
	if (this.name != null) {
	    this.table.add(new LaserTankScore(newMoves, newShots, this.name));
	} else {
	    success = false;
	}
	return success;
    }

    public boolean add(final long newMoves, final long newShots, final String newName) {
	this.table.add(new LaserTankScore(newMoves, newShots, newName));
	return true;
    }

    public boolean check(final long newMoves, final long newShots) {
	return this.table.check(new LaserTankScore(newMoves, newShots, this.name));
    }

    public void view() {
	LaserTankScoreTableViewer.view(this.table, this.viewerTitle);
    }
}
