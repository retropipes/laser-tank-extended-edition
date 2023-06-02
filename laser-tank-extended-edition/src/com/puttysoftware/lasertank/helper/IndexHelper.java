/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.Index;

public class IndexHelper {
    public static String toStringValue(final Index thing) {
	return Integer.toString(thing.ordinal());
    }
}
