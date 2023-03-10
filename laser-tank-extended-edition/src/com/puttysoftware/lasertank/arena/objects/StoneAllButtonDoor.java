/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractAllButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;

public class StoneAllButtonDoor extends AbstractAllButtonDoor {
    // Constructors
    public StoneAllButtonDoor() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.STONE_ALL_DOOR;
    }
}