/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.GameObjectID;

public class GameObjectIDHelper {
    public static GameObjectID fromStringValue(final String value) {
	return GameObjectID.values()[Integer.parseInt(value)];
    }
}
