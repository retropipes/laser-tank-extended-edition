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
import com.puttysoftware.lasertank.index.RangeType;

public class Bricks extends ArenaObject {
	// Constructors
	public Bricks() {
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.BRICKS;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final LaserType laserType, final int forceUnits) {
		Sounds.play(Sound.BREAK_BRICKS);
		Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.getLayer());
		if (laserType == LaserType.POWER) {
			// Laser keeps going
			return DirectionHelper.resolveRelative(dirX, dirY);
		}
		// Laser stops
		return Direction.NONE;
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final RangeType rangeType, final int forceUnits) {
		Sounds.play(Sound.BREAK_BRICKS);
		Game.get().morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX + dirX, locY + dirY, locZ,
				this.getLayer());
		return true;
	}
}