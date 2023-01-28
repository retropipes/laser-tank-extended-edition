/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractCharacter;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;

public class MoltenTank extends AbstractCharacter {
    public MoltenTank(final Direction dir, final int number) {
	super(number);
    }

    // Constructors
    public MoltenTank(final int number) {
	super(number);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MOLTEN_TANK;
    }
}