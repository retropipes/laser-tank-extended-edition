/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractReactionWall;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.helper.DirectionHelper;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;
import com.puttysoftware.lasertank.index.RangeType;

public class ProximityCrystal extends AbstractReactionWall {
    // Constructors
    public ProximityCrystal() {
	this.addType(GameType.BARREL);
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.PROXIMITY_CRYSTAL;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final LaserType laserType, final int forceUnits) {
	final var a = LaserTankEE.getApplication().getArenaManager().getArena();
	// Boom!
	Sounds.play(Sound.PROXIMITY);
	// Destroy barrel
	LaserTankEE.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	// Check for tank in range of explosion
	final var target = a.circularScanTank(locX, locY, locZ, 1);
	if (target) {
	    // Kill tank
	    LaserTankEE.getApplication().getGameManager().gameOver();
	}
	if (laserType == LaserType.POWER) {
	    // Laser keeps going
	    return DirectionHelper.resolveRelative(dirX, dirY);
	}
	// Laser stops
	return Direction.NONE;
    }

    @Override
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// React to balls hitting barrels
	if (pushed.isOfType(GameType.BALL)) {
	    this.laserEnteredAction(x, y, z, 0, 0, LaserType.GREEN, 1);
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final RangeType rangeType, final int forceUnits) {
	final var a = LaserTankEE.getApplication().getArenaManager().getArena();
	// Boom!
	Sounds.play(Sound.PROXIMITY);
	// Check for tank in range of explosion
	final var target = a.circularScanTank(locX + dirX, locY + dirY, locZ, 1);
	if (target) {
	    // Kill tank
	    LaserTankEE.getApplication().getGameManager().gameOver();
	    return true;
	}
	// Destroy barrel
	LaserTankEE.getApplication().getGameManager().morph(new Empty(), locX + dirX, locY + dirY, locZ,
		this.getLayer());
	return true;
    }
}