/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class IcePressureButtonDoor extends AbstractPressureButtonDoor {
    // Constructors
    public IcePressureButtonDoor() {
	this.setMaterial(Material.ICE);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.ICE_PRESSURE_DOOR;
    }
}