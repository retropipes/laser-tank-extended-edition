/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class RollingCrystalVertical extends ArenaObject {
    // Constructors
    public RollingCrystalVertical() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ROLLING_CRYSTAL_VERTICAL;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var dir = DirectionHelper.resolveRelative(dirX, dirY);
	if (dir == Direction.NORTH || dir == Direction.SOUTH) {
	    // Roll
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Break up
	final var a = ArenaManager.getArena();
	// Boom!
	Sounds.play(Sound.PROXIMITY);
	// Destroy barrel
	Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.getLayer());
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
	// Break up
	final var a = ArenaManager.getArena();
	// Boom!
	Sounds.play(Sound.PROXIMITY);
	// Destroy barrel
	Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, this.getLayer());
	// Check for tank in range of explosion
	final var target = a.circularScanTank(x, y, z, 1);
	if (target) {
	    // Kill tank
	    Game.gameOver();
	}
    }
}