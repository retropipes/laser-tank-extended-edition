/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class RangeTypeHelper {
    public static RangeType fromOrdinal(final int value) {
	return RangeType.values()[value];
    }

    public static Material material(final RangeType thing) {
	switch (thing) {
	case BOMB:
	    return Material.METALLIC;
	case HEAT_BOMB:
	    return Material.FIRE;
	case ICE_BOMB:
	    return Material.ICE;
	default:
	    return Material.NONE;
	}
    }
}
