/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;

public class LaserTypeHelper {
	public static RangeType rangeType(final LaserType thing) {
		return switch (thing) {
			case STUNNER -> RangeType.ICE_BOMB;
			case MISSILE -> RangeType.HEAT_BOMB;
			default -> RangeType.BOMB;
		};
	}
}
