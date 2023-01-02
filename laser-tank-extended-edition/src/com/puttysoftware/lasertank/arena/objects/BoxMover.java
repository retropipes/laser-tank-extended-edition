/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMover;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class BoxMover extends AbstractMover {
    // Constructors
    public BoxMover() {
	super(true);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
	this.addType(GameType.BOX_MOVER);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.BOX_MOVER;
    }
}