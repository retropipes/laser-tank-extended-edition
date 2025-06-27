package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.locale.global.DataLoaderString;
import com.puttysoftware.lasertank.locale.global.GlobalStrings;

class ArenaObjectImageResolver {
    public static String getImageName(final GameObjectID objID) {
        return Integer.toString(objID.ordinal());
    }

    public static String getImageName(final GameObjectID objID, final Direction dir) {
        return Integer.toString(objID.ordinal()) + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR)
                + DirectionHelper.toStringValue(dir);
    }

    public static String getImageName(final GameObjectID objID, final Direction dir, final int frameID) {
        return Integer.toString(objID.ordinal()) + GlobalStrings.loadDataLoader(DataLoaderString.DIRECTION_SEPARATOR)
                + DirectionHelper.toStringValue(dir) + GlobalStrings.loadDataLoader(DataLoaderString.SUB_SEPARATOR)
                + Integer.toString(frameID);
    }

    public static String getImageName(final GameObjectID objID, final int frameID) {
        return Integer.toString(objID.ordinal()) + GlobalStrings.loadDataLoader(DataLoaderString.SUB_SEPARATOR)
                + Integer.toString(frameID);
    }

    // Private constructor
    private ArenaObjectImageResolver() {
        // Do nothing
    }
}