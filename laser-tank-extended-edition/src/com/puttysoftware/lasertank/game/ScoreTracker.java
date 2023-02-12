/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.game;

import java.io.File;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.datatype.FileExtensions;
import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.GameString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;
import com.puttysoftware.lasertank.scoring.LaserTankSavedScoreManager;
import com.puttysoftware.lasertank.scoring.LaserTankScoreSortOrder;

class ScoreTracker {
    private static final String MAC_PREFIX = GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_UNIX_HOME);
    private static final String WIN_PREFIX = GlobalStrings
	    .loadUntranslated(UntranslatedString.DIRECTORY_WINDOWS_APPDATA);
    private static final String UNIX_PREFIX = GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_UNIX_HOME);
    private static final String MAC_DIR = GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_SCORES_MAC);
    private static final String WIN_DIR = GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_SCORES_WINDOWS);
    private static final String UNIX_DIR = GlobalStrings.loadUntranslated(UntranslatedString.DIRECTORY_SCORES_UNIX);

    private static String getScoreDirectory() {
	final var osName = System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME));
	if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.MAC_OS_X)) != -1) {
	    // Mac OS X
	    return ScoreTracker.MAC_DIR;
	}
	if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS)) != -1) {
	    // Windows
	    return ScoreTracker.WIN_DIR;
	}
	// Other - assume UNIX-like
	return ScoreTracker.UNIX_DIR;
    }

    private static String getScoreDirPrefix() {
	final var osName = System.getProperty(GlobalStrings.loadUntranslated(UntranslatedString.OS_NAME));
	if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.MAC_OS_X)) != -1) {
	    // Mac OS X
	    return System.getenv(ScoreTracker.MAC_PREFIX);
	}
	if (osName.indexOf(GlobalStrings.loadUntranslated(UntranslatedString.WINDOWS)) != -1) {
	    // Windows
	    return System.getenv(ScoreTracker.WIN_PREFIX);
	}
	// Other - assume UNIX-like
	return System.getenv(ScoreTracker.UNIX_PREFIX);
    }

    private static File getScoresFile(final String filename) {
	final var b = new StringBuilder();
	b.append(ScoreTracker.getScoreDirPrefix());
	b.append(ScoreTracker.getScoreDirectory());
	b.append(filename);
	b.append(Strings.loadCommon(CommonString.UNDERSCORE));
	b.append(LaserTankEE.getApplication().getArenaManager().getArena().getActiveLevelNumber() + 1);
	b.append(FileExtensions.getScoresExtensionWithPeriod());
	return new File(b.toString());
    }

    // Fields
    private LaserTankSavedScoreManager ssMgr;
    private long moves;
    private long shots;
    private long others;
    private boolean trackScores;

    // Constructors
    public ScoreTracker() {
	this.moves = 0L;
	this.shots = 0L;
	this.others = 0L;
	this.ssMgr = null;
	this.trackScores = true;
    }

    // Methods
    boolean checkScore() {
	if (this.trackScores) {
	    return this.ssMgr.check(this.moves, this.shots);
	}
	return false;
    }

    void commitScore() {
	if (this.trackScores) {
	    final var result = this.ssMgr.add(this.moves, this.shots);
	    if (result) {
		this.ssMgr.view();
	    }
	}
    }

    void decrementMoves() {
	this.moves--;
    }

    void decrementOthers() {
	this.others--;
    }

    void decrementShots() {
	this.shots--;
    }

    long getMoves() {
	return this.moves;
    }

    long getOthers() {
	return this.others;
    }

    long getShots() {
	return this.shots;
    }

    void incrementMoves() {
	this.moves++;
    }

    void incrementOthers() {
	this.others++;
    }

    void incrementShots() {
	this.shots++;
    }

    void resetScore() {
	this.moves = 0L;
	this.shots = 0L;
	this.others = 0L;
    }

    void resetScore(final String filename) {
	this.setScoreFile(filename);
	this.moves = 0L;
	this.shots = 0L;
	this.others = 0L;
    }

    void setMoves(final long m) {
	this.moves = m;
    }

    void setOthers(final long o) {
	this.others = o;
    }

    void setScoreFile(final String filename) {
	this.trackScores = true;
	// Check filename argument
	if (filename != null) {
	    if (filename.isEmpty()) {
		this.trackScores = false;
	    }
	} else {
	    this.trackScores = false;
	}
	if (this.trackScores) {
	    // Make sure the needed directories exist first
	    final var sf = ScoreTracker.getScoresFile(filename);
	    final var parent = new File(sf.getParent());
	    if (!parent.exists()) {
		final var success = parent.mkdirs();
		if (!success) {
		    this.trackScores = false;
		}
	    }
	    if (this.trackScores) {
		final var scoresFile = sf.getAbsolutePath();
		this.ssMgr = new LaserTankSavedScoreManager(10, LaserTankScoreSortOrder.DESCENDING,
			GlobalStrings.loadUntranslated(UntranslatedString.PROGRAM_NAME)
				+ Strings.loadCommon(CommonString.SPACE) + Strings.loadGame(GameString.SCORES),
			scoresFile);
	    }
	}
    }

    void setShots(final long s) {
	this.shots = s;
    }

    void showScoreTable() {
	if (this.trackScores) {
	    this.ssMgr.view();
	} else {
	    CommonDialogs.showDialog(Strings.loadGame(GameString.SCORES_UNAVAILABLE));
	}
    }
}
