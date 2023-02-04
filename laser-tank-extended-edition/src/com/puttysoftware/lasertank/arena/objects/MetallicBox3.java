/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;

public class MetallicBox3 extends AbstractMovableObject {
    // Constructors
    public MetallicBox3() {
	super();
	this.addType(GameType.BOX);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> {
	    final var ib = new IcyBox3();
	    ib.setPreviousState(this);
	    yield ib;
	}
	case FIRE -> new HotBox3();
	default -> this;
	};
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_BOX_3;
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_BOX);
    }
}