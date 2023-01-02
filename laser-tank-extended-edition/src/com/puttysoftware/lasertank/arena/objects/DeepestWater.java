/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractGround;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;

public class DeepestWater extends AbstractGround {
    // Constructors
    public DeepestWater() {
	super();
	this.setFrameNumber(1);
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case ICE:
	    final Ice i = new Ice();
	    i.setPreviousState(this);
	    return i;
	case FIRE:
	    return new DeeperWater();
	default:
	    return this;
	}
    }

    @Override
    public int getBlockHeight() {
	return -4;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.DEEPEST_WATER;
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final Application app = LaserTankEE.getApplication();
	// Get rid of pushed object
	app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
	if (pushed.isOfType(GameType.BOX)) {
	    if (pushed.getMaterial() == Material.WOODEN) {
		app.getGameManager().morph(new Bridge(), x, y, z, this.getLayer());
	    } else {
		app.getGameManager().morph(new DeeperWater(), x, y, z, this.getLayer());
	    }
	}
	Sounds.play(Sound.SINK);
	return false;
    }
}
