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

public class DeadAntiTank extends ArenaObject {
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
        Game.get().haltMovingObjects();
        if (laserType != LaserType.MISSILE) {
            return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
        }
        // Destroy
        Sounds.play(Sound.BOOM);
        Game.get().morph(this.getSavedObject(), locX, locY, locZ, this.getLayer());
        return Direction.NONE;
    }
}
