/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class MetallicBox4 extends ArenaObject {
    // Constructors
    public MetallicBox4() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> {
	    final var ib = new IcyBox4();
	    ib.setPreviousState(this);
	    yield ib;
	}
	case FIRE -> new HotBox4();
	default -> this;
	};
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_BOX_4;
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_BOX;
    }
}