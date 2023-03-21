/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.LaserTankEE;
import com.puttysoftware.lasertank.arena.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.utility.TankInventory;

public class WaterDoor extends ArenaObject {
    // Constructors
    public WaterDoor() {
        super();
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.WATER_DOOR;
    }

    // Scriptability
    @Override
    public boolean isConditionallySolid() {
        return TankInventory.getBlueKeysLeft() < 1;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
        Sounds.play(Sound.UNLOCK);
        TankInventory.useBlueKey();
        LaserTankEE.getApplication().getGameManager().morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}