/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractGround;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class Lava extends AbstractGround {
    // Constructors
    public Lava() {
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case ICE -> new Ground();
	default -> this;
	};
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.LAVA;
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final var app = LaserTankEE.getApplication();
	if (pushed instanceof IcyBox) {
	    app.getGameManager().morph(new Ground(), x, y, z, this.getLayer());
	    Sounds.play(Sound.COOL_OFF);
	    return true;
	}
	app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
	Sounds.play(Sound.MELT);
	return false;
    }
}
