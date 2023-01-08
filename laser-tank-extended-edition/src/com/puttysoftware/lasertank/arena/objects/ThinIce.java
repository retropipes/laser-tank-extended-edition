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

public class ThinIce extends AbstractGround {
    // Constructors
    public ThinIce() {
	super();
	this.setMaterial(Material.ICE);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case ICE:
	    final Ice i = new Ice();
	    i.setPreviousState(this);
	    return i;
	case FIRE:
	    return new Water();
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.THIN_ICE;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	Sounds.play(Sound.PUSH_MIRROR);
	LaserTankEE.getApplication().getGameManager().remoteDelayedDecayTo(new Water());
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	LaserTankEE.getApplication().getGameManager().remoteDelayedDecayTo(new Water());
	return true;
    }
}