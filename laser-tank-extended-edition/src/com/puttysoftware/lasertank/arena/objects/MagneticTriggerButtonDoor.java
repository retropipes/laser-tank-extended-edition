/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTriggerButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class MagneticTriggerButtonDoor extends AbstractTriggerButtonDoor {
    // Constructors
    public MagneticTriggerButtonDoor() {
	super();
	this.setMaterial(Material.MAGNETIC);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.MAGNETIC_TRIGGER_DOOR;
    }
}