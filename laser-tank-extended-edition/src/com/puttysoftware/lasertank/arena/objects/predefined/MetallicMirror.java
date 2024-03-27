/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class MetallicMirror extends ArenaObject {
    // Constructors
    public MetallicMirror() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_MIRROR;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var dir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
	if (this.hitReflectiveSide(dir)) {
	    // Reflect laser
	    return this.getDirection();
	}
	// Move mirror
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_MIRROR;
    }
}
