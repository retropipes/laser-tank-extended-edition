/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class Mirror extends ArenaObject {
    // Constructors
    public Mirror() {
        super();
    }

    @Override
    public boolean doLasersPassThrough() {
        return true;
    }

    @Override
    public final GameObjectID getID() {
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
        }
        return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
	public Sound laserEnteredSound() {
		return Sound.PUSH_MIRROR;
	}
}
