/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class StonePressureButtonDoor extends AbstractPressureButtonDoor {
    // Constructors
    public StonePressureButtonDoor() {
	super();
	this.setMaterial(Material.STONE);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.STONE_PRESSURE_DOOR;
    }
}