/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.helper.RangeTypeHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class HotCrystalBlock extends ArenaObject {
    // Constructors
    public HotCrystalBlock() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.HOT_CRYSTAL_BLOCK;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	if (laserType == LaserType.MISSILE) {
	    // Destroy hot crystal block
	    Sounds.play(Sound.BOOM);
	    Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX, locY, locZ, this.layer());
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
	    // Destroy hot crystal block
	    Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), locX + dirX, locY + dirY, locZ, this.layer());
	    return true;
	}
	if (RangeTypeHelper.material(rangeType) == Material.FIRE
		|| RangeTypeHelper.material(rangeType) != Material.ICE) {
	} else {
	    // Freeze crystal block
	    Sounds.play(Sound.FREEZE);
	    Game.morph(this.changesToOnExposure(Material.ICE), locX + dirX, locY + dirY, locZ, this.layer());
	}
	// Do nothing
	return true;
    }
}