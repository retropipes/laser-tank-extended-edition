/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.engine.internal;

import java.util.ResourceBundle;

final class AckInternalStrings {
    static String load(final int index) {
	return ResourceBundle.getBundle("locale.ack.internal").getString(Integer.toString(index));
    }

    private AckInternalStrings() {
    }
}
