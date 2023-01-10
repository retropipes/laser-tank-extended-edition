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
    OBJECTS,
    OVERRIDES,
    SETTINGS,
    TIME,
    OBJECT_CUSTOM_TEXT;
    
    public String getName() {
	return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
