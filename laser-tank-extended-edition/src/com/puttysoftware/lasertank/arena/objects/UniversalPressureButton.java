/*  LaserTank: An Arena-Solving Game
 Copyright (C) 2008-2013 Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.lasertank.arena.objects;

import com.puttysoftware.lasertank.arena.abstractobjects.AbstractPressureButton;
import com.puttysoftware.lasertank.index.GameObjectID;
import com.puttysoftware.lasertank.index.Material;

public class UniversalPressureButton extends AbstractPressureButton {
    // Constructors
    public UniversalPressureButton() {
	super(new UniversalPressureButtonDoor(), true);
	this.setMaterial(Material.NONE);
    }

    @Override
    public final GameObjectID getStringBaseID() {
	return GameObjectID.UNIVERSAL_PRESSURE_BUTTON;
    }
}