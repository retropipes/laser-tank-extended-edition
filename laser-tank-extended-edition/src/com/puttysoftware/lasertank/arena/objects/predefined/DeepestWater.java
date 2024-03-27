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
import com.puttysoftware.lasertank.index.Material;

public class DeepestWater extends ArenaObject {
    // Constructors
    public DeepestWater() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> {
	    final var i = new ArenaObject(GameObjectID.ICE);
	    i.setPreviousState(this);
	    yield i;
	}
	case FIRE -> new ArenaObject(GameObjectID.DEEPER_WATER);
	default -> this;
	};
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.DEEPEST_WATER;
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
	// Get rid of pushed object
	Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, pushed.getLayer());
	if (pushed.isBox()) {
	    if (pushed.getMaterial() == Material.WOODEN) {
		Game.get().morph(new ArenaObject(GameObjectID.BRIDGE), x, y, z, this.getLayer());
	    } else {
		Game.get().morph(new ArenaObject(GameObjectID.DEEPER_WATER), x, y, z, this.getLayer());
	    }
	}
	Sounds.play(Sound.SINK);
	return false;
    }
}
