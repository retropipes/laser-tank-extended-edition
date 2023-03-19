/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.abc.AbstractDoor;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.utility.TankInventory;

public class RedDoor extends AbstractDoor {
    // Constructors
    public RedDoor() {
        super(new RedKey());
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.RED_DOOR;
    }

    // Scriptability
    @Override
    public boolean isConditionallySolid() {
        return TankInventory.getRedKeysLeft() < 1;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
        Sounds.play(Sound.UNLOCK);
        TankInventory.useRedKey();
        LaserTankEE.getApplication().getGameManager().morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}