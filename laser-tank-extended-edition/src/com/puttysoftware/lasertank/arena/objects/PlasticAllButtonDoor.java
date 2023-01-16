/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abc.AbstractAllButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PlasticAllButtonDoor extends AbstractAllButtonDoor {
    // Constructors
    public PlasticAllButtonDoor() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.PLASTIC_ALL_DOOR;
    }
}