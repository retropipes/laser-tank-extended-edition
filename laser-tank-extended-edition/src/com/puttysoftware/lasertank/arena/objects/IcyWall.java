/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;

public class IcyWall extends ArenaObject {
    // Constructors
    public IcyWall() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    }
	    return new ArenaObject(GameObjectID.WALL);
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ICY_WALL;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	if (laserType != LaserType.MISSILE) {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Defrost icy wall
	Sounds.play(Sound.DEFROST);
	ArenaObject ao;
	if (this.hasPreviousState()) {
	    ao = this.getPreviousState();
	} else {
	    ao = new ArenaObject(GameObjectID.WALL);
	}
	Game.get().morph(ao, locX, locY, locZ, this.getLayer());
	return Direction.NONE;
    }
}