/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.helper;

import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;

public class LaserTypeHelper {
    public static RangeType rangeType(final LaserType thing) {
	switch (thing) {
	case STUNNER:
	    return RangeType.ICE_BOMB;
	case MISSILE:
	    return RangeType.HEAT_BOMB;
	default:
	    return RangeType.BOMB;
	}
    }
}
