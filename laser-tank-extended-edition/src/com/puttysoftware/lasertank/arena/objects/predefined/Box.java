/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class Box extends ArenaObject {
    // Constructors
    public Box() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> {
	    final var ib = new ArenaObject(GameObjectID.ICY_BOX);
	    ib.setPreviousState(this);
	    yield ib;
	}
	case FIRE -> new ArenaObject(GameObjectID.HOT_BOX);
	default -> this;
	};
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.BOX;
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_BOX;
    }
}