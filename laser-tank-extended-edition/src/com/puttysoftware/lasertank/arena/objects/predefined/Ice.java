/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Ice extends ArenaObject {
    public Ice() {
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.ICE;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
        Sounds.play(Sound.PUSH_MIRROR);
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
        if (pushed.getID() == GameObjectID.HOT_BOX) {
            final var g = new ArenaObject(GameObjectID.GROUND);
            Game.morph(g, x, y, z, g.layer());
            Sounds.play(Sound.DEFROST);
        }
        return true;
    }
}
