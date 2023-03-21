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

public class ShadowCrystalBlock extends ArenaObject {
	// Constructors
	public ShadowCrystalBlock() {
	}

	@Override
	public ArenaObject changesToOnExposure(final Material materialID) {
		return switch (materialID) {
			case ICE -> {
				final var icb = new IcyCrystalBlock();
				icb.setPreviousState(this);
				yield icb;
			}
			case FIRE -> new HotCrystalBlock();
			default -> this;
		};
	}

	@Override
	public boolean doLasersPassThrough() {
		return true;
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.SHADOW_CRYSTAL_BLOCK;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final LaserType laserType, final int forceUnits) {
		if (laserType == LaserType.MISSILE) {
			// Destroy crystal block
			Sounds.play(Sound.BOOM);
			LaserTankEE.getGame().morph(new Empty(), locX, locY, locZ, this.getLayer());
		}
		// Stop laser
		return Direction.NONE;
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final RangeType rangeType, final int forceUnits) {
		if (RangeTypeHelper.material(rangeType) == Material.METALLIC) {
			// Destroy crystal block
			LaserTankEE.getGame().morph(new Empty(), locX + dirX, locY + dirY, locZ,
					this.getLayer());
			return true;
		}
		if (RangeTypeHelper.material(rangeType) == Material.FIRE) {
			// Heat up crystal block
			Sounds.play(Sound.MELT);
			LaserTankEE.getGame().morph(this.changesToOnExposure(Material.FIRE), locX + dirX,
					locY + dirY, locZ, this.getLayer());
		} else if (RangeTypeHelper.material(rangeType) == Material.ICE) {
			// Freeze crystal block
			Sounds.play(Sound.FREEZE);
			LaserTankEE.getGame().morph(this.changesToOnExposure(Material.ICE), locX + dirX,
					locY + dirY, locZ, this.getLayer());
		}
		return true;
	}
}