/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractAttribute;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class Weakened extends AbstractAttribute {
    // Constructors
    public Weakened() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.WEAKENED;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var app = LaserTankEE.getApplication();
	app.getGameManager().morph(new Cracked(), locX, locY, locZ, this.getLayer());
	Sounds.play(Sound.CRACK);
	return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final var app = LaserTankEE.getApplication();
	app.getGameManager().morph(new Cracked(), locX, locY, locZ, this.getLayer());
	Sounds.play(Sound.CRACK);
    }
}