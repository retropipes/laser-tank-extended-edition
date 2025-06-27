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
import com.puttysoftware.lasertank.index.Layer;

public class Crumbling extends ArenaObject {
    // Constructors
    public Crumbling() {
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.CRUMBLING;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final LaserType laserType, final int forceUnits) {
        Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
        // Destroy whatever we were attached to
        Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, Layer.LOWER_OBJECTS.ordinal());
        Sounds.play(Sound.CRACK);
        return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
        Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
        // Destroy whatever we were attached to
        Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, Layer.LOWER_OBJECTS.ordinal());
        Sounds.play(Sound.CRACK);
    }
}