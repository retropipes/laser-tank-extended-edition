package com.puttysoftware.lasertank.locale.global;

class GlobalFileNames {
    // Static fields
    private static String[] LIST = { "languages", "notranslate", "index_suffix", "data_loader" };

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
