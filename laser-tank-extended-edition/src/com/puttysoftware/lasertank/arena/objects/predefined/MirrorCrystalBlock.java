/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class MirrorCrystalBlock extends ArenaObject {
	// Constructors
	public MirrorCrystalBlock() {
	}

	@Override
	public final GameObjectID getID() {
		return GameObjectID.MIRROR_CRYSTAL_BLOCK;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final LaserType laserType, final int forceUnits) {
		if (laserType == LaserType.MISSILE) {
			// Destroy mirror crystal block
			Sounds.play(Sound.BOOM);
			Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
			return Direction.NONE;
		}
		if (laserType == LaserType.BLUE) {
			// Pass laser through
			return DirectionHelper.resolveRelative(dirX, dirY);
		}
		// Reflect laser
		return DirectionHelper.resolveRelativeInvert(dirX, dirY);
	}

	@Override
	public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final LaserType laserType) {
		return DirectionHelper.resolveRelative(dirX, dirY);
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final RangeType rangeType, final int forceUnits) {
		if (rangeType == RangeType.BOMB || RangeTypeHelper.material(rangeType) == Material.METALLIC) {
			// Destroy mirror crystal block
			Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX + dirX, locY + dirY, locZ, this.layer());
			return true;
		}
		if (RangeTypeHelper.material(rangeType) == Material.FIRE) {
			// Heat up mirror crystal block
			Sounds.play(Sound.MELT);
			Game.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.layer());
		} else if (RangeTypeHelper.material(rangeType) == Material.ICE) {
			// Freeze mirror crystal block
			Sounds.play(Sound.FREEZE);
			Game.morph(this.changesToOnExposure(Material.ICE), locX + dirX, locY + dirY, locZ, this.layer());
		}
		return true;
	}
}