/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;

public class Crystal extends ArenaObject {
    private static void laserEnteredActionInnerP2(final int locX, final int locY, final int locZ, final int locW) {
	// Destroy crystal
	Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, locW);
	// Check for nearby crystals and blow them up too
	try {
	    final var boom2 = ArenaManager.getArena().getCell(locX, locY - 1, locZ, locW).getClass()
		    .equals(Crystal.class);
	    if (boom2) {
		Crystal.laserEnteredActionInnerP2(locX, locY - 1, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final var boom4 = ArenaManager.getArena().getCell(locX - 1, locY, locZ, locW).getClass()
		    .equals(Crystal.class);
	    if (boom4) {
		Crystal.laserEnteredActionInnerP2(locX - 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final var boom6 = ArenaManager.getArena().getCell(locX + 1, locY, locZ, locW).getClass()
		    .equals(Crystal.class);
	    if (boom6) {
		Crystal.laserEnteredActionInnerP2(locX + 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final var boom8 = ArenaManager.getArena().getCell(locX, locY + 1, locZ, locW).getClass()
		    .equals(Crystal.class);
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
	this.destroyed = false;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.CRYSTAL;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	// Did tank die?
	final var dead = this.laserEnteredActionInnerP1(locX, locY, locZ, false);
	if (dead) {
	    // Kill tank
	    Game.gameOver();
	    return Direction.NONE;
	}
	Crystal.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	if (laserType == LaserType.POWER) {
	    // Laser keeps going
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Laser stops
	return Direction.NONE;
    }

    private boolean laserEnteredActionInnerP1(final int locX, final int locY, final int locZ, final boolean oldDead) {
	final var a = ArenaManager.getArena();
	var dead = oldDead;
	// Check if this crystal's been destroyed already
	if (this.destroyed) {
	    return dead;
	}
	// Check for tank in range of explosion
	if (!dead) {
	    dead = a.circularScanTank(locX, locY, locZ, 1);
	}
	// Set destroyed status
	this.destroyed = true;
	// Check for nearby crystals and blow them up too
	final var boom2 = ArenaManager.getArena().getCell(locX, locY - 1, locZ, this.getLayer()).getClass()
		.equals(Crystal.class);
	if (boom2) {
	    return this.laserEnteredActionInnerP1(locX, locY - 1, locZ, dead);
	}
	final var boom4 = ArenaManager.getArena().getCell(locX - 1, locY, locZ, this.getLayer()).getClass()
		.equals(Crystal.class);
	if (boom4) {
	    return this.laserEnteredActionInnerP1(locX - 1, locY, locZ, dead);
	}
	final var boom6 = ArenaManager.getArena().getCell(locX + 1, locY, locZ, this.getLayer()).getClass()
		.equals(Crystal.class);
	if (boom6) {
	    return this.laserEnteredActionInnerP1(locX + 1, locY, locZ, dead);
	}
	final var boom8 = ArenaManager.getArena().getCell(locX, locY + 1, locZ, this.getLayer()).getClass()
		.equals(Crystal.class);
	if (boom8) {
	    return this.laserEnteredActionInnerP1(locX, locY + 1, locZ, dead);
	}
	// Communicate tank dead status back to caller
	return dead;
    }

    @Override
    public void pushCollideAction(final ArenaObject pushed, final int x, final int y, final int z) {
	// React to balls hitting crystals
	if (pushed.canRoll()) {
	    this.laserEnteredAction(x, y, z, 0, 0, LaserType.GREEN, 1);
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	// Did tank die?
	final var dead = this.laserEnteredActionInnerP1(locX + dirX, locY + dirY, locZ, false);
	if (dead) {
	    // Kill tank
	    Game.gameOver();
	    return true;
	}
	// Destroy crystal
	Crystal.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	return true;
    }
}