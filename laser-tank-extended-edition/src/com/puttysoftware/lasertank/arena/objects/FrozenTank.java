/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractCharacter;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class FrozenTank extends AbstractCharacter {
    public FrozenTank(final Direction dir, final int number) {
	super(number);
    }

    // Constructors
    public FrozenTank(final int number) {
	super(number);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.FROZEN_TANK;
    }
}