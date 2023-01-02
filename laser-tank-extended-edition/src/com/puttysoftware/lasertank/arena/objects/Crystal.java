/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.Arena;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractReactionWall;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class Crystal extends AbstractReactionWall {
    private static void laserEnteredActionInnerP2(final int locX, final int locY, final int locZ, final int locW) {
	// Destroy barrel
	LaserTankEE.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, locW);
	// Check for nearby exploding barrels and blow them up too
	try {
	    final boolean boom2 = LaserTankEE.getApplication().getArenaManager().getArena()
		    .getCell(locX, locY - 1, locZ, locW).getClass().equals(Crystal.class);
	    if (boom2) {
		Crystal.laserEnteredActionInnerP2(locX, locY - 1, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final boolean boom4 = LaserTankEE.getApplication().getArenaManager().getArena()
		    .getCell(locX - 1, locY, locZ, locW).getClass().equals(Crystal.class);
	    if (boom4) {
		Crystal.laserEnteredActionInnerP2(locX - 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final boolean boom6 = LaserTankEE.getApplication().getArenaManager().getArena()
		    .getCell(locX + 1, locY, locZ, locW).getClass().equals(Crystal.class);
	    if (boom6) {
		Crystal.laserEnteredActionInnerP2(locX + 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final boolean boom8 = LaserTankEE.getApplication().getArenaManager().getArena()
		    .getCell(locX, locY + 1, locZ, locW).getClass().equals(Crystal.class);
	    if (boom8) {
		Crystal.laserEnteredActionInnerP2(locX, locY + 1, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
    }

    // Fields
    private boolean destroyed;

    // Constructors
    public Crystal() {
	super();
	this.addType(GameType.BARREL);
	this.setMaterial(Material.WOODEN);
	this.destroyed = false;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.CRYSTAL;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	// Boom!
	Sounds.play(Sound.BOOM);
	// Did tank die?
	final boolean dead = this.laserEnteredActionInnerP1(locX, locY, locZ, false);
	if (dead) {
	    // Kill tank
	    LaserTankEE.getApplication().getGameManager().gameOver();
	    return Direction.NONE;
	}
	Crystal.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	if (laserType == LaserType.POWER) {
	    // Laser keeps going
	    return DirectionHelper.resolveRelative(dirX, dirY);
	} else {
	    // Laser stops
	    return Direction.NONE;
	}
    }

    private boolean laserEnteredActionInnerP1(final int locX, final int locY, final int locZ, final boolean oldDead) {
	final Arena a = LaserTankEE.getApplication().getArenaManager().getArena();
	boolean dead = oldDead;
	// Check if this barrel's been destroyed already
	if (this.destroyed) {
	    return dead;
	}
	// Check for tank in range of explosion
	if (!dead) {
	    dead = a.circularScanTank(locX, locY, locZ, 1);
	}
	// Set destroyed status
	this.destroyed = true;
	// Check for nearby exploding barrels and blow them up too
	final boolean boom2 = LaserTankEE.getApplication().getArenaManager().getArena()
		.getCell(locX, locY - 1, locZ, this.getLayer()).getClass().equals(Crystal.class);
	if (boom2) {
	    return this.laserEnteredActionInnerP1(locX, locY - 1, locZ, dead);
	}
	final boolean boom4 = LaserTankEE.getApplication().getArenaManager().getArena()
		.getCell(locX - 1, locY, locZ, this.getLayer()).getClass().equals(Crystal.class);
	if (boom4) {
	    return this.laserEnteredActionInnerP1(locX - 1, locY, locZ, dead);
	}
	final boolean boom6 = LaserTankEE.getApplication().getArenaManager().getArena()
		.getCell(locX + 1, locY, locZ, this.getLayer()).getClass().equals(Crystal.class);
	if (boom6) {
	    return this.laserEnteredActionInnerP1(locX + 1, locY, locZ, dead);
	}
	final boolean boom8 = LaserTankEE.getApplication().getArenaManager().getArena()
		.getCell(locX, locY + 1, locZ, this.getLayer()).getClass().equals(Crystal.class);
	if (boom8) {
	    return this.laserEnteredActionInnerP1(locX, locY + 1, locZ, dead);
	}
	// Communicate tank dead status back to caller
	return dead;
    }

    @Override
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// React to balls hitting exploding barrels
	if (pushed.isOfType(GameType.BALL)) {
	    this.laserEnteredAction(x, y, z, 0, 0, LaserType.GREEN, 1);
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	// Boom!
	Sounds.play(Sound.BOOM);
	// Did tank die?
	final boolean dead = this.laserEnteredActionInnerP1(locX + dirX, locY + dirY, locZ, false);
	if (dead) {
	    // Kill tank
	    LaserTankEE.getApplication().getGameManager().gameOver();
	    return true;
	}
	// Destroy barrel
	Crystal.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	return true;
    }
}