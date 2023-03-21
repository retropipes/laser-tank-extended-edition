/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.assets.Sound;
import com.puttysoftware.lasertank.assets.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class RollingCrystalHorizontal extends ArenaObject {
	// Constructors
	public RollingCrystalHorizontal() {
		super();
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.ROLLING_CRYSTAL_HORIZONTAL;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final LaserType laserType, final int forceUnits) {
		final var dir = DirectionHelper.resolveRelative(dirX, dirY);
		if (dir == Direction.EAST || dir == Direction.WEST) {
			// Roll
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		// Break up
		final var a = LaserTankEE.getArenaManager().getArena();
		// Boom!
		Sounds.play(Sound.PROXIMITY);
		// Destroy barrel
		LaserTankEE.getGame().morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.getLayer());
		// Check for tank in range of explosion
		final var target = a.circularScanTank(locX, locY, locZ, 1);
		if (target) {
			// Kill tank
			LaserTankEE.getGame().gameOver();
		}
		if (laserType == LaserType.POWER) {
			// Laser keeps going
			return DirectionHelper.resolveRelative(dirX, dirY);
		}
		// Laser stops
		return Direction.NONE;
	}

	@Override
	public Sound laserEnteredSound() {
		return Sound.ROLL;
	}

	@Override
	public void pushCollideAction(final ArenaObject pushed, final int x, final int y, final int z) {
		// Break up
		final var a = LaserTankEE.getArenaManager().getArena();
		// Boom!
		Sounds.play(Sound.PROXIMITY);
		// Destroy barrel
		LaserTankEE.getGame().morph(new ArenaObject(GameObjectID.PLACEHOLDER), x, y, z, this.getLayer());
		// Check for tank in range of explosion
		final var target = a.circularScanTank(x, y, z, 1);
		if (target) {
			// Kill tank
			LaserTankEE.getGame().gameOver();
		}
	}
}