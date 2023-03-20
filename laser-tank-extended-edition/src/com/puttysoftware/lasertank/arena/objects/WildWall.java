/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.arena.abc.AbstractWall;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;

public class WildWall extends AbstractWall {
	// Constructors
	public WildWall() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		return switch (materialID) {
			case ICE -> {
				final var iw = new IcyWall();
				iw.setPreviousState(this);
				yield iw;
			}
			case FIRE -> new HotWall();
			default -> this;
		};
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.WILD_WALL;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final LaserType laserType, final int forceUnits) {
		if (laserType == LaserType.MISSILE) {
			// Heat up wall
			Sounds.play(Sound.MELT);
			LaserTankEE.getApplication().getGameManager().morph(new HotWall(), locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		}
		if (laserType == LaserType.STUNNER) {
			// Freeze wall
			Sounds.play(Sound.FREEZE);
			final var iw = new IcyWall();
			iw.setPreviousState(this);
			LaserTankEE.getApplication().getGameManager().morph(iw, locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		}
		// Stop laser
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
}