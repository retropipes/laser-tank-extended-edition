/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;

public class KillerSkull extends ArenaObject {
    // Constructors
    public KillerSkull() {
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.KILLER_SKULL;
    }

    // Scriptability
    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
        Sounds.play(Sound.KILL);
        LaserTankEE.getArenaManager().getArena().fullScanKillTanks();
        LaserTankEE.getGame().morph(new ArenaObject(GameObjectID.PLACEHOLDER), dirX, dirY, dirZ, this.getLayer());
    }
}