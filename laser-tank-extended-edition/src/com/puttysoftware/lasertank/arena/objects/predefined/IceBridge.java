/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class IceBridge extends ArenaObject {
    // Constructors
    public IceBridge() {
    }

    @Override
    public ArenaObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    }
	    return new ArenaObject(GameObjectID.BRIDGE);
	default:
	    return this;
	}
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ICE_BRIDGE;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
	Sounds.play(Sound.PUSH_MIRROR);
    }

    @Override
    public boolean pushIntoAction(final ArenaObject pushed, final int x, final int y, final int z) {
	if (pushed.getID() == GameObjectID.HOT_BOX) {
	    pushed.setSavedObject(new ArenaObject(GameObjectID.BRIDGE));
	}
	return true;
    }
}