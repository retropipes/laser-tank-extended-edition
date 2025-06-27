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

public class ProximityCrystal extends ArenaObject {
    // Constructors
    public ProximityCrystal() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.PROXIMITY_CRYSTAL;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	final var a = ArenaManager.getArena();
	// Destroy barrel
	Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
	// Check for tank in range of explosion
	final var target = a.circularScanTank(locX, locY, locZ, 1);
	if (target) {
	    // Kill tank
	    Game.gameOver();
	}
	if (laserType == LaserType.POWER) {
	    // Laser keeps going
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Laser stops
	return Direction.NONE;
    }

    @Override
    public void pushCollideAction(final ArenaObject pushed, final int x, final int y, final int z) {
	// React to balls hitting barrels
	if (pushed.canRoll()) {
	    this.laserEnteredAction(x, y, z, 0, 0, LaserType.GREEN, 1);
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	final var a = ArenaManager.getArena();
	// Check for tank in range of explosion
	final var target = a.circularScanTank(locX + dirX, locY + dirY, locZ, 1);
	if (target) {
	    // Kill tank
	    Game.gameOver();
	    return true;
	}
	// Destroy barrel
	Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX + dirX, locY + dirY, locZ, this.layer());
	return true;
    }
}