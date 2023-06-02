/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.internal;

import java.util.ResourceBundle;

public final class PrivateStrings {
    public static String error(final PrivateErrorString item) {
	return ResourceBundle.getBundle("locale.diane.error").getString(Integer.toString(item.ordinal()));
    }

    private PrivateStrings() {
    }
}
