/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.EditorLayout;
import com.puttysoftware.lasertank.locale.Strings;

public class EditorLayoutHelper {
    private static String[] NAMES = null;

    public static String toStringValue(final int thing) {
	return Integer.toString(thing);
    }

    public static String getName(final int thing) {
	return Strings.loadEditorLayout(thing);
    }

    public static String[] getNames() {
	if (EditorLayoutHelper.NAMES == null) {
	    EditorLayoutHelper.activeLanguageChanged();
	}
	return EditorLayoutHelper.NAMES;
    }

    public static void activeLanguageChanged() {
	EditorLayoutHelper.NAMES = new String[] { Strings.loadEditorLayout(EditorLayout.CLASSIC),
		Strings.loadEditorLayout(EditorLayout.MODERN_V11), Strings.loadEditorLayout(EditorLayout.MODERN_V12) };
    }

    public static int fromStringValue(final String value) {
	return Integer.parseInt(value);
    }

    private EditorLayoutHelper() {
	// Do nothing
    }
}
