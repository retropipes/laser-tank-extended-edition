/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class WildWall extends ArenaObject {
    // Constructors
    public WildWall() {
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
	    Game.morph(new ArenaObject(GameObjectID.HOT_WALL), locX, locY, locZ, this.layer());
	    return Direction.NONE;
	}
	if (laserType == LaserType.STUNNER) {
	    // Freeze wall
	    Sounds.play(Sound.FREEZE);
	    final var iw = new ArenaObject(GameObjectID.ICY_WALL);
	    iw.setPreviousState(this);
	    Game.morph(iw, locX, locY, locZ, this.layer());
	    return Direction.NONE;
	}
	// Stop laser
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }
}