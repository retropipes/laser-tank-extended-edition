package com.puttysoftware.lasertank.locale.global;

import java.util.ResourceBundle;

import com.puttysoftware.lasertank.helper.IndexHelper;
import com.puttysoftware.lasertank.index.Index;

public class GlobalStrings {
    private static ResourceBundle loadFile(final GlobalFile file) {
	return ResourceBundle.getBundle("locale." + GlobalFileNames.getFileName(file));
    }

    public static String loadLanguage(final int strID) {
	return GlobalStrings.loadFile(GlobalFile.LANGUAGES).getString(Integer.toString(strID));
    }

    public static String loadUntranslated(final UntranslatedString str) {
	return GlobalStrings.loadFile(GlobalFile.NOT_TRANSLATED).getString(Integer.toString(str.ordinal()));
    }

    public static String loadDirection(final int dir) {
	return GlobalStrings.loadFile(GlobalFile.DIRECTIONS).getString(Integer.toString(dir));
    }

    public static String loadIndex(final int index) {
	return GlobalStrings.loadFile(GlobalFile.LANGUAGES).getString(Integer.toString(index));
    }

    public static String loadIndex(final Index index) {
	return GlobalStrings.loadFile(GlobalFile.LANGUAGES).getString(IndexHelper.toStringValue(index));
    }
}
