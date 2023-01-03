package com.puttysoftware.lasertank.locale;

class StringFileNames {
    // Static fields
    private static String[] LIST = new String[] { "dialogs", "difficulty", "editor", "errors", "game", "generic",
	    "menus", "messages", "objects", "overrides", "settings", "time" };

    // Private constructor
    private StringFileNames() {
	// Do nothing
    }

    // Static methods
    static int getFileCount() {
	return StringFileNames.LIST.length;
    }

    static String getFileName(final StringFile file) {
	return StringFileNames.LIST[file.ordinal()];
    }
}
