/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Bomb extends ArenaObject {
    // Constructors
    public Bomb() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.BOMB;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
	this.nextIndex();
    }
}
