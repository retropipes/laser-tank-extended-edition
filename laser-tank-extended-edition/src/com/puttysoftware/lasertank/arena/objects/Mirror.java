/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;

public class Mirror extends AbstractMovableObject {
    // Constructors
    public Mirror() {
	super(true);
	this.setDirection(Direction.NORTHEAST);
	this.setDiagonalOnly(true);
	this.addType(GameType.MOVABLE_MIRROR);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.MIRROR;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	if (laserType == LaserType.MISSILE) {
	    // Destroy mirror
	    Sounds.play(Sound.BOOM);
	    LaserTankEE.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else {
	    final Direction dir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
	    if (AbstractArenaObject.hitReflectiveSide(dir)) {
		// Reflect laser
		return this.getDirection();
	    } else {
		// Move mirror
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	}
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType) {
	// Finish reflecting laser
	Sounds.play(Sound.REFLECT);
	final Direction oldlaser = DirectionHelper.resolveRelativeInvert(locX, locY);
	final Direction currdir = this.getDirection();
	if (oldlaser == Direction.NORTH) {
	    if (currdir == Direction.NORTHWEST) {
		return Direction.WEST;
	    } else if (currdir == Direction.NORTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.SOUTH) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.WEST;
	    } else if (currdir == Direction.SOUTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.WEST) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.SOUTH;
	    } else if (currdir == Direction.NORTHWEST) {
		return Direction.NORTH;
	    }
	} else if (oldlaser == Direction.EAST) {
	    if (currdir == Direction.SOUTHEAST) {
		return Direction.SOUTH;
	    } else if (currdir == Direction.NORTHEAST) {
		return Direction.NORTH;
	    }
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_MIRROR);
    }
}
