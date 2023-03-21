/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class ThinIce extends ArenaObject {
    // Constructors
    public ThinIce() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
        return switch (materialID) {
            case ICE -> {
                final var i = new ArenaObject(GameObjectID.ICE);
                i.setPreviousState(this);
                yield i;
            }
            case FIRE -> new ArenaObject(GameObjectID.WATER);
            default -> this;
        };
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.THIN_ICE;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
        Sounds.play(Sound.PUSH_MIRROR);
        LaserTankEE.getGame().remoteDelayedDecayTo(new ArenaObject(GameObjectID.WATER));
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
        LaserTankEE.getGame().remoteDelayedDecayTo(new ArenaObject(GameObjectID.WATER));
        return true;
    }
}