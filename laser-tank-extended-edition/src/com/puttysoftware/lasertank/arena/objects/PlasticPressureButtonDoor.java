/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;

public class PlasticPressureButtonDoor extends AbstractPressureButtonDoor {
    // Constructors
    public PlasticPressureButtonDoor() {
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.PLASTIC_PRESSURE_DOOR;
    }
}