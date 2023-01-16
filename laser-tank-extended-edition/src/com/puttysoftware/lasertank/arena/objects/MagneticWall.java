/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractWall;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class MagneticWall extends AbstractWall {
    // Constructors
    public MagneticWall() {
	this.addType(GameType.PLAIN_WALL);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MAGNETIC_WALL;
    }
}