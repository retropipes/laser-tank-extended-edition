/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class DeadAntiTank extends AbstractMovableObject {
    // Constructors
    public DeadAntiTank() {
	super();
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.DEAD_ANTI_TANK;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	LaserTankEE.getApplication().getGameManager().haltMovingObjects();
	if (laserType != LaserType.MISSILE) {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Destroy
	Sounds.play(Sound.BOOM);
	LaserTankEE.getApplication().getGameManager().morph(this.getSavedObject(), locX, locY, locZ, this.getLayer());
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	// Do nothing
    }
}
