/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class Tank extends ArenaObject {
    public Tank(final Direction dir, final int number) {
	super(GameObjectID.TANK, dir, number);
    }

    // Constructors
    public Tank(final int number) {
	super(GameObjectID.TANK, number);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.TANK;
    }
}