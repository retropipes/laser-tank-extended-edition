/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.locale;

import java.util.ResourceBundle;

import com.puttysoftware.lasertank.engine.direction.Direction;

public final class LTEStrings {
    public static final String EMPTY = "";

    public static String direction(final Direction item) {
	return ResourceBundle.getBundle("locale.engine.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String directionSuffix(final Direction item) {
	return ResourceBundle.getBundle("locale.engine.dirsuffix").getString(Integer.toString(item.ordinal()));
    }

    public static String subst(final String orig, final String... values) {
	var result = orig;
	for (var s = 0; s < values.length; s++) {
	    result = result.replace("%" + s, values[s]);
	}
	return result;
    }

    private LTEStrings() {
    }
}
