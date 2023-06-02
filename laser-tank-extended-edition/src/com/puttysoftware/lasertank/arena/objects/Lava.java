/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class Lava extends ArenaObject {
    // Constructors
    public Lava() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> new LavaBridge();
	default -> this;
	};
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.LAVA;
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
	if (pushed instanceof IcyBox) {
	    Game.get().morph(new LavaBridge(), x, y, z, this.getLayer());
	    Sounds.play(Sound.COOL_OFF);
	    return true;
	}
	Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.getLayer());
	Sounds.play(Sound.MELT);
	return false;
    }
}
