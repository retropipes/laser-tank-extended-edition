/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractTriggerButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class StoneTriggerButton extends AbstractTriggerButton {
    // Constructors
    public StoneTriggerButton() {
	super(new StoneTriggerButtonDoor(), false);
	this.setMaterial(Material.STONE);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.STONE_TRIGGER_BUTTON;
    }
}