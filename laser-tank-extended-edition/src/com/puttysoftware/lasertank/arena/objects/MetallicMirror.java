/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;

public class MetallicMirror extends AbstractMovableObject {
    // Constructors
    public MetallicMirror() {
	super(true);
	this.addType(GameType.MOVABLE_MIRROR);
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_MIRROR;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var dir = DirectionHelper.resolveRelativeInvert(dirX, dirY);
	if (this.hitReflectiveSide(dir)) {
	    // Reflect laser
	    return this.getDirection();
	}
	// Move mirror
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_MIRROR);
    }
}
