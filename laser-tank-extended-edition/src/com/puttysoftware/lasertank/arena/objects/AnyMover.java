/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMover;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;

public class AnyMover extends AbstractMover {
    // Constructors
    public AnyMover() {
	super();
	this.addType(GameType.MOVER);
	this.addType(GameType.ANTI_MOVER);
	this.addType(GameType.BOX_MOVER);
	this.addType(GameType.MIRROR_MOVER);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.ANY_MOVER;
    }
}