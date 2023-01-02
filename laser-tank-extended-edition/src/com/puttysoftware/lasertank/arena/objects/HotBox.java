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

public class HotBox extends AbstractMovableObject {
    // Constructors
    public HotBox() {
	super(true);
	this.addType(GameType.BOX);
	this.setMaterial(Material.FIRE);
    }

    @Override
    public AbstractArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case ICE:
	    return new Box();
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.HOT_BOX;
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_BOX);
    }
}