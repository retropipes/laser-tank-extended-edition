/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.Application;
import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTeleport;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;

public class StairsUp extends AbstractTeleport {
    // Constructors
    public StairsUp() {
	super();
    }

    @Override
    public int getDestinationFloor() {
	final Application app = LaserTankEE.getApplication();
	return app.getGameManager().getPlayerManager().getPlayerLocationZ() + 1;
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.STAIRS_UP;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final Application app = LaserTankEE.getApplication();
	app.getGameManager().updatePositionAbsoluteNoEvents(this.getDestinationFloor());
	Sounds.play(Sound.UP);
    }
}
