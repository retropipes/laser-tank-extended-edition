/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.Era;
import com.puttysoftware.lasertank.locale.Strings;

public class EraHelper {
    private static String[] NAMES = null;

    public static Era fromOrdinal(final int value) {
        return Era.values()[value];
    }

    public static String getName(final Era thing) {
        return Strings.loadEra(thing);
    }

    public static String[] getNames() {
        if (EraHelper.NAMES == null) {
            EraHelper.activeLanguageChanged();
        }
        return EraHelper.NAMES;
    }

    public static void activeLanguageChanged() {
        EraHelper.NAMES = new String[] { Strings.loadEra(Era.DISTANT_PAST), Strings.loadEra(Era.PAST),
                Strings.loadEra(Era.PRESENT), Strings.loadEra(Era.FUTURE), Strings.loadEra(Era.DISTANT_FUTURE) };
    }
}
