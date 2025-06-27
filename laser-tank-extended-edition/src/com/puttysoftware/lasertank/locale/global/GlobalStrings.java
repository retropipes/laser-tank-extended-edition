/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.locale.global;

import java.util.ResourceBundle;

import com.puttysoftware.lasertank.helper.IndexHelper;
import com.puttysoftware.lasertank.index.Index;

public class GlobalStrings {
    public static String loadDataLoader(final DataLoaderString str) {
        return GlobalStrings.loadFile(GlobalFile.DATA_LOADER).getString(Integer.toString(str.ordinal()));
    }

    private static ResourceBundle loadFile(final GlobalFile file) {
        return ResourceBundle.getBundle("locale." + GlobalFileNames.getFileName(file)); //$NON-NLS-1$
    }

    public static String loadIndex(final Index index) {
        return GlobalStrings.loadFile(GlobalFile.LANGUAGES).getString(IndexHelper.toStringValue(index));
    }

    public static String loadIndex(final int index) {
        return GlobalStrings.loadFile(GlobalFile.LANGUAGES).getString(Integer.toString(index));
    }

    public static String loadLanguage(final int strID) {
        return GlobalStrings.loadFile(GlobalFile.LANGUAGES).getString(Integer.toString(strID));
    }

    public static String loadUntranslated(final UntranslatedString str) {
        return GlobalStrings.loadFile(GlobalFile.NOT_TRANSLATED).getString(Integer.toString(str.ordinal()));
    }
}
