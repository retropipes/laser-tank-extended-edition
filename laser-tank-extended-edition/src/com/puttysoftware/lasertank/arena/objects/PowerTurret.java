/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class PowerTurret extends ArenaObject {
    // Fields
    private boolean autoMove;
    private boolean canShoot;

    // Constructors
    public PowerTurret() {
	this.activateTimer(1);
	this.canShoot = true;
	this.autoMove = false;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.POWER_TURRET;
    }

    @Override
    public void kill(final int locX, final int locY) {
	if (this.canShoot) {
	    Game.get().setLaserType(LaserType.POWER);
	    Game.get().fireLaser(locX, locY, this);
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
	if (laserType == LaserType.MISSILE) {
	    // Kill
	    final var gm = Game.get();
	    final var dat = new ArenaObject(GameObjectID.DEAD_ANTI_TANK);
	    dat.setSavedObject(this.getSavedObject());
	    dat.setDirection(baseDir);
	    gm.morph(dat, locX, locY, locZ, this.getLayer());
	    Sounds.play(Sound.ANTI_DIE);
	    return Direction.NONE;
	}
	if (laserType == LaserType.STUNNER) {
	    // Stun
	    final var gm = Game.get();
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
	    final var gm = Game.get();
	    final var dat = new ArenaObject(GameObjectID.DEAD_ANTI_TANK);
	    dat.setSavedObject(this.getSavedObject());
	    dat.setDirection(baseDir);
	    gm.morph(dat, locX, locY, locZ, this.getLayer());
	    Sounds.play(Sound.ANTI_DIE);
	    return Direction.NONE;
	}
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public Sound laserEnteredSound() {
	return Sound.PUSH_ANTI_TANK;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	final var moveDir = this.getSavedObject().getDirection();
	if (this.getSavedObject().movesHostiles(moveDir)) {
	    final var unres = DirectionHelper.unresolveRelative(moveDir);
	    if (Game.canObjectMove(locX, locY, unres[0], unres[1])) {
		if (this.autoMove) {
		    this.autoMove = false;
		    Game.get().updatePushedPosition(locX, locY, locX + unres[0], locY + unres[1], this);
		}
	    } else {
		this.autoMove = true;
	    }
	}
	this.activateTimer(1);
    }
}
