/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.ArenaObject;
import com.puttysoftware.lasertank.index.GameObjectID;

public class FirePressureButtonDoor extends ArenaObject {
    // Constructors
    public FirePressureButtonDoor() {
    }

    @Override
    public final GameObjectID getID() {
        return GameObjectID.FIRE_PRESSURE_DOOR;
    }
}