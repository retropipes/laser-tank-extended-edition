/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractArenaObject;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractMovableObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.GameType;
import com.puttysoftware.lasertank.index.LaserType;
import com.puttysoftware.lasertank.index.Material;

public class MagneticBox extends AbstractMovableObject {
    // Constructors
    public MagneticBox() {
	super(true);
	this.addType(GameType.BOX);
	this.addType(GameType.MAGNETIC_BOX);
	this.setMaterial(Material.MAGNETIC);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.MAGNETIC_BOX;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final Application app = LaserTankEE.getApplication();
	final AbstractArenaObject mo = app.getArenaManager().getArena().getCell(locX - dirX, locY - dirY, locZ,
		this.getLayer());
	if (laserType == LaserType.BLUE && mo != null && (mo.isOfType(GameType.CHARACTER) || !mo.isSolid())) {
	    app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
	    this.playSoundHook();
	} else if (mo != null && (mo.isOfType(GameType.CHARACTER) || !mo.isSolid())) {
	    app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
	    this.playSoundHook();
	} else {
	    if (laserType == LaserType.MISSILE) {
		Sounds.play(Sound.BOOM);
	    } else {
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	Sounds.play(Sound.PUSH_BOX);
    }
}