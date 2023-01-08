/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTriggerButtonDoor;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class MetallicTriggerButtonDoor extends AbstractTriggerButtonDoor {
    // Constructors
    public MetallicTriggerButtonDoor() {
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.METALLIC_TRIGGER_DOOR;
    }
}