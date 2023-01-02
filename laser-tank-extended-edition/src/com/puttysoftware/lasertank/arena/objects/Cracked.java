/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractAttribute;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class Cracked extends AbstractAttribute {
    // Constructors
    public Cracked() {
	super();
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.CRACKED;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final Application app = LaserTankEE.getApplication();
	app.getGameManager().morph(new Damaged(), locX, locY, locZ, this.getLayer());
	Sounds.play(Sound.CRACK);
	return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final Application app = LaserTankEE.getApplication();
	app.getGameManager().morph(new Damaged(), locX, locY, locZ, this.getLayer());
	Sounds.play(Sound.CRACK);
    }
}