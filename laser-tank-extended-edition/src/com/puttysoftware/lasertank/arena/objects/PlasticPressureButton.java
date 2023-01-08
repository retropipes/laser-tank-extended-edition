/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class PlasticPressureButton extends AbstractPressureButton {
    // Constructors
    public PlasticPressureButton() {
	super(new PlasticPressureButtonDoor(), false);
	this.setMaterial(Material.PLASTIC);
    }

    @Override
    public final GameObjectID getID() {
	return GameObjectID.PLASTIC_PRESSURE_BUTTON;
    }
}