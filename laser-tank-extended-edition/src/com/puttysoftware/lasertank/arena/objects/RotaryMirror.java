/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.RangeType;

public class RotaryMirror extends ArenaObject {
	// Constructors
	public RotaryMirror() {
	}

	@Override
	public boolean doLasersPassThrough() {
		return true;
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.ROTARY_MIRROR;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final LaserType laserType, final int forceUnits) {
		if (laserType == LaserType.MISSILE) {
			// Destroy mirror
			Sounds.play(Sound.BOOM);
			LaserTankEE.getGame().morph(new Empty(), locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		}
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final RangeType rangeType, final int forceUnits) {
		// Rotate mirror
		this.toggleDirection();
		Sounds.play(Sound.ROTATE);
		LaserTankEE.getArenaManager().getArena().markAsDirty(locX + dirX, locY + dirY, locZ);
		return true;
	}
}
