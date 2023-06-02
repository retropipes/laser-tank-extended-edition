/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class MoltenTank extends ArenaObject {
    public MoltenTank(final Direction dir, final int number) {
	super(GameObjectID.MOLTEN_TANK, dir, number);
    }

    // Constructors
    public MoltenTank(final int number) {
	super(GameObjectID.MOLTEN_TANK, number);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MOLTEN_TANK;
    }
}