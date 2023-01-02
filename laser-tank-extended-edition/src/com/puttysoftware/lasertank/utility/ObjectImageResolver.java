package com.puttysoftware.lasertank.utility;

import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class ObjectImageResolver {
    public static String getImageName(final GameObjectID objID) {
	return Integer.toString(objID.ordinal());
    }

    public static String getImageName(final GameObjectID objID, final int frameID) {
	return Integer.toString(objID.ordinal()) + "_" + Integer.toString(frameID);
    }

    public static String getImageName(final GameObjectID objID, final Direction dir) {
	return Integer.toString(objID.ordinal()) + "_d" + DirectionHelper.toStringValue(dir);
    }

    public static String getImageName(final GameObjectID objID, final Direction dir, final int frameID) {
	return Integer.toString(objID.ordinal()) + "_d" + DirectionHelper.toStringValue(dir) + "_"
		+ Integer.toString(frameID);
    }

    // Private constructor
    private ObjectImageResolver() {
	// Do nothing
    }
}
