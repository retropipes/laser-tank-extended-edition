/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTriggerButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class FireTriggerButton extends AbstractTriggerButton {
    // Constructors
    public FireTriggerButton() {
	super(new FireTriggerButtonDoor(), false);
	this.setMaterial(Material.FIRE);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.FIRE_TRIGGER_BUTTON;
    }
}