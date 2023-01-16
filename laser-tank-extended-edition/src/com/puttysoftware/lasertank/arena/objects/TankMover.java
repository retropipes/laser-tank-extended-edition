/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractMover;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class TankMover extends AbstractMover {
    // Constructors
    public TankMover() {
	this.addType(GameType.MOVER);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.TANK_MOVER;
    }
}