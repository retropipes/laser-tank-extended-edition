/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects.predefined;

import com.puttysoftware.lasertank.arena.objects.ArenaObject;
import com.puttysoftware.lasertank.asset.Sound;
import com.puttysoftware.lasertank.asset.Sounds;
import com.puttysoftware.lasertank.game.Game;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.utility.TankInventory;

public class MagneticDoor extends ArenaObject {
    // Constructors
    public MagneticDoor() {
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.MAGNETIC_DOOR;
    }

    @Override
    public boolean isConditionallySolid() {
        return TankInventory.getGreenKeysLeft() < 1;
    }

    @Override
    public void postMoveActionHook(final int dirX, final int dirY, final int dirZ) {
        Sounds.play(Sound.UNLOCK);
        TankInventory.useGreenKey();
        Game.morph(new ArenaObject(GameObjectID.PLACEHOLDER), dirX, dirY, dirZ, this.layer());
    }
}