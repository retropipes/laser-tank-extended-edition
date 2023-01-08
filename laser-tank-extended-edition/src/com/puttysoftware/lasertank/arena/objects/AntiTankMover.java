/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMover;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class AntiTankMover extends AbstractMover {
    // Constructors
    public AntiTankMover() {
	this.addType(GameType.ANTI_MOVER);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ANTI_TANK_MOVER;
    }
}