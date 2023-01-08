/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;

public class Mirror extends AbstractMovableObject {
    // Constructors
    public Mirror() {
	super(true);
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
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_MIRROR);
    }
}
