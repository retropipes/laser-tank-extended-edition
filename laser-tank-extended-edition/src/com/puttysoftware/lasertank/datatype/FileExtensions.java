/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.datatype;

import com.puttysoftware.lasertank.locale.CommonString;
import com.puttysoftware.lasertank.locale.Strings;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;
import com.puttysoftware.lasertank.locale.global.UntranslatedString;

public class FileExtensions {
    // Constants
    private static final String SETTINGS_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_SETTINGS);
    private static final String OLD_LEVEL_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_OLD_LEVEL);
    private static final String OLD_PLAYBACK_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_OLD_PLAYBACK);
    private static final String ARENA_EXTENSION = GlobalStrings.loadUntranslated(UntranslatedString.EXTENSION_ARENA);
    private static final String PROTECTED_ARENA_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_PROTECTED_ARENA);
    private static final String ARENA_LEVEL_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_ARENA_DATA);
    private static final String SAVED_GAME_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_SAVED_GAME);
    private static final String SCORES_EXTENSION = GlobalStrings.loadUntranslated(UntranslatedString.EXTENSION_SCORES);
    private static final String SOLUTION_EXTENSION = GlobalStrings
	    .loadUntranslated(UntranslatedString.EXTENSION_SOLUTION);

    public static String getArenaExtension() {
	return FileExtensions.ARENA_EXTENSION;
    }

    public static String getArenaExtensionWithPeriod() {
	return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.ARENA_EXTENSION;
    }

    public static String getArenaLevelExtensionWithPeriod() {
	return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.ARENA_LEVEL_EXTENSION;
    }

    public static String getGameExtension() {
	return FileExtensions.SAVED_GAME_EXTENSION;
    }

    public static String getGameExtensionWithPeriod() {
	return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.SAVED_GAME_EXTENSION;
    }
    
    public static String getImageExtensionWithPeriod() {
	return ".png";
    }

    public static String getOldLevelExtension() {
	return FileExtensions.OLD_LEVEL_EXTENSION;
    }

    public static String getOldPlaybackExtension() {
	return FileExtensions.OLD_PLAYBACK_EXTENSION;
    }

    public static String getPreferencesExtension() {
	return FileExtensions.SETTINGS_EXTENSION;
    }

    public static String getProtectedArenaExtension() {
	return FileExtensions.PROTECTED_ARENA_EXTENSION;
    }

    public static String getProtectedArenaExtensionWithPeriod() {
	return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.PROTECTED_ARENA_EXTENSION;
    }

    public static String getScoresExtensionWithPeriod() {
	return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.SCORES_EXTENSION;
    }

    public static String getSolutionExtensionWithPeriod() {
	return Strings.loadCommon(CommonString.NOTL_PERIOD) + FileExtensions.SOLUTION_EXTENSION;
    }

    private FileExtensions() {
	// Do nothing
    }
}