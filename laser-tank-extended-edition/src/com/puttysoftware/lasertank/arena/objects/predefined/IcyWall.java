/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class IcyWall extends ArenaObject {
	// Constructors
	public IcyWall() {
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.ICY_WALL;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final LaserType laserType, final int forceUnits) {
		if (laserType != LaserType.MISSILE) {
			// Stop laser
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		// Defrost icy wall
		Sounds.play(Sound.DEFROST);
		ArenaObject ao;
		if (this.hasPreviousState()) {
			ao = this.getPreviousState();
		} else {
			ao = new ArenaObject(GameObjectID.WALL);
		}
		Game.morph(ao, locX, locY, locZ, this.layer());
		return Direction.NONE;
	}
}