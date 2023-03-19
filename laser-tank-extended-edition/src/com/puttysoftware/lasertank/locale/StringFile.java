/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.locale;

import java.util.Locale;

enum StringFile {
    DIALOGS,
    DIFFICULTY,
    EDITOR,
    ERRORS,
    GAME,
    GENERIC,
    MENUS,
    MESSAGES,
    OVERRIDES,
    SETTINGS,
    TIME,
    OBJECT_CUSTOM_TEXT;

    public String getName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
