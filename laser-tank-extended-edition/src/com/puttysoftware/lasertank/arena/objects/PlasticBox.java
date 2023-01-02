/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.Material;

public class PlasticBox extends AbstractMovableObject {
    // Constructors
    public PlasticBox() {
	super(true);
	this.addType(GameType.BOX);
	this.setMaterial(Material.PLASTIC);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case ICE:
	    final IcyBox ib = new IcyBox();
	    ib.setPreviousState(this);
	    return ib;
	case FIRE:
	    return new HotBox();
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.PLASTIC_BOX;
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_BOX);
    }
}