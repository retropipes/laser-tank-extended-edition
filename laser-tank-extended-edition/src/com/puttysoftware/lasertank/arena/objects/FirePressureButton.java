/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class FirePressureButton extends AbstractPressureButton {
    // Constructors
    public FirePressureButton() {
	super(new FirePressureButtonDoor(), false);
	this.setMaterial(Material.FIRE);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.FIRE_PRESSURE_BUTTON;
    }
}