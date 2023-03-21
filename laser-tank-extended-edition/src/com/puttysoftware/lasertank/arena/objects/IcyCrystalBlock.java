/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class IcyCrystalBlock extends ArenaObject {
	// Constructors
	public IcyCrystalBlock() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		switch (materialID) {
			case FIRE:
				if (this.hasPreviousState()) {
					return this.getPreviousState();
				} else {
					return new CrystalBlock();
				}
			default:
				return this;
		}
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.ICY_CRYSTAL_BLOCK;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final LaserType laserType, final int forceUnits) {
		if (laserType == LaserType.MISSILE) {
			// Destroy icy crystal block
			Sounds.play(Sound.BOOM);
			LaserTankEE.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		}
		// Stop laser
		Sounds.play(Sound.LASER_DIE);
		return Direction.NONE;
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final RangeType rangeType, final int forceUnits) {
		if (RangeTypeHelper.material(rangeType) == Material.METALLIC) {
			// Destroy icy crystal block
			LaserTankEE.getApplication().getGameManager().morph(new Empty(), locX + dirX, locY + dirY, locZ,
					this.getLayer());
			return true;
		}
		if (RangeTypeHelper.material(rangeType) == Material.FIRE) {
			// Heat up crystal block
			Sounds.play(Sound.MELT);
			LaserTankEE.getApplication().getGameManager().morph(this.changesToOnExposure(Material.FIRE), locX + dirX,
					locY + dirY, locZ, this.getLayer());
		} else if (RangeTypeHelper.material(rangeType) == Material.ICE) {
		}
		return true;
	}
}