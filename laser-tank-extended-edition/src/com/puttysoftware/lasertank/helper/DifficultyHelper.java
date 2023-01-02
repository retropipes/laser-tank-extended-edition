/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.Difficulty;
import com.puttysoftware.lasertank.locale.Strings;

public class DifficultyHelper {
    private static String[] NAMES = null;

    public static String getName(final Difficulty thing) {
	return Strings.loadDifficulty(thing);
    }

    public static String[] getNames() {
	if (DifficultyHelper.NAMES == null) {
	    DifficultyHelper.activeLanguageChanged();
	}
	return DifficultyHelper.NAMES;
    }

    public static void activeLanguageChanged() {
	DifficultyHelper.NAMES = new String[] { Strings.loadDifficulty(Difficulty.KIDS),
		Strings.loadDifficulty(Difficulty.EASY), Strings.loadDifficulty(Difficulty.MEDIUM),
		Strings.loadDifficulty(Difficulty.HARD), Strings.loadDifficulty(Difficulty.DEADLY) };
    }

    public static Difficulty fromOrdinal(final int value) {
	return Difficulty.values()[value];
    }

    private DifficultyHelper() {
	// Do nothing
    }
}
