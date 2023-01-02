/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractWall;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;

public class HotWall extends AbstractWall {
    // Constructors
    public HotWall() {
	super();
	this.addType(GameType.PLAIN_WALL);
	this.setMaterial(Material.FIRE);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case ICE:
	    return new Wall();
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.HOT_WALL;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	if (laserType == LaserType.STUNNER) {
	    // Cool off hot wall
	    Sounds.play(Sound.COOL_OFF);
	    LaserTankEE.getApplication().getGameManager().morph(new Wall(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }
}