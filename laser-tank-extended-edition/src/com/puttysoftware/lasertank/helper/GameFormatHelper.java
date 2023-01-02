/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.GameFormat;

public class GameFormatHelper {
    public static final GameFormat FORMAT_LATEST = GameFormat.FORMAT_18;

    public static GameFormat fromOrdinal(final int value) {
	return GameFormat.values()[value];
    }

    public final static boolean isValidG1(final GameFormat thing) {
	return thing == GameFormat.FORMAT_5 || thing == GameFormat.FORMAT_6;
    }

    public final static boolean isValidG2(final GameFormat thing) {
	return thing == GameFormat.FORMAT_7 || thing == GameFormat.FORMAT_8;
    }

    public final static boolean isValidG3(final GameFormat thing) {
	return thing == GameFormat.FORMAT_9;
    }

    public final static boolean isValidG4(final GameFormat thing) {
	return thing == GameFormat.FORMAT_10 || thing == GameFormat.FORMAT_11;
    }

    public final static boolean isValidG5(final GameFormat thing) {
	return thing == GameFormat.FORMAT_12 || thing == GameFormat.FORMAT_13 || thing == GameFormat.FORMAT_14
		|| thing == GameFormat.FORMAT_15 || thing == GameFormat.FORMAT_16;
    }

    public final static boolean isValidG6(final GameFormat thing) {
	return thing == GameFormat.FORMAT_17 || thing == GameFormat.FORMAT_18;
    }

    public final static boolean isLevelListStored(final GameFormat thing) {
	return thing.ordinal() >= GameFormat.FORMAT_17.ordinal();
    }

    public final static boolean isMoveShootAllowed(final GameFormat thing) {
	return thing.ordinal() >= GameFormat.FORMAT_11.ordinal();
    }
}
