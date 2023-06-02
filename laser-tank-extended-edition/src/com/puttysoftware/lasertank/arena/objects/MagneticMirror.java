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

public class MagneticMirror extends ArenaObject {
    // Constructors
    public MagneticMirror() {
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MAGNETIC_MIRROR;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	if (laserType == LaserType.MISSILE) {
	    // Destroy mirror
	    Sounds.play(Sound.BOOM);
	    Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_MIRROR;
    }
}
