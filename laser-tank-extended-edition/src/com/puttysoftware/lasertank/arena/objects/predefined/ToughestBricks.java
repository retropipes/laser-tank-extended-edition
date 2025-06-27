/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;

public class ToughestBricks extends ArenaObject {
    // Constructors
    public ToughestBricks() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.TOUGHEST_BRICKS;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	Game.morph(new ArenaObject(GameObjectID.TOUGHER_BRICKS), locX, locY, locZ, this.layer());
	if (laserType == LaserType.POWER) {
	    // Laser keeps going
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Laser stops
	return Direction.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	Game.morph(new ArenaObject(GameObjectID.TOUGHER_BRICKS), locX + dirX, locY + dirY, locZ, this.layer());
	return true;
    }
}