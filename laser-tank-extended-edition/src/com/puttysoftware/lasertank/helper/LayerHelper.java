/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.Layer;
import com.puttysoftware.lasertank.locale.EditorString;
import com.puttysoftware.lasertank.locale.Strings;

public class LayerHelper {
    public static final int COUNT = 4;
    public static final int VIRTUAL_COUNT = 1;
    private static String[] NAMES = null;

    public static void activeLanguageChanged() {
        LayerHelper.NAMES = new String[] { Strings.loadEditor(EditorString.LOWER_GROUND_LAYER),
                Strings.loadEditor(EditorString.UPPER_GROUND_LAYER),
                Strings.loadEditor(EditorString.LOWER_OBJECTS_LAYER),
                Strings.loadEditor(EditorString.UPPER_OBJECTS_LAYER) };
    }

    public static Layer fromOrdinal(final int value) {
        return Layer.values()[value];
    }

    public static String[] getNames() {
        if (LayerHelper.NAMES == null) {
            LayerHelper.activeLanguageChanged();
        }
        return LayerHelper.NAMES;
    }
}
