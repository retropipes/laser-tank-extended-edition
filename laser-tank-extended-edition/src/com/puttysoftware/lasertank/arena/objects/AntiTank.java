/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;

public class AntiTank extends AbstractMovableObject {
    // Fields
    private boolean autoMove;
    private boolean canShoot;

    // Constructors
    public AntiTank() {
	super();
	this.activateTimer(1);
	this.canShoot = true;
	this.autoMove = false;
	this.addType(GameType.ANTI);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ANTI_TANK;
    }

    public void kill(final int locX, final int locY) {
	if (this.canShoot) {
	    LaserTankEE.getApplication().getGameManager().setLaserType(LaserType.RED);
	    LaserTankEE.getApplication().getGameManager().fireLaser(locX, locY, this);
	    this.canShoot = false;
	}
    }

    @Override
    public void laserDoneAction() {
	this.canShoot = true;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var baseDir = this.getDirection();
	if (laserType == LaserType.MISSILE || laserType == LaserType.POWER) {
	    // Kill
	    final var gm = LaserTankEE.getApplication().getGameManager();
	    final var dat = new DeadAntiTank();
	    dat.setSavedObject(this.getSavedObject());
	    dat.setDirection(baseDir);
	    gm.morph(dat, locX, locY, locZ, this.getLayer());
	    Sounds.play(Sound.ANTI_DIE);
	    return Direction.NONE;
	}
	if (laserType == LaserType.STUNNER) {
	    // Stun
	    final var gm = LaserTankEE.getApplication().getGameManager();
	    final var sat = new StunnedAntiTank();
	    sat.setSavedObject(this.getSavedObject());
	    sat.setDirection(baseDir);
	    gm.morph(sat, locX, locY, locZ, this.getLayer());
	    Sounds.play(Sound.STUN);
	    return Direction.NONE;
	}
	final var sourceDir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
	if (sourceDir == baseDir) {
	    // Kill
	    final var gm = LaserTankEE.getApplication().getGameManager();
	    final var dat = new DeadAntiTank();
	    dat.setSavedObject(this.getSavedObject());
	    dat.setDirection(baseDir);
	    gm.morph(dat, locX, locY, locZ, this.getLayer());
	    Sounds.play(Sound.ANTI_DIE);
	    return Direction.NONE;
	} else {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_ANTI_TANK);
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	if (this.getSavedObject().isOfType(GameType.ANTI_MOVER)) {
	    final var moveDir = this.getSavedObject().getDirection();
	    final var unres = DirectionHelper.unresolveRelative(moveDir);
	    if (Game.canObjectMove(locX, locY, unres[0], unres[1])) {
		if (this.autoMove) {
		    this.autoMove = false;
		    LaserTankEE.getApplication().getGameManager().updatePushedPosition(locX, locY, locX + unres[0],
			    locY + unres[1], this);
		}
	    } else {
		this.autoMove = true;
	    }
	}
	this.activateTimer(1);
    }
}
