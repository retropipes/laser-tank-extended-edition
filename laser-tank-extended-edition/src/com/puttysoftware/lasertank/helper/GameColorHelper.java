/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.GameColor;

public class GameColorHelper {
    public static final int COUNT = 16;

    public static GameColor fromOrdinal(final int value) {
        return GameColor.values()[value];
    }

    public static GameColor fromStringValue(final String value) {
        return GameColor.values()[Integer.parseInt(value)];
    }
}
