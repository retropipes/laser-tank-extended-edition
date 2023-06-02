/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PowerBolt extends ArenaObject {
    // Constructors
    public PowerBolt() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.POWER_BOLT;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
	Sounds.play(Sound.POWERFUL);
	Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), dirX, dirY, dirZ, this.getLayer());
	Game.get().setPowerfulTank();
    }
}