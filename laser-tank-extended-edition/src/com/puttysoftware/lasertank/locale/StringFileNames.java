package com.puttysoftware.lasertank.locale;

class StringFileNames {
    // Static fields
    private static String[] LIST = new String[] { "difficulty", "errors", "settings", "generic", "objects", "menus",
	    "dialogs", "messages", "editor", "game", "time" };

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
