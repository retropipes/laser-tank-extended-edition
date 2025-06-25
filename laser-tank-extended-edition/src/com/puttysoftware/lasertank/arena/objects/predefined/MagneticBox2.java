/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.ArenaManager;
import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.Direction;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.LaserType;

public class MagneticBox2 extends ArenaObject {
    // Constructors
    public MagneticBox2() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.MAGNETIC_BOX_2;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final LaserType laserType, final int forceUnits) {
	final var mo = ArenaManager.getArena().getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
	if (laserType == LaserType.BLUE && mo != null && (mo.canControl() || !mo.isSolid())) {
	    Game.updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
	    Sounds.play(this.laserEnteredSound());
	} else if (mo != null && (mo.canControl() || !mo.isSolid())) {
	    Game.updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
	    Sounds.play(this.laserEnteredSound());
	} else if (laserType == LaserType.MISSILE) {
	    Sounds.play(Sound.BOOM);
	} else {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return Direction.NONE;
    }
}