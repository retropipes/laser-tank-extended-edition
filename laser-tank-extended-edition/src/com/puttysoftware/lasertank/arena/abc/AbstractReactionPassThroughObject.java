/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.abc;

import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;

public abstract class AbstractReactionPassThroughObject extends AbstractPassThroughObject {
    // Constructors
    protected AbstractReactionPassThroughObject() {
    }

    @Override
    public final Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    return this.laserEnteredActionHook(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    public abstract Direction laserEnteredActionHook(int locX, int locY, int locZ, int dirX, int dirY,
	    LaserType laserType, int forceUnits);

    @Override
    public final boolean rangeAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    return this.rangeActionHook(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
	}
	return super.rangeAction(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
    }

    public abstract boolean rangeActionHook(int locX, int locY, int locZ, int dirX, int dirY, RangeType rangeType,
	    int forceUnits);
}