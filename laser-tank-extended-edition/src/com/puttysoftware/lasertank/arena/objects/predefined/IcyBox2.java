/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class IcyBox2 extends ArenaObject {
    // Constructors
    public IcyBox2() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    }
	    return new ArenaObject(GameObjectID.BOX_2);
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ICY_BOX_2;
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_BOX;
    }
}