/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.utility.TankInventory;

public class TenBlueLasers extends ArenaObject {
    // Constructors
    public TenBlueLasers() {
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.TEN_BLUE_LASERS;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
	final var gm = Game.get();
	TankInventory.addTenBlueLasers();
	gm.morph(new ArenaObject(GameObjectID.PLACEHOLDER), dirX, dirY, dirZ, this.getLayer());
    }
}
