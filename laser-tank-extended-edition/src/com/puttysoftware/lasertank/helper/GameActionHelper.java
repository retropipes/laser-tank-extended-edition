/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.GameAction;

public class GameActionHelper {
    public static GameAction fromOrdinal(final int value) {
	return GameAction.values()[value];
    }

    public static GameAction fromStringValue(final String value) {
	return GameAction.values()[Integer.parseInt(value)];
    }

    public static boolean isMove(final GameAction thing) {
	return thing == GameAction.MOVE;
    }

    public static boolean isNonMove(final GameAction thing) {
	return thing != GameAction.MOVE;
    }
}
