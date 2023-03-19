/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;

public class MagneticBox3 extends AbstractMovableObject {
	// Constructors
	public MagneticBox3() {
		super();
		this.addType(GameType.BOX);
		this.addType(GameType.MAGNETIC_BOX);
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.MAGNETIC_BOX_3;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final LaserType laserType, final int forceUnits) {
		final var app = LaserTankEE.getApplication();
		final var mo = app.getArenaManager().getArena().getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
		if (laserType == LaserType.BLUE && mo != null && (mo.isOfType(GameType.CHARACTER) || !mo.isSolid())) {
			app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
			this.playSoundHook();
		} else if (mo != null && (mo.isOfType(GameType.CHARACTER) || !mo.isSolid())) {
			app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
			this.playSoundHook();
		} else if (laserType == LaserType.MISSILE) {
			Sounds.play(Sound.BOOM);
		} else {
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		return Direction.NONE;
	}

	@Override
	public void playSoundHook() {
		Sounds.play(Sound.PUSH_BOX);
	}
}