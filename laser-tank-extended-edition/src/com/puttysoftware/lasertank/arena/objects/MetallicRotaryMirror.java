/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractReactionWall;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class MetallicRotaryMirror extends AbstractReactionWall {
    // Constructors
    public MetallicRotaryMirror() {
	super();
	this.setDirection(Direction.NORTHEAST);
	this.setDiagonalOnly(true);
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.METALLIC_ROTARY_MIRROR;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	final Direction dir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
	if (this.hitReflectiveSide(dir)) {
	    // Reflect laser
	    return this.getDirection();
	} else {
	    // Rotate mirror
	    this.toggleDirection();
	    Sounds.play(Sound.ROTATE);
	    return Direction.NONE;
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	// Rotate mirror
	this.toggleDirection();
	Sounds.play(Sound.ROTATE);
	LaserTankEE.getApplication().getArenaManager().getArena().markAsDirty(locX + dirX, locY + dirY, locZ);
	return true;
    }
}
