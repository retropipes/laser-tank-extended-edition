package com.puttysoftware.lasertank.locale.global;

class GlobalFileNames {
    // Static fields
    private static String[] LIST = new String[] { "languages", "notranslate", "direction_suffix", "index_suffix" };

    // Private constructor
    private GlobalFileNames() {
	// Do nothing
    }

    // Static methods
    static int getFileCount() {
	return GlobalFileNames.LIST.length;
    }

    static String getFileName(final GlobalFile file) {
	return GlobalFileNames.LIST[file.ordinal()];
    }
}
