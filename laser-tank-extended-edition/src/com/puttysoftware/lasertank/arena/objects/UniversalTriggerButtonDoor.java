/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTriggerButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class UniversalTriggerButtonDoor extends AbstractTriggerButtonDoor {
    // Constructors
    public UniversalTriggerButtonDoor() {
	this.setMaterial(Material.NONE);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.UNIVERSAL_TRIGGER_DOOR;
    }
}