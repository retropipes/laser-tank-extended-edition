/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

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
		return switch (thing) {
			case BOMB -> Material.METALLIC;
			case HEAT_BOMB -> Material.FIRE;
			case ICE_BOMB -> Material.ICE;
			default -> Material.NONE;
		};
	}
}
