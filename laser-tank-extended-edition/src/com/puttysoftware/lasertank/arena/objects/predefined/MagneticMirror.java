/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class MagneticMirror extends ArenaObject {
    // Constructors
    public MagneticMirror() {
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
            Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
            return Direction.NONE;
        }
        return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }
}
