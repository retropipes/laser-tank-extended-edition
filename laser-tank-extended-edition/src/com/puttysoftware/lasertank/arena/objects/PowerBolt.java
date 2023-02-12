/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractField;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PowerBolt extends AbstractField {
    // Constructors
    public PowerBolt() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.POWER_BOLT;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	Sounds.play(Sound.POWERFUL);
	LaserTankEE.getApplication().getGameManager().morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
	LaserTankEE.getApplication().getGameManager().setPowerfulTank();
    }
}